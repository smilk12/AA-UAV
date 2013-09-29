package il.co.server;


import java.net.*;
import java.io.*;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * server is a class that implements a TCP multiThreaded client server.
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */
public class server extends Thread{

	//.: Server variables.
	public PrintStream SSPS;
	public BufferedReader SS_BF;
	public Socket SS_accept;
	public ServerSocket mySS;
	public PrintStream CSPS;
	public Socket myCS;
	public JTextArea textgui;    
	public Controller control;
	//===========================

	//.: View variables.
	public JLabel AccX;
	public JLabel AccY;
	public JLabel AccZ;
	public JLabel gpsLat;
	public JLabel gpsLon;
	public JLabel gpsAlt;
	public JLabel gpsCity;	
	public JLabel compYaw;	
	public JLabel compRoll;	
	public JLabel compPitch;	
	public JLabel battaryLevel;
	public JLabel battaryVoltage;
	public JLabel battaryStatus;
	//===========================


	//*************************** ..:: server ::.. **************************//
	/**
	 * server constructor to initialize all of the server variables.
	 *
	 *	@param text - GUI Frame text;
	 *	@param AccX - Accelerometer X JLabel;
	 *	@param AccY - Accelerometer Y JLabel;
	 *	@param AccZ - Accelerometer Z JLabel;
	 *	@param gpsLat - GPS Latitude JLabel;
	 *	@param gpsLon - GPS Longitude JLabel;
	 *	@param gpsAlt - GPS Altitude  JLabel;
	 *	@param gpsCity - GPS City JLabel;
	 *	@param compYaw - Compass Yaw JLabel;
	 *	@param compRoll - Compass Roll JLabel;
	 *	@param compPitch - Compass Pitch JLabel;
	 *	@param battLevel - Battery Level JLabel;
	 *	@param battStatus - Battery Status JLabel;
	 *	@param battVoltage - Battery Voltage JLabel;	 	
	 */
	public server(JTextArea text,JLabel AccX,JLabel AccY,JLabel AccZ,JLabel gpsLat,JLabel gpsLon,JLabel gpsAlt,JLabel gpsCity,JLabel compYaw,JLabel compRoll,JLabel compPitch,JLabel battLevel,JLabel battVoltage,JLabel battStatus) throws IOException
	{
		textgui = text;
		this.AccX = AccX;
		this.AccY = AccY;
		this.AccZ = AccZ;
		this.gpsLat = gpsLat;
		this.gpsLon = gpsLon;
		this.gpsAlt = gpsAlt;
		this.gpsCity = gpsCity;
		this.compYaw = compYaw;
		this.compRoll = compRoll;
		this.compPitch = compPitch;
		this.battaryLevel = battLevel;
		this.battaryStatus = battStatus;
		this.battaryVoltage = battVoltage;		
	}
	//************************************************************************//   

	//***************************** ..:: run ::.. ****************************//
	/**
	 * Launch the drone server Thread.
	 */
	public void run()
	{
		while(true)
		{
			try
			{
				control = new Controller();
				control.Initialize(AccX, AccY, AccZ, gpsLat, gpsLon, gpsAlt, gpsCity, compYaw, compRoll, compPitch, battaryLevel, battaryVoltage, battaryStatus);
				mySS = new ServerSocket();
				mySS.bind(new InetSocketAddress("192.168.1.111", 5555));

				textgui.append("ArDrone server running on **** [192.168.1.111:5555]\n");

				while (true) {
					try {
						SS_accept = mySS.accept();
						clientThread thread = new clientThread(SS_accept,textgui,control);	
						thread.start();
					} catch (IOException e) {
						System.out.println(e);
					}
				}				
			}catch(SocketTimeoutException s)
			{
				textgui.append("Socket timed out!");
				break;
			}catch(IOException e)
			{
				e.printStackTrace();
				break;
			}
		}
	}
	//************************************************************************//   
}

