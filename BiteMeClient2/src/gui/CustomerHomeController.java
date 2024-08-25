package gui;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.TimeZone;

import EnumsAndConstants.CommandConstants;
import EnumsAndConstants.OrderStatus;
import EnumsAndConstants.TypeOfOrder;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import logic.CommMessage;
import logic.Orders.Order;
import logic.Users.User;

public class CustomerHomeController {

	@FXML
	private Button btnLogout;

	@FXML
	private Button btnStartOrder;

	@FXML
	private Button btnPreviousOrder;

	@FXML
	private Button btnContact;

	@FXML
	private ImageView personalData;

	@FXML
	private Text contact;
	@FXML
	private Text approved;
	@FXML
	private Text ready;
	@FXML
	private Text delivered;
	@FXML
	private Text orderStatus;
	@FXML
	private ProgressBar orderProg;
	@FXML
	private Button refresh;
    @FXML
    private Button recieveOrder;

	private User user;
	private boolean flag1 = true;
	private boolean flag2 = true;

	private Helper helper = new Helper();

	public CustomerHomeController(User user) {
		this.user = user;
	}

	@FXML
	public void initialize() throws InterruptedException {
		Helper.errorMsg = null;
		Helper.order = null;
		Helper.menu = null;
		Helper.res = null;
		Helper.Refund = null;
		boolean flag = true;

		ArrayList<String> sendto = new ArrayList<>();
		sendto.add(user.getUserName());
		CommMessage cmsg = new CommMessage(CommandConstants.GetOrders, sendto);
		ClientUI.RequestData(cmsg);
		while (Helper.prevOrder == null) {
			Thread.sleep(50);
			if (Helper.errorMsg != null) {
				break;
			}
		}
		if (Helper.prevOrder != null) {
			for (Order order : Helper.prevOrder) {
				if (!order.getStatus().equals(OrderStatus.getEnum("Recieved"))) {
					Helper.newOrder = order;
					flag = false;
				}
			}
		}
		Helper.prevOrder = null; 
		if(flag) {
			Helper.newOrder = null;
		}
		if (Helper.newOrder != null) {
			approved.setVisible(true);
			ready.setVisible(true);
			delivered.setVisible(true);
			orderStatus.setVisible(true);
			orderProg.setVisible(true);
			refresh.setVisible(true);

			if (Helper.newOrder.getStatus().equals(OrderStatus.Pending)) {
				orderProg.setProgress(0);
			}
			if (Helper.newOrder.getStatus().equals(OrderStatus.Approved)) {
				if (flag1) {
					Object controller = new MessageAlertController("Your order approved by the supplier");
					helper.newGui("Message Pop-Up", "/gui/MessageAlert.fxml", controller);
					flag1 = false;
				}
				orderProg.setProgress(0.22);
			}
			if (Helper.newOrder.getStatus().equals(OrderStatus.Ready)) {
				if (flag2) {
					Object controller = new MessageAlertController("Your order is warm and ready");
					helper.newGui("Message Pop-Up", "/gui/MessageAlert.fxml", controller);
					flag2 = false;
				}
				orderProg.setProgress(0.58);
			}
			if (Helper.newOrder.getStatus().equals(OrderStatus.Delivered)) {
				orderProg.setProgress(1);
				recieveOrder.setVisible(true);
			}
			if (Helper.newOrder.getStatus().equals(OrderStatus.Received)) {
				approved.setVisible(false);
				ready.setVisible(false);
				delivered.setVisible(false);
				orderStatus.setVisible(false);
				orderProg.setVisible(false);
				Helper.newOrder = null;
			}
		}
	}


	@FXML
	public void refreshPage(ActionEvent event) throws Exception {
		initialize();
	}

