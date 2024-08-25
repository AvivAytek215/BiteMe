package gui;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Controller class for the Connection Page GUI.
 * Handles the connection to the server and navigation to the Login Page.
 */
public class ConnectionPageController{

	@FXML
	private Button btnConnect = null;

	@FXML
	private TextField ipField;

	@FXML
	private TextField portField;

	@FXML
	private Button submitOrderId = null;

	@FXML
	private Text errorMsg;
	
	private ClientUI client = new ClientUI();
	private Helper helper = new Helper();
	
	
	 /**
     * Connects to the server using the IP address and port provided by the user.
     * If the connection is successful, navigates to the Login Page.
     * If the connection fails, displays an error message.
     *
     * @param event The action event triggered by the user's interaction with the GUI.
     * @throws Exception if an error occurs during the connection or navigation.
     */
	@FXML
	public void connectToServer(ActionEvent event) throws Exception {
		String ipDestination = ipField.getText();
		int port = Integer.parseInt(portField.getText());
		boolean succeed = client.newConnection(ipDestination, port);
		if (succeed) {
			LoginPageController loginCon = new LoginPageController();
			helper.newGui("Login Page", "/gui/LoginPage.fxml", loginCon);
			((Node) event.getSource()).getScene().getWindow().hide();
		} else {
			errorMsg.setVisible(true);
			errorMsg.setText("IP incorrect or server unreachable");
		}
	}

	
	@FXML
	public void initialize() {
	    try {
	    	InetAddress inetAddress = InetAddress.getLocalHost();
	        String ipAddress = inetAddress.getHostAddress();
	        ipField.setText(ipAddress);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
    /**
     * Starts the Connection Page GUI.
     *
     * @param primaryStage The primary stage for this application.
     */
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/gui/ConnectionPage.fxml"));
			Scene scene = new Scene(root);
			//primaryStage.initStyle(StageStyle.UNDECORATED); //added by shlomi
			primaryStage.setTitle("Connection Page");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			System.err.println("Error loading FXML or CSS files of the Connection Page");
			e.printStackTrace();
		}
	}

}