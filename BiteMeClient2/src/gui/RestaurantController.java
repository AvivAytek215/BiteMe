package gui;

import java.util.ArrayList;
import EnumsAndConstants.CommandConstants;
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
import logic.Restaurant;
import logic.Orders.Delivery;
import logic.Orders.Order;
import logic.Users.User;

/**
 * Controller class for the Restaurant view.
 */
public class RestaurantController {
	
	@FXML
	private Button btnBack;

	@FXML
	private Button btnDone;

	@FXML
	private ListView<Restaurant> lstview;
	
	@FXML
	private Text error;
	
	private Order order;
	private User user;
	private Restaurant res;
	private Delivery delivery;
	private ObservableList<Restaurant> observableList;
	private Helper helper = new Helper();
	
	
	public RestaurantController(Order order, User user, Delivery delivery) {
		this.order = order;
		this.user = user;
		this.delivery = delivery;
	}

	
	@FXML
	public void initialize() throws InterruptedException {
		// Create a message to request the list of restaurants
		Helper.menu = null;
		ArrayList<String> msg = new ArrayList<>();
		msg.add(user.getMainBranch().toString());
		CommMessage cmsg = new CommMessage(CommandConstants.GetRestaurants, msg);
		ClientUI.RequestData(cmsg);
        while(Helper.res == null) {
        	Thread.sleep(50);
        	if(Helper.errorMsg != null) {
        		error.setVisible(true);
        		error.setText(Helper.errorMsg);
        		break;
        	}
        }
		if (Helper.res != null) {
			// Convert ArrayList to ObservableList
			observableList = FXCollections.observableArrayList(Helper.res);

			// Set the items of the ListView
			lstview.setItems(observableList);
	        lstview.setCellFactory(new Callback<ListView<Restaurant>, ListCell<Restaurant>>() {
	        	class RestaurantCell extends ListCell<Restaurant> {
	        	    @Override
	        	    protected void updateItem(Restaurant restaurant, boolean empty) {
	        	        super.updateItem(restaurant, empty);
	        	        if (empty || restaurant == null) {
	        	            setText(null);
	        	        } else {
	        	            setText(restaurant.getRestaurantName() + ", " + restaurant.getRestaurantAddress() + ", " + restaurant.getRestaurantType());
	        	        }
	        	    }
	        	}
	            @Override
	            public ListCell<Restaurant> call(ListView<Restaurant> listView) {
	                return new RestaurantCell();
	            }
	        	
	        });
		}

	}
	
	
	@FXML
	public void handleBtnDone(ActionEvent event) throws Exception {
	    
		Restaurant selectedRestaurant = lstview.getSelectionModel().getSelectedItem();
	    if (selectedRestaurant == null) {
	        error.setVisible(true);
	        error.setText("Please choose a restaurant!");
	        return; 
	    }
	    
		for(Restaurant re : Helper.res) {
			if(re.getRestaurantName().equals(lstview.getSelectionModel().getSelectedItem().getRestaurantName()))
					{
						res = re;
						break;
					}
		}
	    if (Helper.res != null) {
	        // Navigate to the MenuPage
	    	order.setRestaurantId(String.valueOf(res.getRestaurantId()));
	        Object controller = new MenuPageController(order, res, user, delivery);
	        helper.newGui("MenuPage", "/gui/MenuPage.fxml",controller);
			((Node) event.getSource()).getScene().getWindow().hide();
	    } else {
	        error.setVisible(true);
	        error.setText("Please choose a restaurant !");
	    }

	}

	@FXML
	public void back(ActionEvent event) throws Exception {
		Helper.res = null;
		Object controller = new StartOrderController(user);
		helper.newGui("Start order", "/gui/StartOrder.fxml", controller);
		((Node) event.getSource()).getScene().getWindow().hide(); 
	}
}
