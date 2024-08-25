package ServerGui;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import EnumsAndConstants.Doneness;
import EnumsAndConstants.ProductSize;
import EnumsAndConstants.TypeOfProduct;
import Server.ServerUI;
import Server.serverDB;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.CommMessage;
import logic.items;
import logic.Users.ClientUser;

/**
 * The ServerPortFrameController class is the controller for the server's GUI.
 * It manages the interaction between the GUI and the server, including starting
 * and stopping the server, connecting to the database, and displaying connected users.
 */
public class ServerPortFrameController {
    @FXML
    private Button btnExit;
    
    @FXML
    private Button btnDone;

    @FXML
    private TextField portxt;

    @FXML
    private TextArea connectedUsersTextArea;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField databaseField;

    @FXML
    private Label ipLabel;
    
    @FXML
    private Label hostLabel;
    
  
    
    @FXML
    private Label connectionStatusLabel;
    
    @FXML
    private Button importUserBtn;
    
    @FXML
    private TextArea errorTextArea; // New TextArea for error messages

    private boolean isConnected = false;
    
    private ServerUI serverUI;

    /**
     * Retrieves the port number entered by the user.
     *
     * @return The port number as a string.
     */
    private String getPort() {
        return portxt.getText();
    }
    
    /**
     * Sets the ServerUI instance for this controller.
     *
     * @param serverUI The ServerUI instance.
     */
    public void setServerUI(ServerUI serverUI) {
    	this.serverUI = serverUI;
    }
    
    /**
     * Initializes and displays the server port frame.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception if an error occurs during initialization.
     */
    public void start(Stage primaryStage) throws Exception {
    	Parent root = FXMLLoader.load(getClass().getResource("/ServerGui/ServerPort.fxml"));
    	Scene scene = new Scene(root);  // Set initial window size to 1200x1000
    	//scene.getStylesheets().add(getClass().getResource("/ServerGui/ServerPort.css").toExternalForm());
    	//primaryStage.initStyle(StageStyle.UNDECORATED); //added by shlomi
    	primaryStage.setTitle("Server Port");
    	primaryStage.setScene(scene);
    	
    	// Add a listener to handle the window close request
    	primaryStage.setOnCloseRequest(event -> {
    		event.consume(); // Consume the event to prevent the default behavior
    		getExitBtn(null); // Call the getExitBtn method
    	});
    	
    	primaryStage.show();
    }
    
    /**
     * Initializes the controller after its root element has been completely processed.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            try {
				String ipAddress = inetAddress.getHostAddress();
				ipLabel.setText("IP: " + ipAddress);
				hostLabel.setText("Host: " + inetAddress.getHostName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } catch (UnknownHostException e) {
            ipLabel.setText("IP: Unable to determine");
            hostLabel.setText("Host: Unable to determine");
        }
     // Initially disable the import button
        importUserBtn.setDisable(true);
    }

    /**
     * Handles the action when the "Done" button is clicked.
     * It connects to the database and starts the server, or disconnects the server if already connected.
     *
     * @param event The event triggered by the button click.
     * @throws Exception if an error occurs during the process.
     */
    @FXML
    public void Done(ActionEvent event) throws Exception {
        if (!isConnected) {
            String port = getPort();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String databaseName = databaseField.getText();

            if (!checkCredentials(port, username, password, databaseName)) {
            	return;
            }
            if (connectToDatabase(username, password, databaseName)) {
                ServerUI.runServer(port, this);
                btnDone.setText("Disconnect");
                isConnected = true;
//                Platform.runLater(()->{
//                	importUserBtn.setDisable(false);
//                });
            } else {
                showError("Failed to connect to the database. Please check your credentials and try again.");
            }
        } else {
            disconnect();
        }
    }
    
    /**
     * Handles the action when the "Import User" button is clicked.
     * It imports users from the imported users table to the main users table.
     *
     * @param event The event triggered by the button click.
     * @throws Exception if an error occurs during the import process.
     */
    public void importUserBtnMethod(ActionEvent event) throws Exception{
    	CommMessage msg = new CommMessage();
        msg = serverDB.importUsersFromImportedUsers(msg);

        // Show the result in the errorTextArea
        if (msg.isSucceeded()) {
            showError("Success: " + msg.getMsg());
        } else {
            showError("Error: " + msg.getMsg());
        }
    }
    
