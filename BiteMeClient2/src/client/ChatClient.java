package client;

import java.io.IOException;
import java.util.ArrayList;
import EnumsAndConstants.CommandConstants;
import common.ChatIF;
import gui.BranchManagerHomePageController;
import gui.CEOHomePageController;
import gui.Helper;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.CommMessage;
import logic.Restaurant;
import logic.menu;
import logic.Orders.Order;
import logic.Reports.MonthlyReport;
import logic.Reports.QuarterReport;
import logic.Users.User;
import ocsf.client.AbstractClient;


public class ChatClient extends AbstractClient {
    // Instance variables **********************************************

    /**
     * The interface for handling client UI interactions.
     */
    ChatIF clientUI;

    /**
     * A flag to indicate if the client is waiting for a server response.
     */
    public static boolean awaitResponse = false;


    // Constructors ****************************************************

    /**
     * Constructs an instance of the {@code ChatClient} class.
     * 
     * @param host          The server host.
     * @param port          The server port.
     * @param clientUI      The interface for handling client UI interactions.
     * @throws IOException If an I/O error occurs when creating the client.
     */
    public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
        super(host, port);
        this.clientUI = clientUI;
    }

    // Instance methods ************************************************

    /**
     * Handles messages received from the server. The method processes different
     * server commands and updates the client's state accordingly.
     * 
     * @param message The message received from the server.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void handleMessageFromServer(Object message) {
        CommMessage msg = (CommMessage) message;
        switch (msg.getCommandForServer()) {

        case Login:
            if (msg.isSucceeded()) {
                Helper.login = (User) msg.getDataFromServer();
            } else {
                Helper.errorMsg = msg.getMsg();
            }
            break;

        case LogOut:
            if (msg.isSucceeded()) {
                Helper.login = null;
                Helper.errorMsg = null;
            } else {
                Helper.errorMsg = msg.getMsg();
            }
            break;

        case GetRestaurants:
            if (msg.isSucceeded()) {
                Helper.res = (ArrayList<Restaurant>) msg.getDataFromServer();
                Helper.errorMsg = null;
            } else {
                Helper.errorMsg = msg.getMsg();
            }
            break;

        case GetMenuAndItems:
            if (msg.isSucceeded()) {
                Helper.menu = (menu) msg.getDataFromServer();
                Helper.errorMsg = null;
            } else {
                Helper.errorMsg = msg.getMsg();
            }
            break;

        case GetOrders:
            if (msg.isSucceeded()) {
                Helper.prevOrder = (ArrayList<Order>) msg.getDataFromServer();
                Helper.errorMsg = null;
            } else {
                Helper.errorMsg = msg.getMsg();
            }
            break;

        case GetRefund:
            if (msg.isSucceeded()) {
                Helper.Refund = (Double) msg.getDataFromServer();
                Helper.errorMsg = null;
            } else {
                Helper.errorMsg = msg.getMsg();
            }
            break;

        case UpdateRefund:
            if (msg.isSucceeded()) {
                Helper.errorMsg = null;
            } else {
                Helper.errorMsg = msg.getMsg();
            }
            break;

        case GetOrdersForRestaurant:
            if (msg.isSucceeded()) {
                Helper.prevOrder = (ArrayList<Order>) msg.getDataFromServer();
                Helper.errorMsg = null;
            } else {
                Helper.errorMsg = msg.getMsg();
            }
            break;

        case UpdateOrder:
            if (msg.isSucceeded()) {
                System.out.println(msg.dataFromServer.toString());
                Helper.order = (Order) msg.getDataFromServer();
                Helper.errorMsg = null;
            } else {
                System.out.println("didn't work updateorder" + msg.getMsg());
            }
            break;

        case CreateOrder:
            if (msg.isSucceeded()) {
                Helper.errorMsg = null;
                Helper.newOrder = (Order) msg.getDataFromServer();
            } else {
                Helper.errorMsg = msg.getMsg();
            }
            break;

        case GetRestaurant:
            if (msg.isSucceeded()) {
                Helper.restaurant = (Restaurant) msg.getDataFromServer();
                Helper.errorMsg = null;
            } else {
                Helper.errorMsg = msg.getMsg();
            }
            break;
         
        case RemoveItemFromMenu:
            if (msg.isSucceeded()) {
                Helper.errorMsg = null;
            } else {
                Helper.errorMsg = msg.getMsg();
            }
            break;
        
        case UpdateItemSpecifications:
            if (msg.isSucceeded()) {
                Helper.errorMsg = null;
            } else {
                Helper.errorMsg = msg.getMsg();
            }
            break;
            
        case AddItemToMenu:
            if (msg.isSucceeded()) {
                Helper.errorMsg = null;
            } else {
                Helper.errorMsg = msg.getMsg();
            }
            break;
           
        case GetFullOrdersForRestaurant:
            if (msg.isSucceeded()) {
            	Helper.prevOrder = (ArrayList<Order>) msg.getDataFromServer();
                Helper.errorMsg = null;
            } else {
                Helper.errorMsg = msg.getMsg();
            }
            break;
          
		case GetUnApprovedUsers:
			if (msg.isSucceeded()) {
				BranchManagerHomePageController.userList = msg;
			} else {
				Helper.errorMsg = msg.getMsg();
			}
			break;
			
		case ApproveUser:
			if (msg.isSucceeded()) {
				BranchManagerHomePageController.UserAprovedResponse = msg;
				System.out.println("ChatClient.java handleMessageFromServer(); User approval response" + BranchManagerHomePageController.UserAprovedResponse);

			} else {
				Helper.errorMsg = msg.getMsg();
			}
			break;
		case GetMonthlyRevenueReport:
			if (msg.isSucceeded()) {
				Helper.reportResponse = (MonthlyReport)msg.getDataFromServer();
				System.out.println("CHatClient.java handleMessageFromServer(); monrthly report is ressponse" + Helper.reportResponse.toString());

			} else {
				Helper.errorMsg = msg.getMsg();
			}
			break;
			
		case GetMonthlyPerformenceReport:
			if (msg.isSucceeded()) {
				Helper.reportResponse = (MonthlyReport)msg.getDataFromServer();
				System.out.println("CHatClient.java handleMessageFromServer(); monrthly report is ressponse" + Helper.reportResponse.toString());

			} else {
				Helper.errorMsg = msg.getMsg();
			}
			break;
			
		case GetMonthlyOrdersReport:
			if (msg.isSucceeded()) {
				Helper.reportResponse = (MonthlyReport)msg.getDataFromServer();
				System.out.println("CHatClient.java handleMessageFromServer(); monthlt report is ressponse" + Helper.reportResponse.toString());

			} else {
				Helper.errorMsg = msg.getMsg();
			}
			break;
			
		case GetQuarterReport:
			if (msg.isSucceeded()) {
				CEOHomePageController.quarterResponseReport = (QuarterReport)msg.getDataFromServer();
				System.out.println("CHatClient.java handleMessageFromServer(); quarter report is ressponse" + CEOHomePageController.quarterResponseReport.toString());

			} else {
				Helper.errorMsg = msg.getMsg();
			}
			break;
		
		case GetFullOrdersForUser:
            if (msg.isSucceeded()) {
                Helper.prevOrder = (ArrayList<Order>) msg.getDataFromServer();
                Helper.errorMsg = null;
            } else {
                Helper.errorMsg = msg.getMsg();
            }
            break;
			
		case TerminateServer:
			try {
		        // Ensure the following code runs on the JavaFX Application Thread
		        Platform.runLater(() -> {
		            try {
		                // Load the FXML file
		                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/TerminateServerPopUp.fxml"));
		                Parent root = loader.load();

		                // Create a new stage for the popup
		                Stage stage = new Stage();
		                stage.setTitle("Server Terminated");
		                stage.setScene(new Scene(root));

		                // Show the popup
		                stage.show();

		                // Pause for 3 seconds
		                PauseTransition delay = new PauseTransition(Duration.seconds(3));
		                delay.setOnFinished(event -> {
		                    // Clean up and exit the application after 3 seconds
		                    Platform.exit();
		                    System.exit(0);  // This ensures the JVM exits
		                });
		                delay.play();

		            } catch (Exception e) {
		                System.out.println("Server Terminated: failed to open");
		                e.printStackTrace();
		            }
		        });
		    } catch (Exception e) {
		        System.out.println("Server Terminated: failed to execute termination process");
		        e.printStackTrace();
		    }
		    break;
		    
        default:
            break;
        }
    }

    /**
     * Sends a message from the client UI to the server.
     * The method handles different server commands and updates the client's state
     * accordingly.
     * 
     * @param msg The message to be sent to the server.
     */
    public void handleMessageFromClientUI(CommMessage msg) {
        try {
            awaitResponse = true;
            sendToServer(msg);
            if (msg.getCommandForServer() instanceof CommandConstants) {
                awaitResponse = false;
            }
            while (awaitResponse) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("handleMessageFromClientUI FAILED");
            e.printStackTrace();
            clientUI.display("Could not send message to server: Terminating client." + e);
            quit();
        }
    }

    /**
     * Closes the connection to the server and exits the client application.
     */
    public void quit() {
        try {
            CommMessage msg = new CommMessage();
            msg.commandForServer = CommandConstants.Disconnect;
        	sendToServer(msg);
        	closeConnection();
        } catch (IOException e) {
        }
        System.exit(0);
    }
}
