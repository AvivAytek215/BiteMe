package gui;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import logic.Orders.OrderSummary;
import logic.Users.User;

public class ReportQuarterlyController implements Initializable {
	
	private User user;
	
	public ReportQuarterlyController(User user) {
		this.user = user;
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
				controller = new CEOHomePageController(user);

			}

			Helper helper = new Helper();
			helper.newGui(title, fxmlStringPath, controller);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ReportRevenueCOntroller,java backToManager(); back to manager");
			e.printStackTrace();

		}

	}

	@FXML
	private Button backBtn;

	@FXML
	private Text reportTitle;

	@FXML
	private BarChart<String, Number> revenueChart;
	@FXML
	private BarChart<String, Number> ordersChart;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// System.out.println("ReportRevenueController.java initialize() my report is "
		// + BranchManagerHomePageController.reportResponseBranchManager);

		// [date : { orders : money } ]
		TreeMap<String, OrderSummary> ordersAndProfitPerDay = CEOHomePageController.quarterResponseReport
				.getOrdersAndProfitPerDay();

		XYChart.Series<String, Number> moneyChart = new XYChart.Series<>();
		moneyChart.setName("Revenue");
		for (String day : ordersAndProfitPerDay.keySet()) {
			// add restraunt name and the revenue
			moneyChart.getData().add(new XYChart.Data<>(day, ordersAndProfitPerDay.get(day).getTotalProfit()));

		}

		revenueChart.getData().add(moneyChart);

		for (Node n : revenueChart.lookupAll(".default-color0.chart-bar")) {
			n.setStyle("-fx-bar-fill: red;");
		}

		XYChart.Series<String, Number> ordersXY = new XYChart.Series<>();
		ordersXY.setName("Orders");
		for (String day : ordersAndProfitPerDay.keySet()) {
			// add restraunt name and the revenue
			ordersXY.getData().add(new XYChart.Data<>(day, ordersAndProfitPerDay.get(day).getOrderCount()));

		}

		ordersChart.getData().add(ordersXY);
		for (Node n : ordersChart.lookupAll(".default-color0.chart-bar")) {
			n.setStyle("-fx-bar-fill: blue;");
		}

	}

}
