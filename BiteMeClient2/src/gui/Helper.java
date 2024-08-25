package gui;

import java.io.IOException;
import java.util.ArrayList;

import EnumsAndConstants.CommandConstants;
import EnumsAndConstants.UserType;
import client.ClientUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.CommMessage;
import logic.Restaurant;
import logic.Users.Supplier;
import logic.Users.User;
import logic.menu;
import logic.Orders.Order;
import logic.Reports.MonthlyReport;

public class Helper {
			
	public static User login = null;
	public static String errorMsg = null;
	public static ArrayList<Restaurant> res = null;
	public static menu menu = null;
	public static ArrayList<Order> prevOrder;
	public static Order order = null;
	public static Double Refund = null;
	public static Order newOrder = null;
	public static Restaurant restaurant = null;
	
	// ----- Reports ------
	public static MonthlyReport reportResponse=null;
	public static String rMonth=null;
	public static String rYear=null;
	// --------------------
	
	public void openUserGUI(User user) {
		String fxmlStringPath = "";
		String title = "";
		Object controller = null;
			try {
				switch(user.getUserType()) {
					case Customer:
						fxmlStringPath = "/gui/CustomerHomePage.fxml";
						title = "Customer home page";
						controller = new CustomerHomeController(user);
						break;
					case BusinessCustomer:
						fxmlStringPath = "/gui/BusinessCustomerHomePage.fxml";
						title = "Business Customer home page";
						controller = new BusinessCustomerHomePageController(user);
						break;
					case BranchManager:
						fxmlStringPath = "/gui/BranchManagerHomePage.fxml";
						title = "Branch Manager home page";
						controller = new BranchManagerHomePageController(user);
						break;
					case CEO:
						fxmlStringPath = "/gui/CEOHomePage.fxml";
						title = "CEO home page";
						controller = new CEOHomePageController(user);
						break;
					case Supplier:
						ArrayList<String> arr = new ArrayList<String>();
						arr.add(user.getUserName());
						ClientUI.RequestData(new CommMessage(CommandConstants.GetRestaurant,arr));
						while (Helper.restaurant == null) {
							Thread.sleep(50);
							if (Helper.errorMsg != null) {
								break;
							}
						}
						if(Helper.restaurant != null) {
							fxmlStringPath = "/gui/SupplierHomePage.fxml";
							title = "Supplier home page";
							Supplier sup = new Supplier(user.getUserName(),user.getPassword(),user.getFirstName(),user.getLastName(),user.getEmail(),user.getPhoneNumber(),UserType.Supplier,user.getMainBranch(),user.getId(),user.getIsLoggedIn());
							sup.setRestauarantID(String.valueOf(Helper.restaurant.getRestaurantId()));
							controller = new SupplierHomePageController(sup);
//							Helper.restaurant = null;
						}
						else {
							System.out.println("Error in getting supplier ID!");
						}
						break;
					default:
						System.out.println("User type not found");
						break;
				}
			}
			catch (Exception e) {
				System.err.println("Helper.java openUserGUI(); error in switch case");
				e.printStackTrace();
			}
			newGui(title , fxmlStringPath, controller);	
	}
		
		
	public void newGui(String title, String fxmlStringPath, Object controller) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlStringPath));
			loader.setController(controller);
			Parent root = loader.load();
			Stage stage = new Stage();
		//	stage.initStyle(StageStyle.UNDECORATED); //added by shlomi
			stage.setTitle(title);
			stage.setScene(new Scene(root));
			stage.show();		
		} catch (IOException e) {
			System.out.println(title + ": failed to open");
			e.printStackTrace();
		}
	}
}
