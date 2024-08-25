package gui;

import java.util.ArrayList;
import EnumsAndConstants.CommandConstants;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import logic.CommMessage;

public class LoginPageController{

    @FXML
    private Button btnExit = null;
    
    @FXML
    private Button btnLogin = null;
    
    @FXML
    private TextField userName;
    
    @FXML
    private PasswordField password;
    
    @FXML
    private Text error;
    
    private Helper helper = new Helper();
    
    
    @FXML
    public void sumbitCredentialsToServer(ActionEvent event) throws Exception {
        String userInput = userName.getText();
        String passInput = password.getText();
        if(userInput == "" || passInput == "") {
        	error.setVisible(true);
        	error.setText("One or more fields is empty, please fill all field !");
        	return;
        }
        ArrayList<String> sendToServer = new ArrayList<String>();
        sendToServer.add(userInput);
        sendToServer.add(passInput);
        ClientUI.RequestData(new CommMessage(CommandConstants.Login,sendToServer));
        while(Helper.login == null) {
        	Thread.sleep(50);
        	if(Helper.errorMsg != null) {
        		error.setVisible(true);
        		error.setText(Helper.errorMsg);
        		break;
        	}
        }
        if(Helper.login != null) {
        	((Node)event.getSource()).getScene().getWindow().hide();
        	helper.openUserGUI(Helper.login);
        }
        else {
        	return;
        }

    }

    @FXML
    public void initialize() {
    	Helper.login = null;
    	Helper.errorMsg = null;
    	Helper.res = null;
    	Helper.menu = null;
    	Helper.prevOrder =null;
    	Helper.order = null;
    	Helper.Refund = null;
    	Helper.newOrder = null;
    	Helper.restaurant = null;
    }
    
    public void exitApp(ActionEvent event) throws Exception {
    	ClientUI.chat.quit(); //added
    	System.exit(0);
    }
    
    @FXML
    public void exitApplication(ActionEvent event) {
        try {
			this.exitApp(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    

}
