package gui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import logic.Users.User;


public class PersonalDataController {
	
	@FXML
	private Button btnBack = null;
		
	@FXML
	private TextField Name;
	
	@FXML
	private Text error;

	@FXML
	private TextField Phone;
	
	@FXML
	private TextField FamilyName;
	
	@FXML
	private TextField Email;
	
	@FXML
	private TextField Id;
	
	private User user;
	private Helper helper=new Helper();
	
	
	public PersonalDataController(User user) {
		this.user = user;
	}
	
	
	@FXML
	public void initialize() {
		Name.setText(user.getFirstName());
		Phone.setText(user.getPhoneNumber());
		FamilyName.setText(user.getLastName());
		Email.setText(user.getEmail());
		Id.setText(user.getId());
	}
	
	
	public void back(ActionEvent event) throws Exception{
		((Node) event.getSource()).getScene().getWindow().hide(); 
		helper.openUserGUI(user);
	}
	
	
}