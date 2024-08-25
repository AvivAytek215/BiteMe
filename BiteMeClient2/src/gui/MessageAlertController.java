package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class MessageAlertController {
	
	@FXML
	private Button OK;
	
	@FXML
	private Text message;
	
	private String str;
	
	public MessageAlertController(String str) {
		this.str = str;
	}
	
	@FXML
	public void initialize() {
		message.setText(str);
	}
	
	@FXML
	public void handleOKbutton(ActionEvent event) throws Exception{
		((Node) event.getSource()).getScene().getWindow().hide();
	}
}
