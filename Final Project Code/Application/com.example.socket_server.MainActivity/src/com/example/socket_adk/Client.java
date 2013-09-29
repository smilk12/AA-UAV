package com.example.socket_adk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import android.util.Log;

/**
 * Client class gives us the ability to connect, send and receive massages from the server using TCP.
 * 
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */
public class Client
{

	private Socket socket;

	private final Server server;

	private final InputStream input;
	private final OutputStream output;

	private boolean keepAlive = true;

	//************************* ..:: client ::.. ****************************//
	/** 
	 * client constructor initialize class variables.
	 *
	 * @param server the server object.    
	 * @param socket the server socket.
	 *     
	 * @since           1.0
	 */	
	public Client(Server server, Socket socket) throws IOException
	{
		this.server = server;
		this.socket = socket;
		socket.setKeepAlive(true);

		this.input = this.socket.getInputStream();
		this.output = this.socket.getOutputStream();

		startCommunicationThread();
	}	
	//************************************************************************//    

	//***************** ..:: startCommunicationThread ::.. *******************//
	/** 
	 * start the client communication to the server thread.
	 *
	 * @since           1.0
	 */	
	public void startCommunicationThread()
	{
		(new Thread() {
			public void run()
			{
				while (keepAlive)
				{
					try
					{

						// Check for input
						if (input.available()>0)
						{

							int bytesRead;
							byte buf[] = new byte[input.available()];
							bytesRead = input.read(buf);

							if (bytesRead==-1)
								keepAlive = false;
							else
								server.receive(Client.this, buf);
						}

					} catch (IOException e)
					{
						keepAlive = false;
						Log.d("microbridge", "IOException: " + e);
					}
				}

				// Client exited, notify parent server
				server.disconnectClient(Client.this);
			}
		}).start();
	}
	//************************************************************************//    

	//*************************** ..:: close ::.. ****************************//
	/** 
	 * close the connection to the server.
	 *
	 * @since           1.0
	 */	
	public void close()
	{
		keepAlive = false;

		// Close the socket, will throw an IOException in the listener thread.
		try
		{
			socket.close();
		} catch (IOException e)
		{
			Log.e("microbridge", "error while closing socket", e);
		}
	}
	//************************************************************************//    

	//**************************** ..:: send ::.. ****************************//
	/**
	 * Send bytes to the server.
	 *  
	 * @param data data to send
	 * @throws IOException
	 * @since       1.0
	 */
	public void send(byte[] data) throws IOException
	{
		try {
			output.write(data);
			output.flush();
		} catch (SocketException ex)
		{
			// Broken socket, disconnect
			close();
			server.disconnectClient(this);
		}
	}
	//************************************************************************//    

	//**************************** ..:: send ::.. ****************************//
	/**
	 * Send a string to the server.
	 * @param command string to send
	 * @throws IOException
	 * @since       1.0
	 */
	public void send(String command) throws IOException
	{
		send(command.getBytes());
	}
	//************************************************************************//    
}
