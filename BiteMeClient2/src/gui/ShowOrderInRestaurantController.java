package gui;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import EnumsAndConstants.CommandConstants;
import EnumsAndConstants.OrderStatus;
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
import logic.Orders.Order;
import logic.Users.Supplier;

public class ShowOrderInRestaurantController {

	private Supplier user;
	private Helper helper = new Helper();

	public ShowOrderInRestaurantController(Supplier user) {
		this.user = user;
	}

	private ObservableList<Order> observableListpending;
	private ObservableList<Order> observableListaccepted;
	private ObservableList<Order> observableListready;
	@FXML
	private ListView<Order> approveLstView;
	@FXML
	private ListView<Order> readyLstView;
	@FXML
	private ListView<Order> deliveredLstView;
	@FXML
	private Button ApproveBtn;
	@FXML
	private Button ReadyBtn;
	@FXML
	private Button DeliverdBtn;
	@FXML
	private Button BackBtn;
	@FXML
	private Text error1;
	@FXML
	private Text error2;
	@FXML
	private Text error3;

	@FXML
	public void initialize() throws InterruptedException {
		// Create a message to request the list of restaurants
		CommMessage cmsg;
		ArrayList<String> sendToServer = new ArrayList<>();
		sendToServer.add(user.getRestauarantID());
		cmsg = new CommMessage(CommandConstants.GetFullOrdersForRestaurant, sendToServer);
		ClientUI.RequestData(cmsg);
		while (Helper.prevOrder == null) {
			Thread.sleep(50);
			if (Helper.errorMsg != null) {
				break;
			}
		}
		if (Helper.prevOrder != null) {
			// Convert ArrayList to ObservableList
			ArrayList<Order> pending = new ArrayList<Order>();
			ArrayList<Order> accepted = new ArrayList<Order>();
			ArrayList<Order> ready = new ArrayList<Order>();
			for (Order order : Helper.prevOrder) {
				if (order.getStatus().equals(OrderStatus.Pending)) {
					pending.add(order);
				} else if (order.getStatus().equals(OrderStatus.Approved)) {
					accepted.add(order);
				} else if (order.getStatus().equals(OrderStatus.Ready)) {
					ready.add(order);
				}

			}
			observableListpending = FXCollections.observableArrayList(pending);
			observableListaccepted = FXCollections.observableArrayList(accepted);
			observableListready = FXCollections.observableArrayList(ready);
			// Set the items of the ListView
			approveLstView.setItems(observableListpending);
			approveLstView.setCellFactory(new Callback<ListView<Order>, ListCell<Order>>() {
				class pendingCell extends ListCell<Order> {
					@Override
					protected void updateItem(Order order, boolean empty) {
						super.updateItem(order, empty);
						if (empty || order == null) {
							setText(null);
						} else {
							setText(order.toString()); 
						}
					}
				}

				@Override
				public ListCell<Order> call(ListView<Order> listView) {
					return new pendingCell();
				}

			});
			readyLstView.setItems(observableListaccepted);
			readyLstView.setCellFactory(new Callback<ListView<Order>, ListCell<Order>>() {
				class approvedCell extends ListCell<Order> {
					@Override
					protected void updateItem(Order order, boolean empty) {
						super.updateItem(order, empty);
						if (empty || order == null) {
							setText(null);
						} else {
							setText(order.toString());
						}
					}
				}

				@Override
				public ListCell<Order> call(ListView<Order> listView) {
					return new approvedCell();
				}

			});
			deliveredLstView.setItems(observableListready);
			deliveredLstView.setCellFactory(new Callback<ListView<Order>, ListCell<Order>>() {
				class readyCell extends ListCell<Order> {
					@Override
					protected void updateItem(Order order, boolean empty) {
						super.updateItem(order, empty);
						if (empty || order == null) {
							setText(null);
						} else {
							setText(order.toString());
						}
					}
				}

				@Override
				public ListCell<Order> call(ListView<Order> listView) {
					return new readyCell();
				}

			});
			Helper.prevOrder = null;
		}

	}

	@FXML
	public void back(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		helper.openUserGUI(user);
	}

	@FXML
	public void HandleApproveBtn(ActionEvent event) throws Exception {
		Order order = approveLstView.getSelectionModel().getSelectedItem();
		if(order == null) {
			error2.setVisible(false);
			error3.setVisible(false);
			error1.setVisible(true);
			error1.setText("Pick one order !");
			return;
		}
		error1.setVisible(false);
		order.setStatus(OrderStatus.Approved);
		
		java.sql.Date sqlDate = java.sql.Date.valueOf(LocalDate.now(ZoneId.of("Asia/Jerusalem")));
        java.sql.Time sqlTime = java.sql.Time.valueOf(LocalTime.now(ZoneId.of("Asia/Jerusalem")));
		order.setApprovedByResDate(sqlDate);
		// Convert java.sql.Time to java.time.LocalTime
		LocalTime localTime = sqlTime.toLocalTime();

		// Subtract 3 hours and 30 minutes
		LocalTime updatedTime = localTime.minusHours(3).minusMinutes(30);

		// Convert back to java.sql.Time
		Time updatedSqlTime = Time.valueOf(updatedTime);
		order.setApprovedByResTime(updatedSqlTime);
		ArrayList<String> sendtoserver = new ArrayList<String>();
		sendtoserver.add(String.valueOf(order.getOrderId()));
		CommMessage com = new CommMessage(CommandConstants.UpdateOrder, sendtoserver);
		com.setDataFromServer(order);
		ClientUI.RequestData(com);
		while (Helper.order == null) {
			Thread.sleep(50);
			if (Helper.errorMsg != null) {
				break;
			}
		}
		if (Helper.order != null) {
			Helper.order = null;
			initialize();
		}
	}

	@FXML
	public void HandleReadyBtn(ActionEvent event) throws Exception {

		Order order = readyLstView.getSelectionModel().getSelectedItem();
		if(order == null) {
			error1.setVisible(false);
			error3.setVisible(false);
			error2.setVisible(true);
			error2.setText("Pick one order !");
			return;
		}
		error2.setVisible(false);
		order.setStatus(OrderStatus.Ready);
		ArrayList<String> sendtoserver = new ArrayList<String>();
		sendtoserver.add(String.valueOf(order.getOrderId()));
		CommMessage com = new CommMessage(CommandConstants.UpdateOrder, sendtoserver);
		com.dataFromServer = order;
		ClientUI.chat.accept(com);
		while (Helper.order == null) {
			Thread.sleep(50);
			if (Helper.errorMsg != null) {
				break;
			}
		}
		if (Helper.order != null) {
			Helper.order = null;
			initialize();
		}
	}

	@FXML
	public void HandleDeliveredBtn(ActionEvent event) throws Exception {
		Order order = deliveredLstView.getSelectionModel().getSelectedItem();
		if(order == null) {
			error1.setVisible(false);
			error2.setVisible(false);
			error3.setVisible(true);
			error3.setText("Pick one order !");
			return;
		}
		error3.setVisible(false);
		order.setStatus(OrderStatus.Delivered);
		ArrayList<String> sendtoserver = new ArrayList<String>();
		sendtoserver.add(String.valueOf(order.getOrderId()));
		CommMessage com = new CommMessage(CommandConstants.UpdateOrder, sendtoserver);
		com.dataFromServer = order;
		ClientUI.chat.accept(com);
		while (Helper.order == null) {
			Thread.sleep(50);
			if (Helper.errorMsg != null) {
				break;
			}
		}
		if (Helper.order != null) {
			Helper.order = null;
			initialize();
		}
	}
}
