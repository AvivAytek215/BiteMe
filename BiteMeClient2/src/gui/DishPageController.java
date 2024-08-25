package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import logic.Restaurant;
import logic.items;
import logic.Orders.Delivery;
import logic.Orders.Order;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import EnumsAndConstants.Doneness;
import EnumsAndConstants.ProductSize;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import logic.Users.*;

public class DishPageController implements Initializable {

	@FXML
	private MenuButton idDishesAmount;
	
	@FXML
	private MenuButton donenessMenuBtn;
	
	@FXML
	private MenuButton sizeMenuBtn;
	
	@FXML
	private Text size;
	
	@FXML
	private Text doneness;
	
	@FXML
	private Text error;

	@FXML
	private TextField idFoodRequests;

	@FXML
	private TextField idPriceToAddup;

	@FXML
	private Spinner<Integer> idSpinner;

	private items item;
	private Order order;
	private User user;
	private Restaurant res;
	private Delivery delivery;
	private Helper helper = new Helper();

	
	public DishPageController(items item, Order order, Restaurant res, User user, Delivery delivery) {
		this.item = item;
		this.order = order;
		this.user = user;
		this.res = res;
		this.delivery = delivery;
	}

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		String price = String.format("%.2f",item.getPrice());
		idPriceToAddup.setText(price);

		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5);
		valueFactory.setValue(1);

		idSpinner.setValueFactory(valueFactory);

		idSpinner.valueProperty().addListener(new ChangeListener<Integer>() {

			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				idPriceToAddup.setText(String.format("%.2f", item.getPrice() * idSpinner.getValue()));

			}

		});
		if (item.getDoneness() != null) {
	        doneness.setVisible(true);
	        donenessMenuBtn.setVisible(true);
	        donenessMenuBtn.getItems().clear();
	        donenessMenuBtn.getItems().addAll(
	            new MenuItem("rare"),
	            new MenuItem("mediumRare"),
	            new MenuItem("medium"),
	            new MenuItem("mediumWell"),
	            new MenuItem("wellDone")
	        );
	        donenessMenuBtn.setText("Select Option");
	        donenessMenuBtn.getItems().forEach(menuItem -> menuItem.setOnAction(event -> {
	            item.setDoneness(Doneness.getEnum(menuItem.getText()));
	            donenessMenuBtn.setText(menuItem.getText());
	        }));
		} else {
			doneness.setVisible(false);
			donenessMenuBtn.setVisible(false);
		}
		if (item.getSize() != null) {
		       size.setVisible(true);
		        sizeMenuBtn.setVisible(true);
		        sizeMenuBtn.getItems().clear();
		        sizeMenuBtn.getItems().addAll(
		            new MenuItem("Small"),
		            new MenuItem("Medium"),
		            new MenuItem("Large")
		        );
		        sizeMenuBtn.setText("Select Option");
		        sizeMenuBtn.getItems().forEach(menuItem -> menuItem.setOnAction(event -> {
		            item.setSize(ProductSize.getEnum(menuItem.getText()));
		            sizeMenuBtn.setText(menuItem.getText());
		        }));
		}
		else {
			size.setVisible(false);
			sizeMenuBtn.setVisible(false);
		}
	}

	
	@FXML
	public void ContinueShopping(ActionEvent event) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		if(donenessMenuBtn.isVisible()) {
			if(donenessMenuBtn.getText().equals("Select Option")) {
				sb.append("You should pick Doneness!\n");
			}
		}
		if(sizeMenuBtn.isVisible()) {
			if(sizeMenuBtn.getText().equals("Select Option")) {
				sb.append("You should pick Size!\n");
			}
		}
		if(sb.length() > 0) {
			error.setVisible(true);
			error.setText(sb.toString());
			return; 
		}else {
			error.setVisible(false);
			item.setRestrictions(idFoodRequests.getText());
			order.setTotal_price(order.getTotal_price() + Double.valueOf(idPriceToAddup.getText()));
			order.setNumOfItems(order.getNumOfItems()+idSpinner.getValue());
			ArrayList<items> newitem = new ArrayList<items>();
			for (int i = 0; i < idSpinner.getValue(); i++) {
				newitem.add(item);
			}
			if(order.getItemsInOrder() == null)
			{
				order.setItemsInOrder(newitem);
			}
			else {
			newitem.addAll(order.getItemsInOrder());
			order.setItemsInOrder(newitem);
			}
			closeFoodSelection(event);
		}
	}

	
	@FXML
	public void closeFoodSelection(ActionEvent event) throws Exception {

		// go back to menuPage.fmxl
		Object controller = new MenuPageController(order, res, user, delivery);
		helper.newGui("Menu Page", "/gui/MenuPage.fxml", controller);
		((Node) event.getSource()).getScene().getWindow().hide(); 
	}

}
