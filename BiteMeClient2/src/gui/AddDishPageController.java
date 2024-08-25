package gui;

import java.util.ArrayList;

import EnumsAndConstants.CommandConstants;
import EnumsAndConstants.Doneness;
import EnumsAndConstants.ProductSize;
import EnumsAndConstants.TypeOfProduct;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import logic.CommMessage;
import logic.items;
import logic.Users.Supplier;

public class AddDishPageController {
	
	@FXML
	private MenuButton type;
	
	@FXML
	private TextField name;
	
	@FXML
	private TextField price;
	
	@FXML
	private CheckBox size;
	
	@FXML
	private CheckBox doneness;
	
	@FXML
	private Button btnAddDish;
	
	@FXML
	private Button btnBack;
	
	@FXML
	private Text error;
	
	
	private Supplier user;
	private Helper helper;

	public AddDishPageController(Supplier user) {
		this.user = user;
		helper = new Helper();
	}
	
	@FXML
	public void handleBackBtn(ActionEvent event) throws Exception{
		Object controller = new UpdateMenuResPageController(user);
		helper.newGui("Update Menu", "/gui/UpdateMenuResPage.fxml", controller);
		((Node) event.getSource()).getScene().getWindow().hide();
	}
	
	@FXML
	public void initialize() {
		items item = new items();
		type.getItems().clear();
        type.getItems().addAll(
	            new MenuItem(TypeOfProduct.Drink.toString()),
	            new MenuItem(TypeOfProduct.MainCourse.toString()),
	            new MenuItem(TypeOfProduct.Side.toString()),
	            new MenuItem(TypeOfProduct.Soup.toString())
	        );
        type.getItems().forEach(menuItem -> menuItem.setOnAction(event -> {
            item.setType(TypeOfProduct.getEnum(menuItem.getText()));
            type.setText(menuItem.getText());
        }));
	}
	
	@FXML
	public void handleAddDishBTN(ActionEvent event) throws Exception{
		error.setVisible(false);
		StringBuilder sb = new StringBuilder();
		
		if(type.getText().equals("Select type of dish")) {
			sb.append("You have to pick type of dish!\n");
		}
		
		if(!name.getText().matches("^[A-Za-z]+( [A-Za-z]+)*$")) {
			sb.append("The name should contain only english letters!\n");
		}
		
		if(!price.getText().matches("^[1-9]\\d*(\\.\\d+)?$")) {
			sb.append("The price should be positive number !\n");
		}
		
		if(sb.length() > 0) {
			error.setVisible(true);
			error.setText(sb.toString());
			return;
		}
		else {
			items dish = new items();
			dish.setItemName(name.getText());
			dish.setPrice(Float.valueOf(price.getText()));
			dish.setType(TypeOfProduct.getEnum(type.getText()));
			if(size.isSelected()) {
				dish.setSize(ProductSize.getEnum("Large"));
			} else {
				dish.setSize(null);
			}
			if(doneness.isSelected()) {
				dish.setDoneness(Doneness.getEnum("medium"));
			} else {
				dish.setDoneness(null);
			}
			
			ArrayList<String> sendToServer = new ArrayList<>();
			sendToServer.add(user.getUserName());
			CommMessage msg = new CommMessage(CommandConstants.GetRestaurant, sendToServer);
			ClientUI.RequestData(msg);
			while (Helper.restaurant == null) {
				Thread.sleep(50);
				if (Helper.errorMsg != null) {
					break;
				}
			}
			if(Helper.restaurant != null) {
				dish.setMenuID(Helper.restaurant.getMenuId());
			}
			
			ArrayList<String> sendToServer2 = new ArrayList<>();
			sendToServer2.add(String.valueOf(dish.getMenuID()));
			CommMessage msg2 = new CommMessage(CommandConstants.GetMenuAndItems, sendToServer2);
			ClientUI.RequestData(msg2);
			
			while (Helper.menu == null) {
				Thread.sleep(50);
				if (Helper.errorMsg != null) {
					break;
				}
			}
			if(Helper.menu != null) {
				for(items i : Helper.menu.getItemsInMenu()) {
					if(i.getItemName().toLowerCase().equals(dish.getItemName().toLowerCase())) {
						error.setVisible(true);
						error.setText("The Item is already in the menu\n" + "Please select different name.");
						return;
					}
				}
			}
			
			System.out.println("Item added:\n" + dish.toString());
			
			CommMessage msg3 = new CommMessage(CommandConstants.AddItemToMenu, new ArrayList<>());
			msg3.setObjectForServer(dish);
			ClientUI.RequestData(msg3);

			if(Helper.errorMsg == null) {			
				Object controller = new UpdateMenuResPageController(user, "Item added successfully !");
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
