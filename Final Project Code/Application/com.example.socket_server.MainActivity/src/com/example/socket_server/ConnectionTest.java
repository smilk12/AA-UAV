package com.example.socket_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import com.example.socket_adk.AbstractServerListener;
import com.example.socket_adk.Client;
import com.example.socket_adk.Server;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * ConnectionTest is a class that checks connections to all of the necessary components in order to use
 * the server application. 
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */
public class ConnectionTest extends Activity {

	private final String TAG = ConnectionTest.class.getSimpleName();

	//.: Connection variables.
	public static String ipaddress = "46.121.229.158";
	public static int port = 5555;
	public Socket mySkt;
	public PrintStream myPS;
	public BufferedReader myBR;
	//===========================

	//.: View variables.
	public TextView textDisplay2;    
	public TextView textDisplay4;    
	public TextView textDisplay7;    
	public TextView textDisplay10;    
	public ProgressBar progBar1;
	public ProgressBar progBar2;
	public ProgressBar progBar3;
	public ProgressBar progBar4;
	public Button b1;
	public ImageView image1;
	public ImageView image2;
	public ImageView image3;
	public ImageView image4;
	public ImageView image5;
	public ImageView image6;
	public ImageView image7;
	public ImageView image8;
	public ImageView image9;
	public ImageView image10;
	public ImageView image11;
	public ImageView image12;
	public ImageView image13;
	public ImageView image14;
	public ImageView image15;
	public ImageView image16;
	public ImageView image17;
	//===========================

	//.: Connection ADK variables.
	public Server mServer = null;
	public int action1 = 0;
	public int action2 = 0;
	public int action3 = 0;
	public int response = 0;
	//===========================

	//.: Other variables.
	public String temp;
	public String myEditValue;
	public boolean CheckServer = false;
	public boolean CheckADK = false;
	public boolean CheckInternet = false;
	public boolean CheckGPS = false;
	//===========================

	//.: Sensors variables.
	//GPS Location
	public Boolean GPSflag = false;
	//===========================


