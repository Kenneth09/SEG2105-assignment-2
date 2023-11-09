package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;
import edu.seg2105.client.common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  ChatIF serverUI; 
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI ) 
  {
    super(port);
    this.serverUI = serverUI; 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  try {
		  String m = msg.toString();
		  if (m.startsWith("#login")) {
			  if (client.getInfo("connected")!=null && (Boolean)client.getInfo("connected")==true) {
				  client.sendToClient("ERROR!!!!!!!!!!");
				  client.close();
			  }
			  else {
				  String[] arr = m.split(" ");
				  client.setInfo("LoginID", arr[1]);
				  client.setInfo("connected",true);
				  client.sendToClient(arr[0]+" <"+arr[1]+">");
			  }
		  }
		  else {
			  System.out.println("Message received: " + msg + " from " + client);
			    String userID;
			    try {
			    	userID = "<"+client.getInfo("LoginID").toString()+">";
			    }
			    catch (Exception e) {
			    	userID = "<Unknown>";
			    }
			    this.sendToAllClients(userID+msg);
		  }
	  }
	  catch (Exception e) {}

  }
  
  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromServerUI(String message)
  {
	//any type must send to the clients
  	sendToAllClients("SERVER MSG: "+message);
	    try
	    {
	    	if (message.startsWith("#")){
	    		handleCommand(message);
	    	}
	    }
	    catch(Exception e)
	    {
	      System.out.println("Could not handle the message");
	       
	    }
  }
  
  private void handleCommand(String command) {
	  if (command.equals("#quit")) {
		  quit(); 
	  }
	  else if (command.equals("#stop")) {
		  stopListening();
	  }
	  else if (command.equals("#close")) {
		  try{
			  close(); 
		  }
		  catch (Exception e) {
			  System.out.println("an unknown error occurs");
		  }
	  }
	  else if(command.startsWith("#setport")) {
		  if (!isListening() && getNumberOfClients()==0) {
			  try {
				  String[] arr = command.split(" "); 
				  int newPort = Integer.parseInt(arr[1]); 
				  setPort(newPort); 
			  }
			  catch(Exception e) {
				 System.out.println("failed to set the new port, please enter a correct command");
			  }
		  }
		  else {
			  System.out.println("please close the server first");
		  }
		  
	  }
	  else if (command.equals("#start")) {
		  if (!isListening()){
			  try {
					listen();
				  } 
				  catch (IOException e) {
					System.out.println("an unknown error occurs");
				}
		 }
		  else {
			  System.out.println("Server is not close yet");
		  }
	  }
	  
	  else if (command.equals("getport")) {
		  System.out.println(getPort());
	  }
	 
	  else {
		  System.out.println("This is an unknown command");
	  }
  }
  
  
  public void quit()
  {
    try
    {
      close();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  
  //No longer need this, cuz we build new server console
//  public static void main(String[] args) 
//  {
//    int port = 0; //Port to listen on
//
//    try
//    {
//      port = Integer.parseInt(args[0]); //Get port from command line
//    }
//    catch(Throwable t)
//    {
//      port = DEFAULT_PORT; //Set port to 5555
//    }
//	
//    EchoServer sv = new EchoServer(port);
//    
//    try 
//    {
//      sv.listen(); //Start listening for connections
//    } 
//    catch (Exception ex) 
//    {
//      System.out.println("ERROR - Could not listen for clients!");
//    }
//  }
  
  /**
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  @Override
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("The client: \""+client.getName()+"\"has connect to the client");
	  
  }

  /**
   * Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println("The client: \""+client.getName()+"\"has disconnect to the client");
  }
}
//End of EchoServer class
