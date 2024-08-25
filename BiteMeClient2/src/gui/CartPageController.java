package gui;

import java.util.ArrayList;
import EnumsAndConstants.CommandConstants;
import EnumsAndConstants.OrderStatus;
import EnumsAndConstants.TypeOfOrder;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Callback;
import logic.CommMessage;
import logic.Restaurant;
import logic.items;
import logic.Orders.Delivery;
import logic.Orders.Order;
import logic.Users.User;

//Import statements for necessary classes and packages

public class CartPageController {

	// FXML annotated fields for UI elements
	@FXML
	private Button btnBack;
	@FXML
	private Button btnDeleteDish;
	@FXML
	private Button btnPay;
	@FXML
	private ListView<items> lstview; // ListView for displaying items in the cart

	@FXML
	private TextField price; // TextField for displaying the total price

	@FXML
	private Text error; // Text for displaying error messages

	// Variables to hold order-related data
	private Order order; // The current order
	private User user; // The current user
	private Restaurant res; // The restaurant associated with the order
	private Delivery delivery; // Delivery details for the order
	private double finalPriceWithReductions = 0; // Total price of the order
	private double totalDiscount = 0; // Total discount applied to the order
	private Helper helper = new Helper(); // Helper class instance (assuming it's a utility class)
	private ArrayList<items> itemsInTheOrder = new ArrayList<>(); // List to assist with managing items in the order

	// Arthur addons
	private double itemsSumPrice;
	private double refundAmount;

	// Constructor to initialize the controller with order, user, restaurant, and
	// delivery information
	public CartPageController(Order order, User user, Restaurant res, Delivery delivery) {
		this.order = order;
		this.user = user;
		this.res = res;
		this.delivery = delivery;
	}

