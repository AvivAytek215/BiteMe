package gui;

import java.util.ArrayList;

import EnumsAndConstants.CommandConstants;
import EnumsAndConstants.Doneness;
import EnumsAndConstants.ProductSize;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import logic.CommMessage;
import logic.items;
import logic.Users.Supplier;

public class UpdateDishPageController {
	
	@FXML
	private TextField type;
	
	@FXML
	private TextField name;
	
	@FXML
	private TextField price;
	
	@FXML
	private CheckBox size;
	
	@FXML
	private CheckBox doneness;
	
	@FXML
	private Button btnUpdateDish;
	
	@FXML
	private Button btnBack;
	
	@FXML
	private Text error;
	
	private Supplier user;
	private items dish;
	private Helper helper;

	public UpdateDishPageController(Supplier user, items dish) {
		this.user = user;
		this.dish = dish;
		helper = new Helper();
	}
	
	@FXML
	public void initialize() {
		type.setText(dish.getType().toString());
		name.setText(dish.getItemName());
		price.setText(String.valueOf(dish.getPrice()));
		if(dish.getDoneness() != null) {
			doneness.setSelected(true);
		}
		if(dish.getSize()!= null) {
			size.setSelected(true);
		}
		
	}
	
	@FXML
	public void handleBackBtn(ActionEvent event) throws Exception{
		Object controller = new UpdateMenuResPageController(user);
		helper.newGui("Update Menu", "/gui/UpdateMenuResPage.fxml", controller);
		((Node) event.getSource()).getScene().getWindow().hide();
	}
	
	@FXML
	public void handleUpdateDishBTN(ActionEvent event) throws Exception{
		if(!price.getText().matches("^\\d+(\\.\\d+)?$") || price.getText() == null) {
			error.setVisible(true);
			error.setText("The price should be positive number !");
			return;
		}
		
		dish.setPrice(Float.valueOf(price.getText()));
		
		if(size.isSelected()) {
			dish.setSize(ProductSize.Large);
		} else {
			dish.setSize(null);
		}
		
		if(doneness.isSelected()) {
			dish.setDoneness(Doneness.medium);
		} else {
			dish.setDoneness(null);
		}
		
		CommMessage msg = new CommMessage(CommandConstants.UpdateItemSpecifications,new ArrayList<>());
		msg.setObjectForServer(dish);
		ClientUI.RequestData(msg);
		
		if(Helper.errorMsg == null) {
			String message = "Dish updated Succesfully !";
			Object controller = new UpdateMenuResPageController(user, message);
			helper.newGui("Update Menu", "/gui/UpdateMenuResPage.fxml", controller);
			((Node) event.getSource()).getScene().getWindow().hide();	
		}
		else {
			error.setVisible(true);
			error.setText(Helper.errorMsg);
			return;
		}
		
		
	}
}
