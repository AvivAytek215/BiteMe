
/*
 * ChatClient.java 
 * 
 * add switch case reposne from server
 * to store the monthlyReport in helper class
 * 
 * Helper class has monthlyReport field that i use for this method
 * 
 * change to pie
 * 
 */

package gui;

import java.util.ArrayList;

import EnumsAndConstants.CommandConstants;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import logic.CommMessage;
import logic.Reports.MonthlyReport;
import logic.Users.User;

public class ReportPerformanceController {

	@FXML
	private Button backBtn;

	@FXML
	private Text reportTitle;

	@FXML
	private BarChart<String, Number> barChart;

	private User user;
	
	
	public ReportPerformanceController(User user) {
		this.user = user;
	}

	@FXML
	public void initialize() {
		Helper helper = new Helper();

		int lateOrders = Helper.reportResponse.getLateOrders();
		int totalOrders = Helper.reportResponse.getTotalOrders();

		XYChart.Series<String, Number> Onetime = new XYChart.Series<>();
		Onetime.getData().add(new XYChart.Data<>("On Time", totalOrders));

		XYChart.Series<String, Number> Misses = new XYChart.Series<>();

		Misses.getData().add(new XYChart.Data<>("Late", lateOrders));

		barChart.getData().addAll(Onetime, Misses);

	}

	@FXML
	private void backToManager(ActionEvent event) throws Exception {
		try {
			String fxmlStringPath;
			String title;
			Object controller;
			((Node) event.getSource()).getScene().getWindow().hide();
			if (CEOHomePageController.CeoActive) {
				fxmlStringPath = "/gui/CEOHomePage.fxml";
				title = "CEO Manager home page";
				controller = new CEOHomePageController(user);
				CEOHomePageController.CeoActive = false;


			} else {
				fxmlStringPath = "/gui/BranchManagerHomePage.fxml";
				title = "Branch Manager home page";
				controller = new BranchManagerHomePageController(user);

			}

			Helper helper = new Helper();
			helper.newGui(title, fxmlStringPath, controller);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ReportRevenueCOntroller,java backToManager(); back to manager");
			e.printStackTrace();

		}

	}

}