	/**
	 * Initializes the controller class. This method sets up the cart view,
	 * calculates prices, and applies discounts.
	 * 
	 * @throws InterruptedException if the thread is interrupted while waiting for
	 *                              data
	 */
	/**
	 * Initializes the controller class. This method sets up the cart view,
	 * calculates prices, and applies discounts.
	 * 
	 * @throws InterruptedException if the thread is interrupted while waiting for
	 *                              data
	 */
	@FXML
	public void initialize() throws InterruptedException {
		// Check if the order is empty and set the view accordingly
		if (order.getItemsInOrder() == null || order.getItemsInOrder().isEmpty()) {
			lstview.setItems(FXCollections.observableArrayList());
			price.setText("0.00 ₪");
			return;
		}
		itemsSumPrice = order.getTotal_price(); // save the sum of all items in order before reductions

		// Request refund data for the user
		ArrayList<String> serverDbParametrs = new ArrayList<>();
		serverDbParametrs.add(user.getUserName());
		CommMessage msg1 = new CommMessage(CommandConstants.GetRefund, serverDbParametrs);
		ClientUI.RequestData(msg1);

		// Wait for refund data to be received
		while (Helper.Refund == null) {
			Thread.sleep(50);
			if (Helper.errorMsg != null) {
				if (!Helper.errorMsg.equals("No refunds for this user")) {
					error.setVisible(true);
					error.setText(Helper.errorMsg);
				}
				break;
			}
		}

		// Process items in the order
		itemsInTheOrder = order.getItemsInOrder();
		boolean flag = true;
		
		for(items item : itemsInTheOrder ) {
			if(item.getItemName().equals("Refund")) {
				flag = false;
				break;
			}
		}
		
		if (Helper.Refund != null) {
			if(flag) {
				// Add a new "Refund" item with the refund amount from Helper.Refund
				itemsInTheOrder.add(new items("Refund", Helper.Refund, 100));
			}
		} else {
			if(flag) {
				itemsInTheOrder.add(new items("Refund", 0.0, 100));
			}
		}
		
		int numOfDeliveries = 0;
		for(items item : itemsInTheOrder ) {
			if(item.getItemName().equals("Delivery")) {
				numOfDeliveries++;
			}
		}
		if(numOfDeliveries == 0) {
			// Add a new "Delivery" item for each participant in the delivery
			for (int i = 0; i < delivery.getNumOfParticipants(); i++) {
				itemsInTheOrder.add(new items("Delivery", delivery.getDeliveryFee(), 99));
			}
		}

		// everybody needs delivery
		finalPriceWithReductions = order.getTotal_price() + delivery.getDeliveryFee() * delivery.getNumOfParticipants();

		// Apply discount for pre-orders
		if (order.getType().equals(TypeOfOrder.PRE)) {
			totalDiscount = finalPriceWithReductions * 0.10; // 10% discount
			// If the discount is greater than 0, add a "Discount" item to the order
			if (totalDiscount > 0) {
				itemsInTheOrder.add(new items("Discount 10% for pre-order", -totalDiscount, 101));
			}
			System.out.println(String.format(
					"order total is %.2f ₪, including delivery its %.2f ₪, after 10%% discount its now %.2f ₪",
					itemsSumPrice, finalPriceWithReductions, (finalPriceWithReductions - totalDiscount)));

			// Subtract the discount from the total price
			finalPriceWithReductions -= totalDiscount;

			// Update the price label with the new total price, formatted to two decimal
			// places
			price.setText(String.format("%.2f ₪", finalPriceWithReductions));
		}

		// Apply refund if available
		if (Helper.Refund != null) {
			refundAmount = Helper.Refund;
			if (refundAmount >= finalPriceWithReductions) {
				price.setText("0.00 ₪");
				order.setTotal_price(0);
			} else {
				System.out.println(String.format("Using account refund, total now is %.2f - %.2f = %.2f ₪",
						finalPriceWithReductions, refundAmount, (finalPriceWithReductions - refundAmount)));
				finalPriceWithReductions -= refundAmount;
				price.setText(String.format("%.2f ₪", finalPriceWithReductions));
			}
		} else {
			price.setText(String.format("%.2f ₪", finalPriceWithReductions));
		}

		// Set up the ListView with custom cell factory
		ObservableList<items> observableList = FXCollections.observableArrayList(order.getItemsInOrder());
		lstview.setItems(observableList);
		lstview.setCellFactory(new Callback<ListView<items>, ListCell<items>>() {
			// Custom ListCell implementation to display item details
			class itemCell extends ListCell<items> {
				@Override
				protected void updateItem(items item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null) {
						setText(null);
					} else {
						String text = item.getItemName() + ", " + (item.getItemName().equals("Refund")? "-" + String.format("%.2f",item.getPrice()): String.format("%.2f",item.getPrice())) + "₪";
						if (item.getDoneness() != null) {
							text += ", " + item.getDoneness();
						}
						if (item.getSize() != null) {
							text += ", " + item.getSize();
						}
						if (item.getRestrictions() != null) {
							text += ", " + item.getRestrictions();
						}
						setText(text);
					}
				}
			}

			@Override
			public ListCell<items> call(ListView<items> listView) {
				return new itemCell();
			}
		});
	}

	/**
	 * Handles the deletion of a dish from the cart.
	 * 
	 * @param event The ActionEvent triggered by the delete button
	 * @throws Exception if an error occurs during the operation
	 */
	@FXML
	public void handleBtnDeleteDish(ActionEvent event) throws Exception {
		// Get the selected item from the ListView

		items itemBeingDeleted = lstview.getSelectionModel().getSelectedItem();

		// Error handling for invalid selections
		if (itemBeingDeleted == null) {
			error.setVisible(true);
			error.setText("You need to pick one dish to remove at a time.");
			return;
		}

		// Error handling for attempting to delete special items like Refund, Delivery,
		// or Discount
		if (itemBeingDeleted.getItemName().equals("Refund")) {
			error.setVisible(true);
			error.setText("You cannot delete the refund.");
			return;
		}

		if (itemBeingDeleted.getItemName().equals("Delivery")) {
			error.setVisible(true);
			error.setText("You cannot delete the delivery fee.");
			return;
		}

		if (itemBeingDeleted.getItemName().equals("Discount 10% for pre-order")) {
			error.setVisible(true);
			error.setText("You cannot delete the discount.");
			return;
		}
		ArrayList<items> helpMe = new ArrayList<items>();
		for(items item: itemsInTheOrder) {
			if(!item.getItemName().equals("Discount 10% for pre-order") &&
			   !item.getItemName().equals("Refund") &&
			   !item.getItemName().equals("Delivery")) {
				helpMe.add(item);
			}
		}
		
		itemsInTheOrder = helpMe;

		// Remove the selected item from the order
		if (itemBeingDeleted != null) {
			if (itemsInTheOrder.contains(itemBeingDeleted)) {
				order.setNumOfItems(order.getNumOfItems() - 1);
				itemsInTheOrder.remove(itemBeingDeleted);
				order.setItemsInOrder(itemsInTheOrder);
				order.sumUpItemsTotal(); // update the total price with removed item
				itemsSumPrice = order.getTotal_price();
				System.out.println("The sum of food in the order is  " + String.format("%.2f ₪", itemsSumPrice));
			}
		}

		// Additional check to remove the discount if the price is zero
		if (price.getText().equals("0.00 ₪")) {
			ArrayList<items> arr2 = order.getItemsInOrder();
			arr2.removeIf(item -> item.getItemName().equals("Discount 10% for pre-order"));
			order.setItemsInOrder(arr2);

		}

		// Reinitialize the page to reflect the changes
		initialize();
	}

	
	/**
	 * Handles the back button action, returning to the menu page.
	 * 
	 * @param event The ActionEvent triggered by the back button
	 * @throws Exception if an error occurs during the operation
	 */
	/**
	 * Handles the back button action, returning to the menu page.
	 * 
	 * @param event The ActionEvent triggered by the back button
	 * @throws Exception if an error occurs during the operation
	 */
	@FXML
	public void Back(ActionEvent event) throws Exception {
		items itemDiscount = new items();
		ArrayList<items> orderItems = order.getItemsInOrder();
		for(items item : orderItems) {
			if(item.getItemName().equals("Discount 10% for pre-order")) {
				itemDiscount = item;
				break;
			}
		}
		if(itemDiscount.getItemName() != null){
			orderItems.remove(itemDiscount);
			order.setItemsInOrder(orderItems);
		}
		
		
		Object controller = new MenuPageController(order, res, user, delivery);
		helper.newGui("Menu Page", "/gui/MenuPage.fxml", controller);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	
	/**
	 * Handles the payment process for the order.
	 * 
	 * @param event The ActionEvent triggered by the pay button
	 * @throws Exception if an error occurs during the operation
	 */
	@FXML
	public void pay(ActionEvent event) throws Exception {
		Helper.errorMsg = null;
		order.setStatus(OrderStatus.Pending);

		// Apply refund if available
		if (Helper.Refund != null) {
			order.setTotal_price(finalPriceWithReductions);
		}

		// Check if the cart has only refund, delivery, or discount items
		ObservableList<items> itemsInCart = lstview.getItems();
		boolean hasOnlyRefundDeliveryOrDiscount = true;
		for (items item : itemsInCart) {
			String itemName = item.getItemName();
			if (!itemName.equals("Refund") && !itemName.equals("Delivery")
					&& !itemName.equals("Discount 10% for pre-order")) {
				hasOnlyRefundDeliveryOrDiscount = false;
				break;
			}
		}

		// Prevent payment if cart only has special items
		if (hasOnlyRefundDeliveryOrDiscount) {
			error.setVisible(true);
			error.setText("Cannot proceed with payment. Cart does not contains food items.");
			return;
		}

		// Process refund
		if (Helper.Refund != null) {
			Helper.Refund -= finalPriceWithReductions;
			if (Helper.Refund < 0) {
				Helper.Refund = 0.0;
			}
			ArrayList<String> sendtoserver = new ArrayList<String>();
			sendtoserver.add(user.getUserName());
			sendtoserver.add(String.valueOf(Helper.Refund));
			CommMessage msg2 = new CommMessage(CommandConstants.UpdateRefund, sendtoserver);
			ClientUI.RequestData(msg2);
		}

		// Create and send the order
		CommMessage msg = new CommMessage(CommandConstants.CreateOrder, new ArrayList<>());

		delivery.setType(order.getType());
		msg.setDataFromServer(order);
		msg.setObjectForServer(delivery);
		ClientUI.RequestData(msg);

		// Wait for order confirmation
		while (Helper.newOrder == null) {
			Thread.sleep(50);
			if (Helper.errorMsg != null) {
				error.setVisible(true);
				error.setText(Helper.errorMsg);
				break;
			}
		}

		// Proceed to summary page if order is successful
		if (Helper.newOrder != null) {
			Helper.errorMsg = null;
			Object controller = new SummaryPageController(order, user);
			helper.newGui("Summary Page", "/gui/summaryPage.fxml", controller);
			((Node) event.getSource()).getScene().getWindow().hide();
		}
	}
}