package Server;

import ServerGui.ServerPortFrameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.CommMessage;

/**
 * The ServerUI class serves as the main entry point for the server application.
 * It extends the JavaFX Application class and provides methods to control the
 * server's lifecycle, including starting and stopping the server, and managing
 * database connections.
 */
public class ServerUI extends Application {
	
	/**
	 * The ServerUI class serves as the main entry point for the server application.
	 * It extends the JavaFX Application class and provides methods to control the
	 * server's lifecycle, including starting and stopping the server, and managing
	 * database connections.
	 */
	private static EchoServer server;
    
	/**
     * The controller for the server's GUI.
     */
	private ServerPortFrameController controller;
    
	/**
     * The controller for the server's GUI.
     */
	private static ServerUI instance;


	/**
     * The main method, which launches the JavaFX application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
        System.out.println("Server is starting...");
    }

    /**
     * The main method, which launches the JavaFX application.
     *
     * @param args Command-line arguments.
     */
    public static ServerUI getInstance() {
        if (instance == null) {
            instance = new ServerUI();
        }
        return instance;
    }

    /**
     * Starts the server on the specified port and associates it with the provided controller.
     *
     * @param p          The port number as a string.
     * @param controller The server GUI controller.
     */
    public static void runServer(String p, ServerPortFrameController controller) {
        int port = Integer.parseInt(p);
        server = new EchoServer(port, controller);
        try {
            server.listen();
        } catch (Exception ex) {
            System.out.println("ERROR - Could not listen for clients!");
        }
    }
    
    /**
     * Attempts to connect to the database using the provided credentials and database name.
     *
     * @param username The database username.
     * @param password The database password.
     * @param dbName   The name of the database to connect to.
     * @return true if the connection is successful, false otherwise.
     */
    public static boolean runDB(String username, String password, String dbName) {

        try {
            return serverDB.connectToDB(username, password, dbName);
            
        } catch (Exception ex) {
            System.out.println("ERROR - Could not listen for clients!");
            return false;
        }
    }

    
    /**
     * Starts the JavaFX application by initializing and displaying the primary stage.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception if an error occurs during initialization.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        controller = new ServerPortFrameController();
        controller.setServerUI(this);
        controller.start(primaryStage);
        
    }
    
    /**
     * Stops the server if it is running.
     */
    public static void stopServer() {
    	try {
    		if (server != null) {
    			server.close();
    			System.out.println("Server stopped.");
    		}
    	} catch (Exception e) {
    		System.out.println("Error stopping the server: " + e.getMessage());
    	}
    }
    
    /**
     * Returns the server's logged-in indicator status.
     *
     * @return false, indicating that the feature is not currently implemented.
     */
    public boolean loggedInIndicator() {
    	//return server.areAllDisconnected();
    	return false;
    }
    
    /**
     * Retrieves the EchoServer instance.
     *
     * @return The EchoServer instance.
     */
    public EchoServer getServer() {
    	return server;
    }
    
    /**
     * Logs out all connected clients by sending a logout message to each.
     *
     * @return true if all clients are successfully logged out, false otherwise.
     */
    public boolean logoutAll() {
    	try {
    		CommMessage msg = new CommMessage();
    		serverDB.LogoutAll(msg);
    		if (msg.isSucceeded) {
    			return true;
    		}
    		else {
    			return false;
    		}
    	}catch (Exception e) {
    		return false;
    	}
    }
}
