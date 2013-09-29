package il.co.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.SQLException;
import javax.swing.JTextArea;

/**
 * clientThread is a class that create a new TCP client tread.
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */
class clientThread extends Thread {

	//.: Server variables.
	private BufferedReader is = null;
	private PrintStream os = null;
	private Socket clientSocket = null;
	public JTextArea textgui;    
	public Controller control;
	//===========================

	//************************ ..:: ActivityDBManager ::.. *************************// 
	/** 
	 * Added in API level 1
	 * 
	 * constructor initialize the client variables.
	 * 
	 * @param clientSocket - the new client socket. 
	 * @param text - the UI text area
	 * @param control - the controller object.
	 * 
	 * @since           1.0
	 */	    
	public clientThread(Socket clientSocket,JTextArea text,Controller control) {
		this.clientSocket = clientSocket;
		textgui = text;
		this.control = control;
	}
	//************************************************************************//   

	//***************************** ..:: run ::.. ****************************//
	/**
	 * Launch the drone client Thread.
	 */
	public void run() {
		try {

			is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			os = new PrintStream(clientSocket.getOutputStream());

			/* Start the conversation. */
			String line = is.readLine();
			/* The message is public, broadcast it to all other clients. */
			synchronized (this) {
				textgui.append("From: [" + this.clientSocket.getInetAddress() + ":" + this.clientSocket.getPort() + "]    Msg: " + line + "\n");
				try {
					os.println(control.getAction(line));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			is.close();
			os.close();
			clientSocket.close();
			Thread.currentThread().interrupt();
		} catch (IOException e) {
		}
	}
	//************************************************************************//   
}
