/*
 * had to change ChatClient.java
 * to handle cases of each server- reportMontlhy request-resopnse.
 * 
 * 
 * need to also add quaterly response hanlde in chatClient
 */

package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import EnumsAndConstants.BranchLocation;
import EnumsAndConstants.CommandConstants;
import EnumsAndConstants.ReportType;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import logic.CommMessage;
import logic.Reports.MonthlyReport;
import logic.Reports.QuarterReport;
import logic.Users.User;

/**
 * Controller class for the BranchManager Home Page. This class handles the
 * interaction logic for the BranchManagerHomePage.fxml file.
 */
public class BranchManagerHomePageController implements Initializable {

	@FXML
	private TextField FixedBranch;

	@FXML
	private Button RegisterCustomerButton;

	@FXML
	private TextField YearSelectionFxml;

	@FXML
	private Text errorFxml;

	@FXML
	private Button logoutButton;

	@FXML
	private ComboBox<String> monthFxml;

	@FXML
	private ComboBox<ReportType> rTypeBtn;

	@FXML
	private ImageView image;

    @FXML
    private ComboBox<String> usersApproveFxml;

	
	@FXML
	private Button viewMonthlyBtn;

	private Helper helper = new Helper();

	private ReportType reportType;


	public static CommMessage UserAprovedResponse = null;
	public static CommMessage userList = null;
	private User user;

	String selectedMonth;
	String selectedYear;
	
	public BranchManagerHomePageController(User user) {
		this.user = user;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		RegisterCustomerButton.setDisable(false);

		errorFxml.setVisible(false);

		FixedBranch.setText(Helper.login.getMainBranch().toString());
		monthFxml.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
		rTypeBtn.getItems().addAll(ReportType.Revenue, ReportType.Performance, ReportType.Sales);
		
		refreshUserList();
		
	}

	// --------------------GET REPORT

	@FXML
	private void selectedDate(ActionEvent event) throws Exception {
		errorFxml.setDisable(true);
		selectedMonth = monthFxml.getValue();

		rTypeBtn.setDisable(false);// enable report selection

	}

	@FXML
	private void selectRerportType(ActionEvent event) throws Exception {
		reportType = rTypeBtn.getValue();
		viewMonthlyBtn.setDisable(false);

	}

	/**
	 * Handles the View Monthly Report button action. This method is called when the
	 * view Monthly report button is pressed.
	 */
	@FXML
	private void handleViewMonthlyReportButtonAction(ActionEvent event) throws Exception {
		selectedYear = YearSelectionFxml.getText();
		Boolean successfulRequest = true;
		MonthlyReport reportResponse = null;
		Helper.errorMsg = null;
		Helper.reportResponse = null;
		ArrayList<String> sendToServer = new ArrayList<String>();
		sendToServer.add(Helper.login.getMainBranch().toString()); // branch
		sendToServer.add(selectedMonth);
		sendToServer.add(selectedYear);

		String fxmlStringPath = "";
		String title = "";
		Object controller = null;

		switch (this.reportType) {
		case Revenue:

			fxmlStringPath = "/gui/ReportRevenue.fxml";
			title = "View Revenue Report";
			controller = new ReportRevenueController(user);
			ClientUI.RequestData(new CommMessage(CommandConstants.GetMonthlyRevenueReport, sendToServer));
			System.out.println(
					"BranchManagerHomePageController.java handleViewMonthlyReportButtonAction() Break switch case");

			break;

		case Performance:

			fxmlStringPath = "/gui/ReportPerformance.fxml";
			title = "View Performance Report";
			controller = new ReportPerformanceController(user);
			ClientUI.RequestData(new CommMessage(CommandConstants.GetMonthlyPerformenceReport, sendToServer));

			break;

		case Sales:

			fxmlStringPath = "/gui/ReportSales.fxml";
			title = "View Sales Report";
			controller = new ReportSalesController(user);
			ClientUI.RequestData(new CommMessage(CommandConstants.GetMonthlyOrdersReport, sendToServer));

			break;
			
		default:
			break;

		}

		while (Helper.reportResponse == null) {
			try {
				Thread.sleep(50);
				System.out.println(
						"BranchManagerHomePage.java handleViewMonthlyReportButtonAction(); while loop waiting for response");
				if (Helper.errorMsg != null) { // error recieved
					errorFxml.setVisible(true); // display error "no data for requested report"
					monthFxml.setValue(null); // nullify inputs
					YearSelectionFxml.setText("");
					successfulRequest = false;
					rTypeBtn.setDisable(true);// disable report selection
					viewMonthlyBtn.setDisable(false); // disable view monthly report button
					System.out.println(
							"BranchManagerHomePage.java handleViewMonthlyReportButtonAction(); while loop FAIL ERROR MSG "
									+ Helper.errorMsg);
					Helper.errorMsg = null;

				}

			} catch (Exception e) { // compiler forced me to add try catch
				System.out.println(
						"BranchManagerHomePage.java handleViewMonthlyReportButtonAction()  while loop exception ");
				e.printStackTrace();
			}
		}

		if (successfulRequest) {
			helper.newGui(title, fxmlStringPath, controller);
			((Node) event.getSource()).getScene().getWindow().hide();

		}

	}