	//*************************** ..:: onCreate ::.. *************************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when the activity is starting. This is where most initialization should go: calling setContentView(int) to inflate the activity's UI, using findViewById(int) to programmatically interact with widgets in the UI, calling managedQuery(android.net.Uri, String[], String, String[], String) to retrieve cursors for data being displayed, etc. 	
	 * You can call finish() from within this function, in which case onDestroy() will be immediately called without any of the rest of the activity lifecycle (onStart(), onResume(), onPause(), etc) executing. 
	 * Derived classes must call through to the super class's implementation of this method. If they do not, an exception will be thrown.
	 * 
	 * This method starts the initComponents method,createEvants method and the startChecking method.   
	 * 
	 * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
	 * @since           1.0
	 */	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection_test);

		initComponents();
		createEvents();
		startChecking();
	}
	//************************************************************************//    

	//******************** ..:: Initialize Components ::.. *******************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to initialize all of the view and sensor variables.
	 * 
	 * @since           1.0
	 */	    
	public void initComponents(){
		textDisplay2 = (TextView)    this.findViewById(R.id.textView2);
		textDisplay4 = (TextView)    this.findViewById(R.id.textView4);
		textDisplay7 = (TextView)    this.findViewById(R.id.textView7);
		textDisplay10 = (TextView)    this.findViewById(R.id.textView10);
		progBar1     = (ProgressBar) this.findViewById(R.id.progressBar1);
		progBar2     = (ProgressBar) this.findViewById(R.id.progressBar2);
		progBar3     = (ProgressBar) this.findViewById(R.id.progressBar3);
		progBar4     = (ProgressBar) this.findViewById(R.id.progressBar4);
		image1       = (ImageView)   this.findViewById(R.id.imageView1);
		image2       = (ImageView)   this.findViewById(R.id.imageView2);
		image3       = (ImageView)   this.findViewById(R.id.imageView3);
		image4       = (ImageView)   this.findViewById(R.id.imageView4);
		image5       = (ImageView)   this.findViewById(R.id.imageView5);
		image6       = (ImageView)   this.findViewById(R.id.imageView6);
		image7       = (ImageView)   this.findViewById(R.id.imageView7);
		image8       = (ImageView)   this.findViewById(R.id.imageView8);
		image9       = (ImageView)   this.findViewById(R.id.imageView9);
		image10      = (ImageView)   this.findViewById(R.id.imageView10);
		image11      = (ImageView)   this.findViewById(R.id.imageView11);
		image12      = (ImageView)   this.findViewById(R.id.imageView12);
		image13      = (ImageView)   this.findViewById(R.id.imageView13);
		image14      = (ImageView)   this.findViewById(R.id.imageView14);
		image15      = (ImageView)   this.findViewById(R.id.imageView15);
		image16      = (ImageView)   this.findViewById(R.id.imageView16);
		image17      = (ImageView)   this.findViewById(R.id.imageView17);
	}
	//************************************************************************//   

	//********************* ..:: Create Events ::.. **************************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to start the activity events that runs in background.
	 * This method start a client thread that send commands to the arduino adk.
	 * This method start a listener for the response we get from the arduino adk server and take the responses into action. 
	 * This method start a runOnUiThread that change the activity view in first use and when a button is pressed.
	 * This method handle a button click event that changes the activity view.   
	 * 
	 * 
	 * @since           1.0
	 */	    
	public void createEvents(){
		b1 = (Button)findViewById(R.id.button1);    	
		runOnUiThread(new Runnable() {
			public void run() {
				b1.setVisibility(View.INVISIBLE);
				progBar1.setVisibility(View.VISIBLE);
				progBar2.setVisibility(View.INVISIBLE);
				progBar3.setVisibility(View.INVISIBLE);
				progBar4.setVisibility(View.INVISIBLE);
				image1.setVisibility(View.VISIBLE);
				image2.setVisibility(View.INVISIBLE);
				image3.setVisibility(View.INVISIBLE);
				image4.setVisibility(View.INVISIBLE);
				image5.setVisibility(View.INVISIBLE);
				image6.setVisibility(View.INVISIBLE);
				image7.setVisibility(View.INVISIBLE);
				image8.setVisibility(View.INVISIBLE);
				image9.setVisibility(View.INVISIBLE);
				image10.setVisibility(View.INVISIBLE);
				image11.setVisibility(View.INVISIBLE);
				image12.setVisibility(View.INVISIBLE);
				image13.setVisibility(View.INVISIBLE);
				image14.setVisibility(View.INVISIBLE);
				image15.setVisibility(View.INVISIBLE);
				image16.setVisibility(View.INVISIBLE);
				image17.setVisibility(View.INVISIBLE);
				textDisplay2.setVisibility(View.INVISIBLE);
				textDisplay4.setVisibility(View.INVISIBLE);
				textDisplay7.setVisibility(View.INVISIBLE);
				textDisplay10.setVisibility(View.INVISIBLE);
			}
		});

		b1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				runOnUiThread(new Runnable() {
					public void run() {
						b1.setVisibility(View.INVISIBLE);
						progBar1.setVisibility(View.VISIBLE);
						progBar2.setVisibility(View.INVISIBLE);
						progBar3.setVisibility(View.INVISIBLE);
						progBar4.setVisibility(View.INVISIBLE);
						image1.setVisibility(View.VISIBLE);
						image2.setVisibility(View.INVISIBLE);
						image3.setVisibility(View.INVISIBLE);
						image4.setVisibility(View.INVISIBLE);
						image5.setVisibility(View.INVISIBLE);
						image6.setVisibility(View.INVISIBLE);
						image7.setVisibility(View.INVISIBLE);
						image8.setVisibility(View.INVISIBLE);
						image9.setVisibility(View.INVISIBLE);
						image10.setVisibility(View.INVISIBLE);
						image11.setVisibility(View.INVISIBLE);
						image12.setVisibility(View.INVISIBLE);
						image13.setVisibility(View.INVISIBLE);
						image14.setVisibility(View.INVISIBLE);
						image15.setVisibility(View.INVISIBLE);
						image16.setVisibility(View.INVISIBLE);
						image17.setVisibility(View.INVISIBLE);
						textDisplay2.setVisibility(View.INVISIBLE);
						textDisplay4.setVisibility(View.INVISIBLE);
						textDisplay7.setVisibility(View.INVISIBLE);
						textDisplay10.setVisibility(View.INVISIBLE);
					}
				});
				CheckServer = false;
				CheckADK = false;
				CheckGPS = false;
				CheckInternet = false;
				startChecking();
			}
		});                  	

		try {
			mServer = new Server(4568); // Use ADK port
			mServer.start();
		} catch (IOException e) {
			Log.e(TAG, "Unable to start TCP server", e);
			System.exit(-1);
		}

		mServer.addListener(new AbstractServerListener() {	    	 
			@Override
			public void onReceive(Client client, byte[] data)
			{
				response =  ((data[1] << 8) | (data[0] & 0xFF));
				if (response == 30){CheckADK = true;}
			}
		});		
	}
	//************************************************************************//   

	//********************* ..:: StartChecking ::.. **************************//
	/** 
	 * Added in API level 1
	 * startChecking method checks if there is a connection to all components that we need in order to use this application.
	 * This method checks connection to the Internet,Drone Server,GPS and Arduino ADK, if all connection are OK we will transfer to the 
	 * next activity screen, if not we will get a button that we can press in order to checks the connections again.
	 *
	 * @since           1.0
	 */	
	public void startChecking(){    	
		new Thread(new Runnable() {

			public void run() {
				try {
					//--------------------------- Checking Internet Connection ------------------------------// 
					Thread.sleep(1000);
					if(isConnectingToInternet() == true){
						runOnUiThread(new Runnable() {
							public void run() {
								progBar1.setVisibility(View.INVISIBLE);
								image2.setVisibility(View.VISIBLE);
								image4.setVisibility(View.VISIBLE);
								textDisplay2.setVisibility(View.VISIBLE);
								progBar2.setVisibility(View.VISIBLE);
								CheckInternet = true;									        
							}
						});
					}else{
						runOnUiThread(new Runnable() {
							public void run() {
								progBar1.setVisibility(View.INVISIBLE);
								image5.setVisibility(View.VISIBLE);
								image16.setVisibility(View.VISIBLE);
								textDisplay2.setVisibility(View.VISIBLE);
							}
						});
						Thread.interrupted();
					}
					//---------------------------------------------------------------------------------------// 

					//----------------------------- Checking Server Connection ------------------------------// 
					if(CheckServer == false){
						myEditValue = "Plane:CheckServerConnection";
						new massege().execute("4");
						Thread.sleep(4000);
					}

					if(CheckServer == true){
						runOnUiThread(new Runnable() {
							public void run() {
								progBar2.setVisibility(View.INVISIBLE);
								image3.setVisibility(View.VISIBLE);
								image6.setVisibility(View.VISIBLE);
								textDisplay4.setVisibility(View.VISIBLE);
								progBar4.setVisibility(View.VISIBLE);
							}
						});
					}else{	
						runOnUiThread(new Runnable() {
							public void run() {
								progBar2.setVisibility(View.INVISIBLE);
								image7.setVisibility(View.VISIBLE);
								image17.setVisibility(View.VISIBLE);
								textDisplay4.setVisibility(View.VISIBLE);
								b1.setVisibility(View.VISIBLE);
							}
						});
						Thread.interrupted();
					}
					//---------------------------------------------------------------------------------------// 

					//-------------------------------- Checking GPS Connection ------------------------------// 
					if (getGpsStatus() == true) {
						runOnUiThread(new Runnable() {
							public void run() {
								progBar4.setVisibility(View.INVISIBLE);
								image11.setVisibility(View.VISIBLE);
								image12.setVisibility(View.VISIBLE);
								textDisplay10.setVisibility(View.VISIBLE);
								progBar3.setVisibility(View.VISIBLE);
								CheckGPS = true;
							}
						});
					} else {
						runOnUiThread(new Runnable() {
							public void run() {
								progBar4.setVisibility(View.INVISIBLE);
								image13.setVisibility(View.VISIBLE);
								image15.setVisibility(View.VISIBLE);
								textDisplay10.setVisibility(View.VISIBLE);
								b1.setVisibility(View.VISIBLE);
							}
						});
						Thread.interrupted();
					}
					//---------------------------------------------------------------------------------------// 

					//-------------------------------- Checking ADK Connection ------------------------------// 
					if(CheckADK == false){
						action1 = 30;
						sendToADK();
						Thread.sleep(1000);
					}
					//CheckADK = true;//Delete
					if(CheckADK == true){
						runOnUiThread(new Runnable() {
							public void run() {
								progBar3.setVisibility(View.INVISIBLE);
								image9.setVisibility(View.VISIBLE);
								image8.setVisibility(View.VISIBLE);
								textDisplay7.setVisibility(View.VISIBLE);
							}
						});                       
						Thread.sleep(2000);
					}else{
						runOnUiThread(new Runnable() {
							public void run() {
								progBar3.setVisibility(View.INVISIBLE);
								image10.setVisibility(View.VISIBLE);
								image14.setVisibility(View.VISIBLE);
								textDisplay7.setVisibility(View.VISIBLE);
								b1.setVisibility(View.VISIBLE);
							}
						});
						Thread.interrupted();                        	
					}                                                							
					//---------------------------------------------------------------------------------------//

					if(CheckServer == true && CheckADK == true && CheckGPS == true && CheckInternet == true){
						mServer.stop();
						finish();
						startActivity(new Intent("com.example.socket_server.mainactivity.MainActivity"));                        	
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();               	    	
	}
	//************************************************************************//   

	//************************ ..:: Client socket ::.. ***********************//
	/**
	 * Added in API level 1
	 * AsyncTask enables proper and easy use of the UI thread. This class allows to perform background operations and publish results on the UI thread without having to manipulate threads and/or handlers.
	 * AsyncTask must be subclassed to be used. The subclass will override at least one method (doInBackground(Params...)), and most often will override a second one (onPostExecute(Result).)
	 *
	 * @param String value that we want to make use in the background task process.
	 * 
	 * This method override the doInBackground method in order to implements the socket client connection to the drone server in order to send commands to the server and get a response.
	 * This method override the onPostExecute method in order to implements the socket client connection response that we got from the drone server.
	 * 
	 * @since           1.0
	 *  
	 */	
	private class massege extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... urls) {
			try{
				mySkt = new Socket(ipaddress, port);
				myPS = new PrintStream(mySkt.getOutputStream());

				myPS.println(myEditValue);

				myBR = new BufferedReader(new InputStreamReader(mySkt.getInputStream()));

				temp = myBR.readLine();

				myBR.close();
				myPS.close();
				mySkt.close();
			}catch (Exception e) {
				e.printStackTrace();
			}	        		

			if(temp!=null)
				return temp;
			else
				return urls[0];
		}

		protected void onPostExecute(String result) {
			if(result.compareTo("ConnectionOK") == 0) {CheckServer = true;}
		}
	}
	//************************************************************************//

	//************************ ..:: Server ADK ::.. **************************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to send a command to the arduino adk.
	 * This method Send the state commands to ADK Main Board as a byte
	 * 
	 * @since           1.0
	 */	    
	public void sendToADK(){
		try {
			// Send the state of each LED to ADK Main Board as a byte
			mServer.send(new byte[] { (byte) action1, (byte) action2, (byte) action3 });
		} catch (IOException e) {
			Log.e(TAG, "problem sending TCP message", e);
		}				   		
	}
	//************************************************************************//    

	//****************** ..:: isConnectingToInternet ::.. ********************//
	/** 
	 * Added in API level 1
	 * isConnectingToInternet method checks if there is a connection to the Internet.
	 *
	 * @return True if there is a connection to the Internet and false if there isn't.       
	 * @since           1.0
	 */	
	public boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}
		}
		return false;          
	}
	//************************************************************************//

	//********************** ..:: getGpsStatus ::.. **************************//
	/** 
	 * Added in API level 1
	 * getGpsStatus method checks if the GPS id turned on.
	 *
	 * @return True if there is a GPS turned on and false if he isn't.       
	 * @since           1.0
	 */	
	private boolean getGpsStatus() {
		ContentResolver contentResolver = getBaseContext().getContentResolver();
		boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(
				contentResolver, LocationManager.GPS_PROVIDER);
		if (gpsStatus) {
			return true;
		} else {
			return false;
		}
	}
	//************************************************************************//

	//******************* ..:: onCreateOptionsMenu ::.. **********************//
	/** 
	 *
	 * Added in API level 1
	 * Initialize the contents of the Activity's standard options menu. You should place your menu items in to menu. 
	 * This is only called once, the first time the options menu is displayed. To update the menu every time it is displayed, see onPrepareOptionsMenu(Menu). 
	 * The default implementation populates the menu with standard system menu items. These are placed in the CATEGORY_SYSTEM group so that they will be correctly ordered with application-defined menu items. Deriving classes should always call through to the base implementation. 
	 * You can safely hold on to menu (and any items created from it), making modifications to it as desired, until the next time onCreateOptionsMenu() is called. 
	 * When you add items to the menu, you can implement the Activity's onOptionsItemSelected(MenuItem) method to handle them there.
	 *
	 * @param  menu  The options menu in which you place your items. 
	 * @return You must return true for the menu to be displayed; if you return false it will not be shown.
	 * @since           1.0
	 */	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_connection_test, menu);
		return true;
	}
	//************************************************************************//
}
