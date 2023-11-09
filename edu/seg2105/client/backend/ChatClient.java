// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  String loginID; 
  boolean alreadyLogin = false; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID; 
    openConnection();
    if (loginID=="") {
    	quit(); 
    }
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	if (message.startsWith("#") && !(message.startsWith("#login"))){
    		handleCommand(message);
    	}
    	else {
    		sendToServer(message);
    	}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  private void handleCommand(String command) {
	  if (command.equals("#quit")) {
		  quit();
	  }
	  else if(command.equals("#logoff")) {
		  try {
			  closeConnection();
		  }
		  catch (Exception e) {
			  clientUI.display("failed to disconnect :(");
		  }
	  }
	  else if(command.startsWith("#sethost")) {
		  if (!isConnected()) {
			  try {
				  String[] arr = command.split(" "); 
				  String newHost = (arr[1]); 
				  setHost(newHost); 
			  }
			  catch(Exception e) {
				  clientUI.display("failed to set the new host name, please enter a correct command");
			  }
		  }
		  else {
			  clientUI.display("please disconnect from the server first");
		  }
		  
	  }
	  else if(command.startsWith("#setport")) {
		  if (!isConnected()) {
			  try {
				  String[] arr = command.split(" "); 
				  int newPort = Integer.parseInt(arr[1]); 
				  setPort(newPort); 
			  }
			  catch(Exception e) {
				  clientUI.display("failed to set the new port, please enter a correct command");
			  }
		  }
		  else {
			  clientUI.display("please disconnect from the server first");
		  }
		  
	  }
	  else if(command.equals("#login")) {
		  if (!isConnected()) {
			  try {
				  openConnection();
			  }
			  catch(Exception e) {
				  clientUI.display("failed to connect to the server :(");
			  }
		  }
		  else {
			  clientUI.display("You already connected to the server!");
		  }
	  }
	  else if(command.equals("#gethost")) {
		  clientUI.display(getHost());
	  }
	  else if(command.equals("#getport")) {
		  clientUI.display(Integer.toString(getPort()));
	  }
	  else {
		  clientUI.display("This is an unknown command");
	  }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  public String getLoginID() {
	  return this.loginID;
  }
  
  /**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
  	@Override
	protected void connectionClosed() {
  		clientUI.display("Connect closed!");
		
	}
	
	/**
	 * Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  	@Override
	protected void connectionException(Exception exception) {
		clientUI.display("The server has been shut down :( ");
		quit();
		
	}
}
//End of ChatClient class
