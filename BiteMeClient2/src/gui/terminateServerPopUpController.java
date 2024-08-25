package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class terminateServerPopUpController {

    @FXML
    private Label terminationMessage;

    @FXML
    public void initialize() {
        // You can set any initial values or perform actions here when the popup loads
        terminationMessage.setText("Server is Terminating, Application is Closing...");
    }

    // Any other methods you need for handling logic can be added here
}