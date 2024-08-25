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

public class ReportCompareController implements Initializable {
	
	private User user;
	
	public ReportCompareController(User user) {
		this.user = user;
	}

	@FXML
	private void backToManager(ActionEvent event) throws Exception {
		try {
			String fxmlStringPath;
			String title;
			Object controller;
			((Node) event.getSource()).getScene().getWindow().hide();

			fxmlStringPath = "/gui/CEOHomePage.fxml";
			title = "CEO Manager home page";
			controller = new CEOHomePageController(user);
			CEOHomePageController.CeoActive = false;

			Helper helper = new Helper();
			helper.newGui(title, fxmlStringPath, controller);
		} catch (Exception e) {
			System.out.println("ReportCompareCOntroller.java backToManager(); back to Ceo");
			e.printStackTrace();

		}

	}

	@FXML
	private Button backBtn;

	@FXML
	private Text reportTitle;

	@FXML
	private BarChart<String, Number> revenueChart1;
	@FXML
	private BarChart<String, Number> ordersChart1;

	@FXML
	private BarChart<String, Number> revenueChart2;
	@FXML
	private BarChart<String, Number> ordersChart2;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// System.out.println("ReportRevenueController.java initialize() my report is "
		// + BranchManagerHomePageController.reportResponseBranchManager);

		// [date : { orders : money } ]
		TreeMap<String, OrderSummary> ordersAndProfitPerDay = CEOHomePageController.quarterResponseReport.getOrdersAndProfitPerDay();
		
		


		XYChart.Series<String, Number> moneyChart = new XYChart.Series<>();
		moneyChart.setName("Revenue");
		for (String day : ordersAndProfitPerDay.keySet()) {
			// add restraunt name and the revenue
			moneyChart.getData().add(new XYChart.Data<>(day, ordersAndProfitPerDay.get(day).getTotalProfit()));

		}

		revenueChart1.getData().add(moneyChart);

		for (Node n : revenueChart1.lookupAll(".default-color0.chart-bar")) {
			n.setStyle("-fx-bar-fill: red;");
		}

		XYChart.Series<String, Number> ordersXY = new XYChart.Series<>();
		ordersXY.setName("Orders");
		for (String day : ordersAndProfitPerDay.keySet()) {
			// add restraunt name and the revenue
			ordersXY.getData().add(new XYChart.Data<>(day, ordersAndProfitPerDay.get(day).getOrderCount()));

		}

		ordersChart1.getData().add(ordersXY);
		for (Node n : ordersChart1.lookupAll(".default-color0.chart-bar")) {
			n.setStyle("-fx-bar-fill: blue;");
		}

		
		
		// ----------------------------------graph2
		
		
		TreeMap<String, OrderSummary> ordersAndProfitPerDay2 = CEOHomePageController.quarterResponseReportCompare.getOrdersAndProfitPerDay();
		
		
		XYChart.Series<String, Number> moneyChart2 = new XYChart.Series<>();
		moneyChart2.setName("Revenue");
		for (String day : ordersAndProfitPerDay2.keySet()) {
			// add restraunt name and the revenue
			moneyChart2.getData().add(new XYChart.Data<>(day, ordersAndProfitPerDay2.get(day).getTotalProfit()));

		}

		revenueChart2.getData().add(moneyChart2);

		for (Node n : revenueChart2.lookupAll(".default-color0.chart-bar")) {
			n.setStyle("-fx-bar-fill: red;");
		}

		XYChart.Series<String, Number> ordersXY2 = new XYChart.Series<>();
		ordersXY2.setName("Orders");
		for (String day : ordersAndProfitPerDay2.keySet()) {
			// add restraunt name and the revenue
			ordersXY2.getData().add(new XYChart.Data<>(day, ordersAndProfitPerDay2.get(day).getOrderCount()));

		}

		ordersChart2.getData().add(ordersXY2);
		for (Node n : ordersChart2.lookupAll(".default-color0.chart-bar")) {
			n.setStyle("-fx-bar-fill: blue;");
		}


	}

}
