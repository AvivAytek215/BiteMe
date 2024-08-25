package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import logic.Orders.Delivery;
import logic.Orders.Order;
import logic.Users.User;

/**
 * Controller class for starting a business order. This class handles the
 * interaction with the business order GUI.
 */
public class StartBusinessOrderController {

	@FXML
	private ComboBox<Integer> biterCountComboBox;

	@FXML
	private VBox emailFieldsContainer;

	@FXML
	private Text errormsg;

	@FXML
	private Button btnChooseRestaurant;

	@FXML
	private Button btnBack;

	private User user;
	private Order order; 
	private Helper helper = new Helper();
	private Delivery delivery;

	/**
	 * Constructor for the StartBusinessOrderController.
	 * 
	 * @param user The user object representing the logged-in user.
	 */
	public StartBusinessOrderController(User user) {
		this.user = user;
		this.order = new Order(user.getUserName()); 
	}

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the FXML file has been loaded.
	 */
	@FXML
	public void initialize() {
		// Initialize the ComboBox with options for number of biters
		for (int i = 1; i <= 5; i++) {
			biterCountComboBox.getItems().add(i);
		}

		// Initially hide all email fields
		for (int i = 0; i < 5; i++) {
			Text emailLabel = new Text("Email:");
			emailLabel.setVisible(false);
			TextField emailField = new TextField();
			emailField.setPromptText("Enter email for biter " + (i + 1));
			emailField.setVisible(false);
			emailFieldsContainer.getChildren().addAll(emailLabel, emailField);
		}
	}

	/**
	 * Handles the selection of number of biters. This method shows the email fields
	 * based on the selected number of biters.
	 * 
	 * @param event The ActionEvent triggered by the selection.
	 */
	@FXML
	public void chooseRes(ActionEvent event) {
		Integer biterCount = biterCountComboBox.getValue();
		if (biterCount == null) {
			return;
		}

		// Show the necessary number of email fields
		for (int i = 0; i < 5; i++) {
			if (i < biterCount) {
				emailFieldsContainer.getChildren().get(i * 2).setVisible(true);
				emailFieldsContainer.getChildren().get(i * 2 + 1).setVisible(true);
			} else {
				emailFieldsContainer.getChildren().get(i * 2).setVisible(false);
				emailFieldsContainer.getChildren().get(i * 2 + 1).setVisible(false);
			}
		}
		int deliveryfeeb = 25 - 5 * (biterCount - 1);
		if (deliveryfeeb < 15) {
			deliveryfeeb = 15;
		}
		delivery = new Delivery(order.getOrderId(), user.getFirstName(), user.getPhoneNumber(), biterCount, deliveryfeeb);
		
		order.setDeliveryID(delivery.getDeliveryId());

		errormsg.setVisible(false);
	}

	/**
	 * Handles the order submission. This method checks if all email fields are
	 * filled and shows an error if not.
	 * 
	 * @param event The ActionEvent triggered by the button click.
	 */
	@FXML
    public void btnChooseRestaurant(ActionEvent event) {
        // Check if a number of biters has been selected
        if (biterCountComboBox.getValue() == null) {
            errormsg.setText("Please select the number of biters.");
            errormsg.setVisible(true);
            return;
        }
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{1,40}$";
        Pattern pattern = Pattern.compile(emailRegex);

		ArrayList<String> emailOfParticipants = new ArrayList<>();
        List<Node> emailFields = emailFieldsContainer.getChildren();
        StringBuilder errorMessages = new StringBuilder();
        boolean hasEmptyField = false;

        for (int i = 1; i < emailFields.size(); i += 2) {
            TextField emailField = (TextField) emailFields.get(i);
            if (emailField.isVisible()) {
                String email = emailField.getText();
                if (email.isEmpty()) {
                    hasEmptyField = true;
                } else if (!pattern.matcher(email).matches()) {
                    errorMessages.append("The email in slot ")
                                 .append((i / 2) + 1)
                                 .append(" is invalid, ")
                                 .append("the pattern is: ___@___.___\n");
                } else {
                    if (emailOfParticipants.contains(email)) {
                        errorMessages.append("The email ").append(email).append(" is duplicated.\n");
                    } else {
                        emailOfParticipants.add(email);
                    }
                }
            }
        }

        if (hasEmptyField) {
            errormsg.setText("Please fill all email fields.");
            errormsg.setVisible(true);
            return;
        } else if (errorMessages.length() > 0) {
            errormsg.setText(errorMessages.toString().trim());
            errormsg.setVisible(true);
            return;
        }
        delivery.setUsernamesOfParticipants(emailOfParticipants);
		// Proceed with order submission logic
		Object controller = new StartOrderController(user, order, delivery);
		helper.newGui("restaurant", "/gui/StartOrder.fxml", controller);
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	@FXML
	public void back(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		helper.openUserGUI(user);
	}
}
