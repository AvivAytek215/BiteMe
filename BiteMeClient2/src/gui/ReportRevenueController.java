package gui;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import logic.Users.User;

public class ReportRevenueController implements Initializable {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button backBtn;

	@FXML
	private Text reportTitle;

	@FXML
	private BarChart<String, Number> revenueChart;

	@FXML
	private CategoryAxis xAxis;

	@FXML
	private NumberAxis yAxis;

	private User user;
	

	public ReportRevenueController(User user) {
		this.user = user;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	
		Helper helper = new Helper();
		HashMap<String, Double> revenuePerRestaurant = Helper.reportResponse.getRevenuePerRestaurant();
		
		//System.out.println("ReportRevenueController.java initialize() my report is " + Helper.reportResponse.toString());
		XYChart.Series<String, Number> RevenuData = new XYChart.Series<>();
		for (String r : revenuePerRestaurant.keySet()) {
			//add restraunt name and the revenue
			RevenuData.getData().add(new XYChart.Data<>(r ,   revenuePerRestaurant.get(r)  ));
			
		}
		
		revenueChart.getData().add(RevenuData);

	}

	@FXML
	private void backToManager(ActionEvent event) throws Exception {
		CEOHomePageController ceo = new CEOHomePageController(user);
		try {
			Helper.reportResponse = null;
			String fxmlStringPath ;
			String title ;
			Object controller ;
			((Node) event.getSource()).getScene().getWindow().hide();
			if( CEOHomePageController.CeoActive) {
				 fxmlStringPath = "/gui/CEOHomePage.fxml";
				 title = "CEO Manager home page";
				 controller = new CEOHomePageController(user);
				 CEOHomePageController.CeoActive = false;


			}
			else {
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
