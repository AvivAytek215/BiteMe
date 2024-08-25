
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
import EnumsAndConstants.CommandConstants;
import EnumsAndConstants.ReportType;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import logic.CommMessage;
import logic.Reports.MonthlyReport;
import logic.Reports.QuarterReport;
import logic.Users.User;

/**
 * Controller class for the BranchManager Home Page. This class handles the
 * interaction logic for the BranchManagerHomePage.fxml file.
 */
public class CEOHomePageController implements Initializable {

	@FXML
	private TextField QuarterlyYearSelectionFxml;

	@FXML
	private ComboBox<String> QuarterlymonthFxml;

	@FXML
	private TextField YearSelectionFxml;

	@FXML
	private ComboBox<String> branchFxml;

	@FXML
	private ComboBox<String> compareBranchFxml;

	@FXML
	private ComboBox<String> compareQuarterFxml;

	@FXML
	private Button compareViewBtn;

	@FXML
	private TextField compareYearFxml;

	@FXML
	private Text errorFxml;

	@FXML
	private CheckBox isComparing;

	@FXML
	private Button logoutButton;

	@FXML
	private ComboBox<String> monthFxml;

	@FXML
	private ComboBox<String> qBranchFxml;

	@FXML
	private ComboBox<ReportType> rTypeBtn;

	@FXML
	private Button viewMonthlyBtn;

	@FXML
	private Button viewQuarterlyBtn;
	
	@FXML
	private ImageView image;

	// diffrenciate between switching from report -> CEO/Branchmanager
	public static Boolean CeoActive = false;

	// create helper so i can use its methods and
	private Helper helper = new Helper();
	// Monthly data
	private ReportType reportType;
	private String selectedMonth;
	private String selectedYear;
	private String selectedBranch;
	// Quarter report data
	HashMap<String, String[]> quaters = new HashMap<String, String[]>(); // this is a dictionary,{ Q# : [start month,
																			// end month] }
	private String quarterSelectedBranch;
	String qMonthStart;
	String qMonthEnd; // why is this not private
	String qYear;
	// Compare view data
	private String compareSelectedBranch;
	private String compareMonthStart; // does this have to be private too
	private String compareMonthEnd;
	private String compareYear;

	// store server report respnse, reports will pull from here
	public static MonthlyReport reportResponseBranchManager = null;
	public static QuarterReport quarterResponseReport = null;
	public static QuarterReport quarterResponseReportCompare = null;
	
	private User user;
	
	public CEOHomePageController(User user) {
		this.user = user;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		quaters.put("Q1", new String[] { "1", "3" });
		quaters.put("Q2", new String[] { "4", "6" });
		quaters.put("Q3", new String[] { "7", "9" });
		quaters.put("Q4", new String[] { "10", "12" });
		// set up selection buttons

		// ----------MONTHLY
		branchFxml.getItems().addAll("North", "Center", "South");
		monthFxml.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
		rTypeBtn.getItems().addAll(ReportType.Revenue, ReportType.Performance, ReportType.Sales);
		// ---------QUARTER
		qBranchFxml.getItems().addAll("North", "Center", "South");
		QuarterlymonthFxml.getItems().addAll("Q1", "Q2", "Q3", "Q4");

		// -----------COMPARE
		compareBranchFxml.getItems().addAll("North", "Center", "South");
		compareQuarterFxml.getItems().addAll("Q1", "Q2", "Q3", "Q4");

	}

	// -----------------------------MONTHLY--------------------------------

	@FXML
	private void branchSelection(ActionEvent event) throws Exception {
		errorFxml.setDisable(true);

		selectedBranch = branchFxml.getValue();

		monthFxml.setDisable(false);// enable month selection

	}

