package com.example.socket_adk;

/**
 * 
 * Base class for implementing a ServerListener. Extend this class to capture a subset of the server events.
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */
public class AbstractServerListener implements ServerListener
{
	//********************** ..:: onServerStarted ::.. ***********************//
	/**
	 * Called when the server is started.
	 * @param server the server that is started 
	 * @since       1.0
	 */
	public void onServerStarted(Server server)
	{
	}
	//************************************************************************//    

	//*********************** ..:: onServerStopped ::.. **********************//
	/**
	 * Called when the server is stopped.
	 * @param server the server that is stopped 
	 * @since       1.0
	 */
	public void onServerStopped(Server server)
	{
	}
	//************************************************************************//    

	//********************** ..:: onClientConnect ::.. ***********************//
	/**
	 * Called when a new client (device) connects to the server.
	 * @param server the server that is started 
	 * @param client the Client object representing the newly connected client
	 * @since       1.0
	 */
	public void onClientConnect(Server server, Client client)
	{
	}
	//************************************************************************//    

	//********************* ..:: onClientDisconnect ::.. *********************//
	/**
	 * Called when a new client (device) disconnects from the server.
	 * @param server the server that is started 
	 * @param client the Client that disconnected
	 * @since       1.0
	 */
	public void onClientDisconnect(Server server, Client client)
	{
	}
	//************************************************************************//    

	//************************* ..:: onReceive ::.. **************************//
	/**
	 * Called when data is received from the client.
	 * @param client source client
	 * @param data data
	 * @since       1.0
	 */
	public void onReceive(Client client, byte[] data)
	{
	}
	//************************************************************************//    
}
