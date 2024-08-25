package logic;

import java.io.Serializable;
import java.util.ArrayList;

import EnumsAndConstants.CommandConstants;

/**
 * Class to hold a messages between server and client.
 * 
 * This class holds the object with the proper class for the request.
 * 
 */
public class CommMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1062261494793351579L;
	
	//data from server to client
	public boolean isSucceeded;  //flag for indicate if Operation succeeded 
	public String msg; //description why not succeded
	public Object dataFromServer; //requested Object from server (if succeeded)
	public String dataType; //data type of object 
	
	//data from client to server
	public CommandConstants commandForServer;  //command from user to perform //ENUM
	public ArrayList<String> messageForServer;  //command descriptions
	public Object objectForServer; //object for server for create or else
	
	
	

	public CommMessage() {
	}

	//constructor for server use
	public CommMessage(boolean isSucceeded, String msg, Object dataFromServer) {
		this.isSucceeded = isSucceeded;
		this.msg = msg;
		this.dataFromServer = dataFromServer;
	}


	//constructor for client use
	public CommMessage(CommandConstants commandForServer, ArrayList<String> messageForServer) {
		this.commandForServer = commandForServer;
		this.messageForServer = messageForServer;
	}
	
	public CommMessage(CommandConstants commandForServer, ArrayList<String> messageForServer,Object objectForServer) {
		this.commandForServer = commandForServer;
		this.messageForServer = messageForServer;
		this.objectForServer = objectForServer;
	}
	
	

	@Override
	public String toString() {
		if (dataFromServer != null) {
			return "[Request Type: " + commandForServer + ",Object: " + dataFromServer.toString() + "]";
		}
		return "[Request Type: " + commandForServer + "]";
	}

	public Object getObjectForServer() {
		return objectForServer;
	}
	
	public void setObjectForServer(Object objectForServer) {
		this.objectForServer = objectForServer;
	}
	
	/**
	 * @return the commMessage
	 */
	public Object getCommMessage() {
		return dataFromServer;
	}
	
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	
	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}
	
	/**
	 * @param commMessage the commMessage to set
	 */
	public void setCommMessage(Object dataFromServer) {
		this.dataFromServer = dataFromServer;
	}
	
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public CommandConstants getCommandForServer() {
		return commandForServer;
	}
	
	public void setCommandForServer(CommandConstants commandForServer) {
		this.commandForServer = commandForServer;
	}
	
	public ArrayList<String> getMessageForServer() {
		return messageForServer;
	}
	
	public void setMessageForServer(ArrayList<String> messageForServer) {
		this.messageForServer = messageForServer;
	}
	
	public boolean isSucceeded() {
		return isSucceeded;
	}
	
	public void setSucceeded(boolean isSucceeded) {
		this.isSucceeded = isSucceeded;
	}
	
	public Object getDataFromServer() {
		return dataFromServer;
	}
	
	public void setDataFromServer(Object dataFromServer) {
		this.dataFromServer = dataFromServer;
	}
}
