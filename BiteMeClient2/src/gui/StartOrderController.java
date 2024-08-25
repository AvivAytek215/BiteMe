package gui;

import EnumsAndConstants.TypeOfOrder;
import EnumsAndConstants.UserType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import logic.Orders.Order;
import logic.Orders.Delivery;
import logic.Users.User;
import javafx.scene.Node;

public class StartOrderController {

	@FXML
	private Button deliveryBtn;

	@FXML
	private Button pickUpBtn;

	@FXML
	private Button backBtn;

	private User user;
	private Order order;
	private Delivery delivery;
	private Helper helper = new Helper();

	public StartOrderController(User user) {
		this.user = user;
		order = new Order(user.getUserName());
	}

	public StartOrderController(User user, Order order, Delivery delivery) {
		this.user = user;
		this.order = order;
		this.delivery = delivery;
	}

	@FXML
	public void back(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		helper.openUserGUI(user);
	}

	@FXML
	public void delivery(ActionEvent event) throws Exception {
		if (user.getUserType().equals(UserType.BusinessCustomer)) {
			Object controller = new DeliveryController(order, user, delivery);
			helper.newGui("Start Delivery", "/gui/DeliveryPage.fxml", controller);
			((Node) event.getSource()).getScene().getWindow().hide();
		} else {
			Object controller = new DeliveryController(order, user);
			helper.newGui("Start Delivery", "/gui/DeliveryPage.fxml", controller);
			((Node) event.getSource()).getScene().getWindow().hide();
		}
	}

	@FXML
	public void pickUp(ActionEvent event) throws Exception {
		order.setType(TypeOfOrder.PickUp);
		if (user.getUserType().equals(UserType.BusinessCustomer)) {
			Object controller = new PickUpAndFutureController(order, user,delivery);
			helper.newGui("PickUp Page", "/gui/PickUpPage.fxml", controller);
			((Node) event.getSource()).getScene().getWindow().hide();
		} else {
			Object controller = new PickUpAndFutureController(order, user);
			helper.newGui("PickUp Page", "/gui/PickUpPage.fxml", controller);
			((Node) event.getSource()).getScene().getWindow().hide();
		}
	}

}