	@FXML
	private void selectedDate(ActionEvent event) throws Exception {
		errorFxml.setDisable(true);
		selectedMonth = monthFxml.getValue();

		rTypeBtn.setDisable(false);// enable report type selection

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
		selectedYear = YearSelectionFxml.getText(); // initial year, default 2024
		Boolean successfulRequest = true;
		MonthlyReport reportResponse = null;
		Helper.errorMsg = null;
		Helper.reportResponse = null;
		ArrayList<String> sendToServer = new ArrayList<String>();
		sendToServer.add(selectedBranch); // branch
		sendToServer.add(selectedMonth);
		sendToServer.add(selectedYear);
//		sendToServer.add(BranchLocation.North.toString()); // branch
//		sendToServer.add("8");
//		sendToServer.add("2024");

//		System.out.println("BranchManagerHomePageController.java handleViewMonthlyReportButtonAction(); crafted this paramerter object " + sendToServer.toString());

		String fxmlStringPath = "";
		String title = "";
		Object controller = null;

		switch (this.reportType) {
		case Revenue:

			System.out.println(
					"BranchManagerHomePageController.java handleViewMonthlyReportButtonAction() Switch Case Revenue. the helper is filled "
							+ Helper.reportResponse);

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
			CeoActive = true;
			reportResponseBranchManager = Helper.reportResponse;

			((Node) event.getSource()).getScene().getWindow().hide();

			helper.newGui(title, fxmlStringPath, controller);
//				CeoActive = false;

		}

	}

//	------------------------------QUARTER--------------------------------------

	@FXML
	private void quarterQranchSelection(ActionEvent event) throws Exception {
		errorFxml.setDisable(true);

		quarterSelectedBranch = qBranchFxml.getValue();

	}

	@FXML
	void QuaterSelect(ActionEvent event) {
		errorFxml.setDisable(true);
		qMonthStart = (quaters.get(QuarterlymonthFxml.getValue()))[0];
		qMonthEnd = (quaters.get(QuarterlymonthFxml.getValue()))[1];

		viewQuarterlyBtn.setDisable(false);// enable report selection

	}

