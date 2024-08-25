package Server;

import java.util.ArrayList;
import java.util.Iterator;
import EnumsAndConstants.BranchLocation;
import EnumsAndConstants.CommandConstants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logic.CommMessage;
import logic.items;
import logic.Users.ClientUser;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 * The EchoServer class is responsible for handling client-server communication.
 * It extends the AbstractServer class from the OCSF framework and manages
 * incoming client connections, processes client messages, and interacts with
 * the server database.
 */
public class EchoServer extends AbstractServer {
	/**
     * Default port number for the server.
     */
	final public static int DEFAULT_PORT = 5555;
    
	/**
     * Reference to the server database.
     */
	private serverDB database;
    
	/**
     * Controller for the server GUI.
     */
	private ServerGui.ServerPortFrameController controller;
    
	/**
     * Controller for the server GUI.
     */
	private ArrayList<ClientUser> clients = new ArrayList<>();

	/**
     * Constructs an EchoServer with the specified port and controller.
     *
     * @param port       The port number to listen on.
     * @param controller The server GUI controller.
     */
    public EchoServer(int port, ServerGui.ServerPortFrameController controller) {
        super(port);
        this.controller = controller;
        this.database = new serverDB();
    }
    
    /**
     * Handles incoming messages from clients.
     *
     * @param msg    The message received from the client.
     * @param client The connection to the client that sent the message.
     */
    @Override
    public void handleMessageFromClient(Object msg, ConnectionToClient client) {
    	if (msg instanceof CommMessage) {
    		
    		CommMessage commMsg = (CommMessage)msg;
    		switch (commMsg.getCommandForServer()) {
			case Login:
				commMsg = serverDB.Login(commMsg.messageForServer.get(0), commMsg.messageForServer.get(1), commMsg);
				this.sendToClient(commMsg,client);
				if (commMsg.isSucceeded == true) {
					updateClientUsernameAndConnection(client, commMsg.messageForServer.get(0));
					
				}
				break;
				
			case LogOut:
				commMsg = serverDB.Logout(commMsg.messageForServer.get(0), commMsg);
				this.sendToClient(commMsg,client);
				break;
			
			case PowerLogOut:
				commMsg = serverDB.Logout(commMsg.messageForServer.get(0), commMsg);
				break;
				
			case GetRestaurant:
				commMsg = serverDB.getRestaurant(commMsg.messageForServer.get(0), commMsg);
				this.sendToClient(commMsg, client);
				break;
				
			case GetRefund:
				commMsg = serverDB.GetRefund(commMsg.messageForServer.get(0), commMsg);
				this.sendToClient(commMsg, client);
				break;
			
			case UpdateRefund:
				commMsg = serverDB.UpdateRefund(commMsg.messageForServer.get(0),Double.parseDouble(commMsg.messageForServer.get(1)), commMsg);
				this.sendToClient(commMsg, client);
				break;
				
			case GetMenuAndItems:
				commMsg = serverDB.getMenuAndItemsByMenuID(Integer.parseInt( commMsg.messageForServer.get(0)) ,commMsg);
				this.sendToClient(commMsg,client);
				break;
		
			case GetRestaurants:
				EnumsAndConstants.BranchLocation branch = EnumsAndConstants.BranchLocation.getEnum(commMsg.messageForServer.get(0));
				commMsg = serverDB.getAllRestaurantsFrom(branch, commMsg);
				this.sendToClient(commMsg, client);
				break;
				
			case UpdateOrder:
				commMsg = serverDB.UpdateOrder(Integer.parseInt(commMsg.messageForServer.get(0)), commMsg);
				this.sendToClient(commMsg, client);
				break;
				
			case GetOrders:
				commMsg = serverDB.GetOrders(commMsg.messageForServer.get(0), commMsg);
				this.sendToClient(commMsg, client);
				break;
				
			case GetOrdersForRestaurant:
				commMsg = serverDB.GetOrdersForRestaurant(commMsg.messageForServer.get(0), commMsg);
				this.sendToClient(commMsg, client);
				break;
				
			case GetPersonalData:
				commMsg = serverDB.GetPersonalData(commMsg.messageForServer.get(0), commMsg);
				this.sendToClient(commMsg, client);
				break;
				
			case ApproveUser:
				commMsg = serverDB.approveUser(commMsg.messageForServer.get(0), commMsg);
				this.sendToClient(commMsg, client);
				break;
				
			case GetQuarterReport:
				commMsg = serverDB.getQuarterReport(BranchLocation.getEnum(commMsg.messageForServer.get(0))  ,Integer.parseInt(commMsg.messageForServer.get(1)) , Integer.parseInt(commMsg.messageForServer.get(2)), Integer.parseInt(commMsg.messageForServer.get(3)), commMsg);
				this.sendToClient(commMsg, client);
				break;
				
			case GetMonthlyPerformenceReport:
				commMsg = serverDB.getMonthlyPerformanceReport(BranchLocation.getEnum(commMsg.messageForServer.get(0)), Integer.parseInt(commMsg.messageForServer.get(1)), Integer.parseInt(commMsg.messageForServer.get(2)), commMsg);
				this.sendToClient(commMsg, client);
				break;
				
			case GetMonthlyRevenueReport:
				commMsg = serverDB.getMonthlyRevenueReport(BranchLocation.getEnum(commMsg.messageForServer.get(0)), Integer.parseInt(commMsg.messageForServer.get(1)), Integer.parseInt(commMsg.messageForServer.get(2)), commMsg);
				this.sendToClient(commMsg, client);
				break;
				
			case GetMonthlyOrdersReport:
				commMsg = serverDB.getMonthlyOrdersReport(BranchLocation.getEnum(commMsg.messageForServer.get(0)), Integer.parseInt(commMsg.messageForServer.get(1)), Integer.parseInt(commMsg.messageForServer.get(2)), commMsg);
				this.sendToClient(commMsg, client);
				break;
			
			case GetFullOrdersForRestaurant:
				commMsg = serverDB.GetFullOrdersForRestaurant(commMsg.messageForServer.get(0), commMsg);
				this.sendToClient(commMsg, client);
				break;
				
			case GetItemsInOrder:
				commMsg = serverDB.GetItemsInOrder(Integer.parseInt(commMsg.getMessageForServer().get(0)), commMsg);
				this.sendToClient(commMsg, client);
				break;
				
			case CreateOrder:
//				Order reqOrder = (Order)commMsg.getDataFromServer();
//				String reqRestaurant = reqOrder.getRestaurantId();
//				CommMessage com = new  CommMessage();
//				com = serverDB.getRestaurantSupplier(reqRestaurant, com);
//				if (!com.isSucceeded) {
//					commMsg.isSucceeded = false;
//					commMsg.msg = "cannot find restaurant supplierID";
//					this.sendToClient(commMsg, client);
//					break;
//				}
//				String supplierId = (String)com.getDataFromServer();
//				ConnectionToClient clientCon = isSupplierConnected(supplierId);
//				if (clientCon == null) {
//					commMsg.isSucceeded = false;
//					commMsg.msg = "Supplier And Restaurant are not connected to the System";
//					this.sendToClient(commMsg, client);
//					break;
//				}
				
				commMsg = serverDB.CreateOrder(commMsg);
				this.sendToClient(commMsg, client);  //need to add check if the supliier is connected 
//				if (commMsg.isSucceeded) {
//					this.sendToClient(commMsg, clientCon);
//				}
				break;
			
			case GetUnApprovedUsers:
                commMsg = serverDB.GetUnApprovedUsers(commMsg.messageForServer.get(0),commMsg);
                this.sendToClient(commMsg, client);
                break;
                //to test
            case RemoveItemFromMenu:
                commMsg = serverDB.RemoveItemFromMenu(Integer.parseInt(commMsg.messageForServer.get(0)), commMsg);
                this.sendToClient(commMsg, client);
                break;

            case AddItemToMenu:
                commMsg = serverDB.addItemToMenu((items)commMsg.objectForServer, commMsg);
                this.sendToClient(commMsg, client);
                break;

            case UpdateItemSpecifications:
                commMsg = serverDB.updateItemSpecifications((items)commMsg.objectForServer, commMsg);
                this.sendToClient(commMsg, client);
                break;
			
            case GetFullOrdersForUser:
				commMsg = serverDB.GetFullOrdersForUser(commMsg.messageForServer.get(0), commMsg);
				this.sendToClient(commMsg, client);
				break;
            	
            case Disconnect:
            	this.clientDisconnected(client);
            	break;
            	
			default:
				commMsg.isSucceeded = false;
				commMsg.msg = "UnRecognized Command";
				this.sendToClient(commMsg, client);
				break;
			}
    	}
    }
    
