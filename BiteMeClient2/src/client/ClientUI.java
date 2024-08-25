package client;

import java.io.IOException;
import java.util.TimeZone;

import gui.ConnectionPageController;
import javafx.application.Application;
import javafx.stage.Stage;
import logic.CommMessage;
import logic.Users.User;


public class ClientUI extends Application {

    /**
     * The {@code ClientController} instance used for managing client-server communication.
     */
    public static ClientController chat; 

    /**
     * The {@code ChatClient} instance that represents the client.
     */
    public static ChatClient client;

    /**
     * The {@code User} object that holds the current user's information.
     */
    private User user;
    
    /**
     * The main method that serves as the entry point of the application. 
     * It launches the JavaFX application by calling the {@code launch} method.
     * 
     * @param args Command line arguments passed to the application.
     * @throws Exception If an exception occurs during the launch process.
     */
    public static void main(String args[]) throws Exception {
    	TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jerusalem"));
        launch(args);
    }
    
    /**
     * The start method initializes the JavaFX application and displays 
     * the connection page UI.
     * 
     * @param primaryStage The primary stage for this application.
     * @throws Exception If an error occurs during the initialization.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        ConnectionPageController aFrame = new ConnectionPageController();
        aFrame.start(primaryStage);
    }
    
    /**
     * Sends a request message from the client UI to the server via the {@code ClientController}.
     * 
     * @param comm The {@code CommMessage} object containing the request data.
     */
    public static void RequestData(CommMessage comm) {
        chat.accept(comm);
    }
    
    /**
     * Establishes a new connection to the server with the specified IP address and port.
     * 
     * @param ip   The IP address of the server.
     * @param port The port number on which the server is listening.
     * @return {@code true} if the connection is successfully established; {@code false} otherwise.
     */
    public boolean newConnection(String ip, int port) {
        try {
            chat = new ClientController(ip, port);
            return true;
        } catch (IOException ex) {
            return false;
        }
        
    }
    
    /**
     * Returns the {@code User} object that represents the current user.
     * 
     * @return The {@code User} object containing the current user's information.
     */
    public User getUser() {
        return user;
    }

}