	@FXML
	void viewQuarterly(ActionEvent event) {
		qYear = QuarterlyYearSelectionFxml.getText();
		QuarterReport quarterResponseReport = null;
		Boolean successfulRequest = true;
		Helper.errorMsg = null;
		ArrayList<String> sendToServer = new ArrayList<String>();
		sendToServer.add(quarterSelectedBranch); // branch
		sendToServer.add(qMonthStart); //
		sendToServer.add(qMonthEnd); //
		sendToServer.add(qYear); //
		// STUB
//		sendToServer.add(BranchLocation.North.toString()); // branch
//		sendToServer.add("7");
//		sendToServer.add("9");
//		sendToServer.add("2024");

		String fxmlStringPath = "ReportQuarterly.fxml";
		String title = "Quarterly report";
		Object controller = new ReportQuarterlyController(user);

		ClientUI.RequestData(new CommMessage(CommandConstants.GetQuarterReport, sendToServer));

		while (CEOHomePageController.quarterResponseReport == null) {
			try {
				Thread.sleep(50);
				System.out.println(
						"BranchManagerHomePage.java handleViewMonthlyReportButtonAction(); while loop waiting for response");
				if (Helper.errorMsg != null) { // error recieved
					errorFxml.setVisible(true); // display error "no data for requested report"
					QuarterlymonthFxml.setValue(null); // nullify inputs
					QuarterlyYearSelectionFxml.setText("");
					successfulRequest = false;
					viewQuarterlyBtn.setDisable(true);// disable view report
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

			CeoActive = true;

			((Node) event.getSource()).getScene().getWindow().hide();

			try {

				helper.newGui(title, fxmlStringPath, controller);

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("failed to open report monthly");
				e.printStackTrace();
			}
		}

	}

	// --------------------------------COMPARE----------------------------------

	@FXML
	private void handleEnableCompare(ActionEvent event) throws Exception {
		if (isComparing.isSelected()) {
			// turn on compare
			compareYearFxml.setVisible(true);
			compareBranchFxml.setVisible(true);
			compareQuarterFxml.setVisible(true);
			compareViewBtn.setVisible(true);
		} else {
			// turn off compare
			compareYearFxml.setVisible(false);
			compareBranchFxml.setVisible(false);
			compareQuarterFxml.setVisible(false);
			compareViewBtn.setVisible(false);
		}

	}

	@FXML
	private void compareBranchSelection(ActionEvent event) throws Exception {
		errorFxml.setDisable(true);

		compareSelectedBranch = compareBranchFxml.getValue();

	}

	@FXML
	void compareQuarterSelection(ActionEvent event) {
		errorFxml.setDisable(true);
		compareMonthStart = (quaters.get(compareQuarterFxml.getValue()))[0];
		compareMonthEnd = (quaters.get(compareQuarterFxml.getValue()))[1];

		compareViewBtn.setDisable(false);// enable report selection

	}

	@FXML
	void handleCompareView(ActionEvent event) {
		compareYear = compareYearFxml.getText();
		qYear = QuarterlyYearSelectionFxml.getText();

		CEOHomePageController.quarterResponseReport = null;
		CEOHomePageController.quarterResponseReportCompare = null;

		Boolean successfulRequest = true;
		Helper.errorMsg = null;
		ArrayList<String> sendToServer1 = new ArrayList<String>();
		sendToServer1.add(quarterSelectedBranch); // branch
		sendToServer1.add(qMonthStart); //
		sendToServer1.add(qMonthEnd); //
		sendToServer1.add(qYear); //
		ArrayList<String> sendToServer2 = new ArrayList<String>();
		sendToServer2.add(compareSelectedBranch); // branch
		sendToServer2.add(compareMonthStart); //
		sendToServer2.add(compareMonthEnd); //
		sendToServer2.add(compareYear); //

		String fxmlStringPath = "ReportCompare.fxml";
		String title = "Compare report";
		Object controller = new ReportCompareController(user);
		ClientUI.RequestData(new CommMessage(CommandConstants.GetQuarterReport, sendToServer1));

		while (CEOHomePageController.quarterResponseReport == null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				System.out.println("CeoHomePageController.java handleCompareView(); thread sleep threw exception");
				e.printStackTrace();
			}
			System.out.println("CeoHomePageController.java handleCompareView(); while loop waiting for response");
			if (Helper.errorMsg != null) { // error recieved
				errorFxml.setVisible(true); // display error "no data for requested report"
				compareQuarterFxml.setValue(null); // nullify inputs
				compareYearFxml.setText("");
				successfulRequest = false;
				compareViewBtn.setDisable(true);// disable view report
				System.out.println(
						"CeoHomePageController.java handleCompareView(); while loop FAIL ERROR MSG " + Helper.errorMsg);
				Helper.errorMsg = null;
			}
		}

		CEOHomePageController.quarterResponseReportCompare = CEOHomePageController.quarterResponseReport;
		CEOHomePageController.quarterResponseReport = null;

		ClientUI.RequestData(new CommMessage(CommandConstants.GetQuarterReport, sendToServer2));

		while (CEOHomePageController.quarterResponseReport == null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				System.out.println("CeoHomePageController.java handleCompareView(); thread sleep threw exception");
				e.printStackTrace();
			}
			System.out.println("CeoHomePageController.java handleCompareView(); while loop waiting for response");
			if (Helper.errorMsg != null) { // error recieved
				errorFxml.setVisible(true); // display error "no data for requested report"
				compareQuarterFxml.setValue(null); // nullify inputs
				compareYearFxml.setText("");
				successfulRequest = false;
				compareViewBtn.setDisable(true);// disable view report
				System.out.println(
						"CeoHomePageController.java handleCompareView(); while loop FAIL ERROR MSG " + Helper.errorMsg);
				Helper.errorMsg = null;
			}

		}

		if (successfulRequest) {
			System.out.println(CEOHomePageController.quarterResponseReport.toString());
			System.out.println(CEOHomePageController.quarterResponseReportCompare.toString());
			CeoActive = true;

			((Node) event.getSource()).getScene().getWindow().hide();

			try {

				helper.newGui(title, fxmlStringPath, controller);

			} catch (Exception e) {

				System.out.println("failed to open Compare");
				e.printStackTrace();
			}
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