	// ------------------GET USER

	@FXML
	private void handleRegistration(ActionEvent event) throws Exception {
		errorFxml.setVisible(false);
		Boolean successfulRequest = true;
		UserAprovedResponse = null;
		userList = null;
		ArrayList<User> unapprovedUsers = new ArrayList<User>();
		Boolean userBeingApproved = false;

		String username = usersApproveFxml.getValue();
		if(username == null) {
		errorFxml.setText("Please select user first !");

		errorFxml.setVisible(true);
		errorFxml.setFill(Color.RED);
		return;
	}
		ArrayList<String> sendToServer = new ArrayList<String>();
		sendToServer.add(username);
		ClientUI.RequestData(new CommMessage(CommandConstants.ApproveUser, sendToServer));

		
		
		while (BranchManagerHomePageController.UserAprovedResponse == null) {
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				System.out.println("BranchManagerHomePage.java handleRegistration() tired of sleeping");
			}
			System.out.println("BranchManagerHomePage.java handleRegistration(); while loop waiting for response");
			if (Helper.errorMsg != null) { // error recieved
				System.out.println("BranchManagerHomePage.java handleRegistration(); while loop FAIL ERROR MSG "
						+ Helper.errorMsg);
				Helper.errorMsg = null;

			}

		}
		if (BranchManagerHomePageController.UserAprovedResponse.isSucceeded()) {

			errorFxml.setText(String.format("Success- User: %s has been updated", username));

			errorFxml.setVisible(true);
			errorFxml.setFill(new Color(0.0, 0.8842, 0.0589, 1.0));
			System.out.println("success");
			// success msg
		} else {
//			errorFxml.setFill(new Color(1.0, 0.0, 0.0, 1.0));
//			errorFxml.setText("ERROR: Couldn't update user status");
//			errorFxml.setVisible(true);
		}
		
		
		refreshUserList();

	}

	
	
	
	@SuppressWarnings("unchecked")
	private void refreshUserList() {
		usersApproveFxml.getItems().clear();

		ArrayList<User> unapprovedUsers = new ArrayList<User>();

		ArrayList<String> sendToServer1 = new ArrayList<String>();
		sendToServer1.add(Helper.login.getMainBranch().toString());
		ClientUI.RequestData(new CommMessage(CommandConstants.GetUnApprovedUsers, sendToServer1));

		while (BranchManagerHomePageController.userList == null) {
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				System.out.println("BranchManagerHomePage.java handleRegistration() tired of sleeping");
			}
			System.out.println("BranchManagerHomePage.java handleRegistration(); while loop waiting for response");
			if (Helper.errorMsg != null) { // error recieved
				System.out.println("BranchManagerHomePage.java handleRegistration(); while loop FAIL ERROR MSG "
						+ Helper.errorMsg);
				Helper.errorMsg = null;
		
				errorFxml.setFill(new Color(0.0, 0.0, 0.0, 1.0)); //black
				errorFxml.setText("Currently no unapproved users exist");
				errorFxml.setVisible(true);
				RegisterCustomerButton.setDisable(true);
				return;
			}
		}
		
		// success
		unapprovedUsers = (ArrayList<User>) BranchManagerHomePageController.userList.getDataFromServer();
		for (User person : unapprovedUsers) {
		
				usersApproveFxml.getItems().add(person.getUserName());
			

		}
		
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
	public void personalDataView(MouseEvent event) throws Exception {
		Object controller = new PersonalDataController(user);
		helper.newGui("Personal Data", "/gui/PersonalDataPage.fxml", controller);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

}