    /**
     * Validates the input credentials.
     *
     * @param port         The port number.
     * @param username     The username for the database.
     * @param password     The password for the database.
     * @param databaseName The database name.
     * @return true if all credentials are valid, false otherwise.
     */
    private boolean checkCredentials(String port, String username, String password,String databaseName) {
    	if (port.trim().isEmpty() && username.trim().isEmpty() && password.trim().isEmpty() && databaseName.trim().isEmpty()) {
            showError("All fields must be filled.");
            return false;
        }
        
        if (port.trim().isEmpty()) {
            showError("Port field must be filled.");
            return false;
        }

        if (username.trim().isEmpty()) {
            showError("Username field must be filled.");
            return false;
        }

        if (password.trim().isEmpty()) {
            showError("Password field must be filled.");
            return false;
        }

        if (databaseName.trim().isEmpty()) {
            showError("Database Name field must be filled.");
            return false;
        }
        return true;
    }

    /**
     * Connects to the database using the provided credentials.
     *
     * @param username     The username for the database.
     * @param password     The password for the database.
     * @param databaseName The name of the database.
     * @return true if the connection is successful, false otherwise.
     */
    private boolean connectToDatabase(String username, String password, String databaseName) {
    	boolean isConnected2 = ServerUI.runDB(username, password, databaseName);
        Platform.runLater(() -> {
            if (isConnected2) {
                connectionStatusLabel.setText("Status: Connected");
                connectionStatusLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                importUserBtn.setDisable(false); // Enable the import button
            } else {
                connectionStatusLabel.setText("Status: Not Connected");
                connectionStatusLabel.setTextFill(javafx.scene.paint.Color.RED);
                importUserBtn.setDisable(true); // Disable the import button
            }
        });
        return isConnected2;
    }

    /**
     * Disconnects the server and updates the UI.
     */
    private void disconnect() {
        //serverDB.closeDBconnection();
    	/*
    	if (!ServerUI.getInstance().loggedInIndicator()) {
        	showError("Cannot terminate Server , There is still Users connected");
        	return;
        }*/
        ServerUI.stopServer();
        btnDone.setText("Connect");
        connectionStatusLabel.setText("Status: Not Connected");
        connectionStatusLabel.setTextFill(javafx.scene.paint.Color.RED);
        isConnected = false;
        importUserBtn.setDisable(true); // Disable the import button
//        Platform.runLater(()->{
//        	importUserBtn.setDisable(true);
//        });
    }

    /**
     * Handles the action when the "Exit" button is clicked.
     * It terminates the server and exits the application.
     *
     * @param event The event triggered by the button click.
     */
    @FXML
    public void getExitBtn(ActionEvent event) {
        try {
        	if (ServerUI.getInstance().getServer() == null) {
        		throw new NullPointerException();
        	}
        	//if (!ServerUI.getInstance().loggedInIndicator()) {
        	//	showError("Cannot terminate Server , There is still Users connected");
        	//	return;
        	//}
        	ServerUI.getInstance().getServer().terminateServer();
        	boolean log = ServerUI.getInstance().logoutAll();
        	if (!log) {
        		showError("Failed to LogOut all users");
        		Thread.sleep(2000);
        	}
        	ServerUI.stopServer();
        }catch (Exception e) {
//        	System.err.println("Error loading FXML or CSS files.");
//            e.printStackTrace();
        }
    	System.out.println("Exiting application...");
        // Close DB connection and stop server
        //serverDB.closeDBconnection();
    	System.gc();
        System.exit(0);
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        //serverDB.closeDBconnection();
//        ServerUI.stopServer();
//        System.exit(0);
        this.getExitBtn(null);
    }
    
    /**
     * Updates the UI to display the list of connected users.
     *
     * @param observableList The list of connected users.
     */
    public void updateUI(ObservableList<ClientUser> observableList) {
        Platform.runLater(() -> {
        	connectedUsersTextArea.clear();
        	for (ClientUser c : observableList) {
        		connectedUsersTextArea.appendText(c.toString());
        	}
        });
    }

    /**
     * Updates the UI to display the list of connected users.
     *
     * @param observableList The list of connected users.
     */
    private void showError(String message) {
        Platform.runLater(() -> {
            errorTextArea.appendText(message + "\n");
        });
    }
}
