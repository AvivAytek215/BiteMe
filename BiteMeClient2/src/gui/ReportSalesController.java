package gui;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import logic.Users.User;

public class ReportSalesController implements Initializable{

	@FXML
	private Button backBtn;

	@FXML
	private Text reportTitle;

	@FXML
	private BarChart<String, Number> salesChart;
	
	private User user;

	public ReportSalesController(User user) {
		this.user = user;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		System.out.println("ReportSalesController.java initialize() my report is " + Helper.reportResponse);

		
		
		 HashMap<String, HashMap<String, Integer>> ordersPerRestaurant = Helper.reportResponse.getOrdersPerRestaurant();
		
		XYChart.Series<String, Number> restaurant = null;

		for (String restaurantName : ordersPerRestaurant.keySet()) {
			restaurant = new XYChart.Series<>();
			restaurant.setName(restaurantName);

			//for each food in the menu
			for (String foodInMenu : ordersPerRestaurant.get(restaurantName).keySet()) {
				HashMap<String, Integer> restaurantMenu = ordersPerRestaurant.get(restaurantName);
				Integer foodSales = restaurantMenu.get(foodInMenu); // each restaunt has menu, food ID: to its sales in the
																// restaunt
				restaurant.getData().add(new XYChart.Data<>(foodInMenu, foodSales));
			}
			salesChart.getData().add(restaurant);

		}

	}

	@FXML
	private void backToManager(ActionEvent event) throws Exception {
		Helper.reportResponse=null;
		try {
			String fxmlStringPath;
			String title;
			Object controller;
			((Node) event.getSource()).getScene().getWindow().hide();
			if (CEOHomePageController.CeoActive) {
				fxmlStringPath = "/gui/CEOHomePage.fxml";
				title = "CEO Manager home page";
				controller = new CEOHomePageController(user);

			} else {
				fxmlStringPath = "/gui/BranchManagerHomePage.fxml";
				title = "Branch Manager home page";
				controller = new BranchManagerHomePageController(user);

			}
			CEOHomePageController.CeoActive = false;
			Helper helper = new Helper();
			helper.newGui(title, fxmlStringPath, controller);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ReportRevenueCOntroller,java backToManager(); back to manager");
			e.printStackTrace();

		}

	}




}