    /**
     * Sends a message to a specific client.
     *
     * @param msg    The message to send.
     * @param client The client connection to send the message to.
     */
	public void sendToClient(Object msg, ConnectionToClient client) {
		try {
			client.sendToClient(msg);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" Error sending msg to client !");
		}
	}
	
	/**
     * Checks if a supplier is connected by their username.
     *
     * @param username The supplier's username.
     * @return The ConnectionToClient object if the supplier is connected, null otherwise.
     */
	private ConnectionToClient isSupplierConnected(String username) {
		for(ClientUser c : clients){
			if (c.getUsername().equals(username)) {
				return c.getConnection();
			}
		}
		return null;
	}
	
	/**
     * Updates the client's username and connection information.
     *
     * @param client   The connection to the client.
     * @param username The username to associate with the client.
     */
	private void updateClientUsernameAndConnection(ConnectionToClient client, String username) {
	    String ipAddress = client.getInetAddress().getHostAddress();
	    String hostName = client.getInetAddress().getHostName();
	    for (ClientUser c : clients) {
	        if (c.getIpAddress().equals(ipAddress) && c.getHostName().equals(hostName)) {
	            c.setUsername(username);
	            c.setConnection(client);
	            controller.updateUI(getClients());
	            break;
	        }
	    }
	}
	
	/**
     * Retrieves the ConnectionToClient object by username.
     *
     * @param username The username to search for.
     * @return The ConnectionToClient object if found, null otherwise.
     */
	public ConnectionToClient getConnectionToClientByUsername(String username) {
	    for (ClientUser c : clients) {
	        if (username.equals(c.getUsername())) {
	            return c.getConnection();
	        }
	    }
	    return null;
	}

	/**
     * Terminates the server by sending a termination message to all clients.
     */
	public void terminateServer() {
		CommMessage msg = new CommMessage();
		msg.setCommandForServer(CommandConstants.TerminateServer);
		this.sendToAllClients(msg);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	/**
     * Handles a new client connection.
     *
     * @param client The connection to the new client.
     */
	@Override
	protected void clientConnected(ConnectionToClient client) {
	    System.out.println(client + " is connected");
	    ClientUser newClient = new ClientUser(client.getInetAddress().getHostAddress(), client.getInetAddress().getHostName(), "Connected", client);

	    // Check if client is already connected
	    for (ClientUser c : clients) {
	        if (newClient.isEqual(c)) {
	            return;
	        }
	    }
	    clients.add(newClient);
	    //controller.updateUI(getClients());
	}

	/**
     * Handles a client disconnection.
     *
     * @param client The connection to the client that disconnected.
     */
	@Override
	protected void clientDisconnected(ConnectionToClient client) {
	    System.out.println(client + " is disconnected");
	    ClientUser disconnectingClient = new ClientUser(client.getInetAddress().getHostAddress(), client.getInetAddress().getHostName(), "Disconnected", client);

	    // Iterate using an iterator to safely remove elements
	    Iterator<ClientUser> iterator = clients.iterator();
	    while (iterator.hasNext()) {
	        ClientUser c = iterator.next();
	        if (disconnectingClient.isEqual(c)) {
	            if (c.getUsername() != null) {
	                // Call Logout for the current username without sending a message to the client
	                CommMessage commMsg = new CommMessage();
	                serverDB.Logout(c.getUsername(), commMsg);
	            }
	            iterator.remove();
	            controller.updateUI(getClients());
	            break;
	        }
	    }
	    System.out.println(clients);
	    this.getClientConnections();
	    
	}
    
	/**
     * Retrieves the list of currently connected clients.
     *
     * @return An ObservableList of ClientUser objects representing the connected clients.
     */
    public ObservableList<ClientUser> getClients() {
		return FXCollections.observableArrayList(clients);
	}
    
    /**
     * Checks if all clients are disconnected.
     *
     * @return true if all clients are disconnected, false otherwise.
     */
    public boolean areAllDisconnected() {
		for (ClientUser c : clients) {
			if (c.getStatus() == "Connected") {
				return false;
			}
		}
		return true;
	}
    
    /**
     * Checks if all clients are disconnected.
     *
     * @return true if all clients are disconnected, false otherwise.
     */
    @Override
    protected void serverStarted() {
        System.out.println("Server listening for connections on port " + getPort());
    }

    /**
     * Checks if all clients are disconnected.
     *
     * @return true if all clients are disconnected, false otherwise.
     */
    @Override
    protected void serverStopped() {
        System.out.println("Server has stopped listening for connections.");
    }
}