    @FXML
    void recieveBtn(ActionEvent event) {
		Helper.newOrder.setStatus(OrderStatus.Received);
		ArrayList<String> send = new ArrayList<String>();
		send.add(String.valueOf(Helper.newOrder.getOrderId()));
		java.sql.Date sqlDate = java.sql.Date.valueOf(LocalDate.now(ZoneId.of("Asia/Jerusalem")));
        java.sql.Time sqlTime = java.sql.Time.valueOf(LocalTime.now(ZoneId.of("Asia/Jerusalem")));
		Helper.newOrder.setRecievedByCustomerDate(sqlDate);
		// Convert java.sql.Time to java.time.LocalTime
		LocalTime localTime = sqlTime.toLocalTime();

		// Subtract 3 hours and 30 minutes
		LocalTime updatedTime = localTime.minusHours(3).minusMinutes(30);

		// Convert back to java.sql.Time
		Time updatedSqlTime = Time.valueOf(updatedTime);
		Helper.newOrder.setRecievedByCustomerTime(updatedSqlTime);
		long differenceInMillis = Helper.newOrder.getRecievedByCustomerTime().getTime()
				- Helper.newOrder.getAskedByCustomerTime().getTime();

		// Convert milliseconds to minutes
		long differenceInMinutes = differenceInMillis / (1000 * 60);
		if (Helper.newOrder.getType().equals(TypeOfOrder.PRE) && differenceInMinutes > 20) {
			Helper.newOrder.setIsLate(true);
		} else if (differenceInMinutes > 60) {
			Helper.newOrder.setIsLate(true);
		} else {
			Helper.newOrder.setIsLate(false);
		}
		if (Helper.newOrder.isIsLate()) {
			ArrayList<String> array = new ArrayList<>();
			array.add(user.getUserName());
			CommMessage msg1 = new CommMessage(CommandConstants.GetRefund, array);
			ClientUI.RequestData(msg1);
			while (Helper.Refund == null) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (Helper.errorMsg != null) {
					if(Helper.errorMsg.equals("No refunds for this user"))
					{
						Helper.Refund=0.0;
					}
					break;
				}
			}
			if (Helper.Refund != null) {
				ArrayList<String> sendtoserver = new ArrayList<String>();
				sendtoserver.add(user.getUserName());
				Helper.Refund += Helper.newOrder.getTotal_price() / 2;
				sendtoserver.add(String.valueOf(Helper.Refund));
				CommMessage msg2 = new CommMessage(CommandConstants.UpdateRefund, sendtoserver);
				ClientUI.RequestData(msg2);
			}
		}

		CommMessage msg = new CommMessage(CommandConstants.UpdateOrder, send);
		msg.setDataFromServer(Helper.newOrder);
		ClientUI.RequestData(msg);
		while (Helper.order == null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (Helper.errorMsg != null) {
				break;
			}
		}
		if (Helper.order != null) {
			Helper.order = null;
			Helper.newOrder = null;
			Helper.errorMsg = null;
			Helper.menu = null;
			Helper.prevOrder = null;
			Helper.res = null;
			Helper.Refund = null;
			approved.setVisible(false);
			ready.setVisible(false);
			delivered.setVisible(false);
			orderStatus.setVisible(false);
			orderProg.setVisible(false);
			recieveOrder.setVisible(false);
			try {
				initialize();
			} catch (InterruptedException e) {
				System.out.println("CustomerHomePage.java recieveBtn(); innitialize failed");
				e.printStackTrace();
			}
		}
		return; 
	}	

	@FXML
	public void contactUs(ActionEvent event) throws Exception {
		contact.setVisible(true);
		contact.setText("Thank you for your call we will come back via email");
	}

	@FXML
	public void logOut(ActionEvent event) throws Exception {
		ArrayList<String> sendtoserver = new ArrayList<String>();
		sendtoserver.add(user.getUserName());
		sendtoserver.add(user.getPassword());
		CommMessage msg = new CommMessage(CommandConstants.LogOut, sendtoserver);
		ClientUI.RequestData(msg);
		if (Helper.errorMsg == null) {
			Object controller = new LoginPageController();
			helper.newGui("login", "/gui/LoginPage.fxml", controller);
			((Node) event.getSource()).getScene().getWindow().hide();
		} else {
			System.out.println("error in login convert" + Helper.errorMsg);
		}
	}

	@FXML
	public void previousOrder(ActionEvent event) throws Exception {
		Object controller = new PreviusOrderController(user);
		helper.newGui("Previous Orders", "/gui/PreviousOrderPage.fxml", controller);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	@FXML
	public void startOrder(ActionEvent event) throws Exception {
		Object controller = new StartOrderController(user);
		helper.newGui("Start Order", "/gui/StartOrder.fxml", controller);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	@FXML
	public void personalDataView(MouseEvent event) throws Exception {
		Object controller = new PersonalDataController(user);
		helper.newGui("Personal Data", "/gui/PersonalDataPage.fxml", controller);
		((Node) event.getSource()).getScene().getWindow().hide();
	}
}
