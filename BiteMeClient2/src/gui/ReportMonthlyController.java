package gui;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;



public class ReportMonthlyController implements Initializable {

	public static class RestaurantInReport {
		private int id;
		private String rName;
		private int revenue;

		private int totalOrders;
		private int lateOrders;

		// id
		// name
		private Map<Integer, Integer> foodSold; // [food_ID : Sold_Units ]
		private int foodID;
		private String foodName;
		private int foodSales;

		// Table 3 consturctor for foods
		public RestaurantInReport(int foodID, String foodName, int foodSales, int rest) {
			this.id = rest;
			this.foodID = foodID;
			this.foodName = foodName;
			this.foodSales = foodSales;
		}

		public RestaurantInReport(int id, String rName, int revenue, int totalOrders, int lateOrders) {
			this.id = id;
			this.rName = rName;
			this.revenue = revenue;
			this.totalOrders = totalOrders;
			this.lateOrders = lateOrders;
		}

		/**
		 * @param id
		 * @param rName
		 * @param revenue
		 * @param totalOrders
		 * @param lateOrders
		 * @param foodSold
		 * @param itemsSold
		 */
		public RestaurantInReport(int id, String rName, int revenue, int totalOrders, int lateOrders,
				Map<Integer, Integer> foodSold) {
			this.id = id;
			this.rName = rName;
			this.revenue = revenue;
			this.totalOrders = totalOrders;
			this.lateOrders = lateOrders;
			this.foodSold = foodSold;
		}

		public int getId() {
			return id;
		}

		public String getRName() {
			return rName;
		}

		public int getRevenue() {
			return revenue;
		}

		public int getTotalOrders() {
			return totalOrders;
		}

		public int getLateOrders() {
			return lateOrders;
		}

		/**
		 * @return the foodSold
		 */
		public Map<Integer, Integer> getFoodSold() {
			return foodSold;
		}

		/**
		 * @return the foodID
		 */
		public int getFoodID() {
			return foodID;
		}

		/**
		 * @return the foodName
		 */
		public String getFoodName() {
			return foodName;
		}

		/**
		 * @return the foodSales
		 */
		public int getFoodSales() {
			return foodSales;
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

}
