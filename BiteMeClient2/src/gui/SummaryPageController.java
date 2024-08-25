package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import logic.Orders.Order;
import logic.Users.User;

public class SummaryPageController {
	
	@FXML 
	private TextArea orderSum;
	
	@FXML
	private Button btnHomePage;
	
	
	private Order order;
	private User user;
	private Helper helper = new Helper();
	
	public SummaryPageController(Order order, User user) {
		this.order = order;
		this.user = user;
	}
	
	@FXML
	public void handleBtnHomePage(ActionEvent event) throws Exception{
		helper.openUserGUI(user);
		((Node) event.getSource()).getScene().getWindow().hide();
	}
	
	
	@FXML
	public void initialize() {
		orderSum.setText(order.toString());
	}
	
}
