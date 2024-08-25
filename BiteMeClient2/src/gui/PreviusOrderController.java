package gui;

import java.util.ArrayList;
import EnumsAndConstants.CommandConstants;
import EnumsAndConstants.OrderStatus;
import EnumsAndConstants.UserType;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.util.Callback;
import logic.CommMessage;
import logic.Orders.Order;
import logic.Users.User;

public class PreviusOrderController {

	@FXML
	private ListView<Order> lstView;
	
	@FXML
	private Button Back;
	
	@FXML
	private Text error;

	private User user;
	private Helper helper=new Helper();
	private ObservableList<Order> observableList;
	
	public PreviusOrderController(User user) {
			this.user = user;
	}
	
    @FXML
    public void initialize() throws InterruptedException {
        // Create a message to request the list of restaurants
    	CommMessage cmsg,cmsg2;
    	if(user.getUserType().equals(UserType.Customer) || user.getUserType().equals(UserType.BusinessCustomer))
    	{
	        ArrayList<String> msg = new ArrayList<>();
	        msg.add(user.getUserName());
	        cmsg = new CommMessage(CommandConstants.GetFullOrdersForUser,msg);
            ClientUI.RequestData(cmsg);

    	}
    	else {
    		if(Helper.restaurant != null) {
                ArrayList<String> msg2 = new ArrayList<>();
                msg2.add(String.valueOf(Helper.restaurant.getRestaurantId()));
                cmsg2 = new CommMessage(CommandConstants.GetFullOrdersForRestaurant, msg2);
                ClientUI.RequestData(cmsg2);
    		}
    		else {
    			return;
    		}
            
    	}
		while (Helper.prevOrder == null) {
			Thread.sleep(50);
			if (Helper.errorMsg != null) {
				error.setVisible(true);
				error.setText(Helper.errorMsg);
				break;
			}
		}
		if(Helper.prevOrder != null) {
			ArrayList<Order> array = new ArrayList<Order>();
			for(Order ord: Helper.prevOrder) {
				if(ord.getStatus().equals(OrderStatus.Received)) {
					array.add(ord);
				}
			}
			// Convert ArrayList to ObservableList
			observableList = FXCollections.observableArrayList(array);
			
			// Set the items of the ListView
			lstView.setItems(observableList);
			lstView.setCellFactory(new Callback<ListView<Order>, ListCell<Order>>() {
				class itemCell extends ListCell<Order> {
					@Override
					protected void updateItem(Order order, boolean empty) {
						super.updateItem(order, empty);
						if (empty || order == null) {
							setText(null);
						} else {
							setText(order.toString());
						}
					}
				}
				
				@Override
				public ListCell<Order> call(ListView<Order> listView) {
					return new itemCell();
				}
				
			});
		}
    }
    
    
	@FXML
	public void back(ActionEvent event) throws Exception {
		Helper.prevOrder = null;
    	((Node)event.getSource()).getScene().getWindow().hide();
		helper.openUserGUI(user);
	}
}
