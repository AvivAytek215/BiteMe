package gui;

import java.util.ArrayList;

import EnumsAndConstants.CommandConstants;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.CommMessage;
import logic.Users.Supplier;

/**
 * Controller class for the Supplier Home Page GUI. Handles user interactions
 * and navigates to other pages or displays information.
 */
public class SupplierHomePageController {

	@FXML
	private Button btnUpdateMenu;
	@FXML
	private Button btnActiveOrder;
	@FXML
	private Button btnPreviusOrders;
	@FXML
	private Button btnContactUs;
	@FXML
	private Button btnLogout;
	@FXML
	private ImageView personalData;
	@FXML
	private Text contact;

	private Supplier user;
	private Helper helper = new Helper();

	/**
	 * Constructor for SupplierHomePageController.
	 * 
	 * @param user The user currently logged in.
	 */
	public SupplierHomePageController(Supplier user) {
		this.user = user;
	}

	/**
	 * Handles the "Update Menu" button click event. Navigates to the Update Menu
	 * page.
	 * 
	 * @param event The ActionEvent triggered by the button click.
	 * @throws Exception If an error occurs during the event handling.
	 */
	@FXML
	public void handleBTNupdateMenu(ActionEvent event) throws Exception {
		Object controller = new UpdateMenuResPageController(user);
		helper.newGui("Update Menu", "/gui/UpdateMenuResPage.fxml", controller);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	/**
	 * Handles the "Active Order" button click event. Navigates to the Show Active
	 * Orders page.
	 * 
	 * @param event The ActionEvent triggered by the button click.
	 * @throws Exception If an error occurs during the event handling.
	 */
	@FXML
	public void handleBTNActiveOrder(ActionEvent event) throws Exception {
		Object controller = new ShowOrderInRestaurantController(user);
		helper.newGui("Show Active Orders", "/gui/ShowOrderInRestaurant.fxml", controller);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	/**
	 * Handles the "Previous Order" button click event. Navigates to the Show
	 * Previous Orders page.
	 * 
	 * @param event The ActionEvent triggered by the button click.
	 * @throws Exception If an error occurs during the event handling.
	 */
	@FXML
	public void handleBTNPreviousOrder(ActionEvent event) throws Exception {
		Object controller = new PreviusOrderController(user);
		helper.newGui("Show Previous Orders", "/gui/PreviousOrderPage.fxml", controller);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	/**
	 * Handles the "Contact Us" button click event. Displays a contact message to
	 * the user.
	 * 
	 * @param event The ActionEvent triggered by the button click.
	 * @throws Exception If an error occurs during the event handling.
	 */
	@FXML
	public void Contact(ActionEvent event) throws Exception {
		contact.setVisible(true);
		contact.setText("Thank you for your call we will come back in email");
	}

	/**
	 * Handles the "Log Out" button click event. Logs the user out and navigates to
	 * the login page.
	 * 
	 * @param event The ActionEvent triggered by the button click.
	 * @throws Exception If an error occurs during the event handling.
	 */
	@FXML
	public void LogOut(ActionEvent event) throws Exception {
		ArrayList<String> sendtoserver = new ArrayList<String>();
		sendtoserver.add(user.getUserName());
		sendtoserver.add(user.getPassword());
		CommMessage msg = new CommMessage(CommandConstants.LogOut, sendtoserver);
		ClientUI.chat.accept(msg);
		if (Helper.errorMsg == null) {
			Object controller = new LoginPageController();
			helper.newGui("login", "/gui/LoginPage.fxml", controller);
			((Node) event.getSource()).getScene().getWindow().hide();
		} else {
			System.out.println("error in login convert" + Helper.errorMsg);
		}
	}

	/**
	 * Handles the personal data view click event. Navigates to the Personal Data
	 * page.
	 * 
	 * @param event The MouseEvent triggered by the image view click.
	 * @throws Exception If an error occurs during the event handling.
	 */
	@FXML
	public void personalDataView(MouseEvent event) throws Exception {
		Stage stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage1.hide();
		Object controller = new PersonalDataController(user);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/PersonalDataPage.fxml"));
		loader.setController(controller);
		Parent root = loader.load();
		Stage stage = new Stage();
		stage.setTitle("Personal Data");
		stage.setScene(new Scene(root));
		stage.show();
	}
}
