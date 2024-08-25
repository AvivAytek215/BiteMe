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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import logic.CommMessage;
import logic.items;
import logic.Users.Supplier;

public class UpdateMenuResPageController {

	@FXML
	private Button btnUpdateDish;

	@FXML
	private Button btnAddDish;

	@FXML
	private Button btnRemoveDish;

	@FXML
	private Button btnBack;

	@FXML
	private ListView<items> dishListView;
	
	@FXML
	private Text errorMsg;

	private ObservableList<items> observableListitems;

	// Helper instance to manage GUI navigation
	private Helper helper;
	private Supplier user;
	private String str;

	public UpdateMenuResPageController(Supplier user) {
		this.user = user;
		helper = new Helper();
	}
	
	public UpdateMenuResPageController(Supplier user, String str) {
		this.user = user;
		this.str = str;
		helper = new Helper();
	}

	@FXML
	public void initialize() throws InterruptedException {
		Helper.menu = null;
		if(str != null) {
			errorMsg.setVisible(true);
			errorMsg.setFill(Color.GREEN);
			errorMsg.setText(str);
			str = null;
		}
		if (Helper.restaurant != null) {
			ArrayList<String> sendToServer = new ArrayList<>();
			sendToServer.add(String.valueOf(Helper.restaurant.getMenuId()));
			CommMessage msg = new CommMessage(CommandConstants.GetMenuAndItems, sendToServer);
			ClientUI.RequestData(msg);
			while (Helper.menu == null) {
				Thread.sleep(50);
				if (Helper.errorMsg != null) {
					break;
				}
			}
			if (Helper.menu != null) {
				Helper.restaurant.setMenu(Helper.menu.getItemsInMenu());

				observableListitems = FXCollections.observableArrayList(Helper.restaurant.getMenu());
				dishListView.setItems(observableListitems);
				dishListView.setCellFactory(new Callback<ListView<items>, ListCell<items>>() {
					class pendingCell extends ListCell<items> {
						@Override
						protected void updateItem(items item, boolean empty) {
							super.updateItem(item, empty);
							if (empty || item == null) {
								setText(null);
							} else {
								setText(item.getItemID() + ", " + item.getItemName() + ", " + item.getPrice() + "₪");
							}
						}
					}

					@Override
					public ListCell<items> call(ListView<items> listView) {
						return new pendingCell();
					}

				});
			}
			else {
				return;
			}
		}
	}

	@FXML
	public void handleUpdateDish(ActionEvent event) throws Exception {
		Helper.errorMsg = null;
		items dish = dishListView.getSelectionModel().getSelectedItem();
		if (dish == null) {
			errorMsg.setVisible(true);
			errorMsg.setFill(Color.RED);
			errorMsg.setText("Please select dish !");
			return;
		}
		Object controller = new UpdateDishPageController(user, dish);
		helper.newGui("Update Dish", "/gui/UpdateDishPage.fxml", controller);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	@FXML
	public void handleAddDish(ActionEvent event) throws Exception {
		Helper.errorMsg = null;
		Object controller = new AddDishPageController(user);
		helper.newGui("Add Dish", "/gui/AddDishPage.fxml", controller);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	@FXML
	public void handleDeleteDish(ActionEvent event) throws Exception {
		items dish = dishListView.getSelectionModel().getSelectedItem();
		if (dish == null) {
			errorMsg.setVisible(true);
			errorMsg.setFill(Color.RED);
			errorMsg.setText("Please select dish !");
			return;
		}
		ArrayList<String> sendToServer = new ArrayList<>();
		sendToServer.add(String.valueOf(dish.getItemID()));
		CommMessage msg = new CommMessage(CommandConstants.RemoveItemFromMenu,sendToServer);
		ClientUI.RequestData(msg);
		errorMsg.setVisible(true);
		if(Helper.errorMsg == null) {
			errorMsg.setFill(Color.GREEN);
			errorMsg.setText("Dish removed succesfully !");
		}
		else {
			errorMsg.setText(Helper.errorMsg);
		}
		initialize();
	}
	
	@FXML
	public void handleBack(ActionEvent event) throws Exception {
		Object controller = new SupplierHomePageController(user);
		helper.newGui("Supplier Home Page", "/gui/SupplierHomePage.fxml", controller);
		((Node) event.getSource()).getScene().getWindow().hide();
	}
}
