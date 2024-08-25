package gui;

import java.util.ArrayList;
import EnumsAndConstants.CommandConstants;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.util.Callback;
import logic.CommMessage;
import logic.Restaurant;
import logic.items;
import logic.Orders.Delivery;
import logic.Orders.Order;
import logic.Users.User;

/**
 * Controller class for the Restaurant view.
 */
public class MenuPageController {

	@FXML
	private Button btnBack;

	@FXML
	private Button btnDone;
	
	@FXML
	private Text error;
	
	@FXML
	private Button btnfinish;

	@FXML
	private ListView<items> lstview;
	
	private Order order;
	private Restaurant res;
	private User user;
	private Delivery delivery;
	private items item;
	private Helper helper = new Helper();

	public MenuPageController(Order order, Restaurant res, User user, Delivery delivery) {
		this.order = order;
		this.res = res;
		this.user = user;
		this.delivery = delivery;
	}

	
	/**
	 * Initializes the controller class.
	 * 
	 * @throws InterruptedException
	 */
	@FXML
	public void initialize() throws InterruptedException {
		Helper.errorMsg = null;
		// Create a message to request the list of menus
		ArrayList<String> msg = new ArrayList<>();
		msg.add(String.valueOf(res.getMenuId()));
		CommMessage cmsg = new CommMessage(CommandConstants.GetMenuAndItems, msg);
		ClientUI.RequestData(cmsg);
		while (Helper.menu == null) {
			Thread.sleep(50);
			if (Helper.errorMsg != null) {
				error.setVisible(true);
				error.setText(Helper.errorMsg);
				break;
			}
		}
		if (Helper.menu != null) {
			ObservableList<items> observableList = FXCollections.observableArrayList(Helper.menu.getItemsInMenu());

			// Set the items of the ListView
			lstview.setItems(observableList);
			lstview.setCellFactory(new Callback<ListView<items>, ListCell<items>>() {
				class itemCell extends ListCell<items> {
					@Override
					protected void updateItem(items item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setText(null);
						} else {
							setText(item.getItemName() + ", " + item.getPrice() + "₪");
						}
					}
				}

				@Override
				public ListCell<items> call(ListView<items> listView) {
					return new itemCell();
				}

			});
		}

	}

	
	/**
	 * Handles the Done button click event.
	 *
	 * @param event The ActionEvent triggered by the button click.
	 * @throws Exception If an error occurs during the operation.
	 */
	@FXML
	public void handleBtnDone(ActionEvent event) throws Exception {
		if(lstview.getSelectionModel().getSelectedItem() == null) {
			error.setVisible(true);
			error.setText("You Should pick an item first !");
			return;
		}
		for (items re : Helper.menu.getItemsInMenu()) {
			if (re.getItemName().equals(lstview.getSelectionModel().getSelectedItem().getItemName())) {
				item = re;
			}
		}
		if (item != null) {
			// Navigate to the MenuPage
			Object controller = new DishPageController(item, order, res, user, delivery);
			helper.newGui("Dish Page", "/gui/DishPage.fxml", controller);
			((Node) event.getSource()).getScene().getWindow().hide();
		} else {
			error.setVisible(true);
			error.setText("Please choose an item");
		}

	}

	
	/**
	 * Handles the Back button click event.
	 *
	 * @param event The ActionEvent triggered by the button click.
	 * @throws Exception If an error occurs during the operation.
	 */
	@FXML
	public void Back(ActionEvent event) throws Exception {
		// Open the Restaurant Page
		Helper.menu=null;
		order.setItemsInOrder(null);
		Object controller = new RestaurantController(order, user, delivery);
		helper.newGui("RestaurantPage", "/gui/RestaurantPage.fxml", controller);
		((Node) event.getSource()).getScene().getWindow().hide(); 
	}

	
	/**
	 * Handles the Finish button click event.
	 *
	 * @param event The ActionEvent triggered by the button click.
	 * @throws Exception If an error occurs during the operation.
	 */
	@FXML
	public void handleButtonFinish(ActionEvent event) throws Exception {
		// Navigate to the CartPage
		Object controller = new CartPageController(order, user, res, delivery);
		helper.newGui("Cart Page", "/gui/CartPage.fxml", controller);
		((Node) event.getSource()).getScene().getWindow().hide(); 
	}

}