package com.example.socket_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Random;


import com.example.sensors_listener.MyAccelerometerListener;
import com.example.sensors_listener.MyCompassListener;
import com.example.sensors_listener.MyLocationListener;
import com.example.socket_adk.Server;

import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

/**
 * MainActivity is a class that display the application trademark but behind the scene
 * this method handles all of the incoming commands from the user application and transferring
 * them to the arduino adk over the USB socket, this method also send the user application all
 * of the sensor information that indicate important info about the flight. 
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */
public class MainActivity extends Activity {

	private final String TAG = MainActivity.class.getSimpleName();

	//.: Connection variables.
	public static String ipaddress = "46.121.229.158";
	public static int port = 5555;
	public final Random myRandom = new Random();
	public static int serial;
	public Socket mySkt;
	public PrintStream myPS;
	public BufferedReader myBR;
	//===========================

	//.: Connection variables.
	public static String ipaddress2 = "46.121.229.158";
	public static int port2 = 5555;
	public Socket mySkt2;
	public PrintStream myPS2;
	public BufferedReader myBR2;
	//===========================


	//.: Other variables.
	public String temp;
	public String myEditValue;
	public String myEditValue2;
	public TextView textDisplay1;    
	//===========================

	//.: Connection ADK variables.
	public Server mServer = null;
	public static boolean autoFlight = false; 	
	public int action1 = 0;
	public int action2 = 0;
	public int action3 = 0;
	//===========================


	//.: Sensors variables.
	//Accelerometer
	public SensorManager mSensorManager;
	public MyAccelerometerListener acc;
	public float[] accValues = {0.0f,0.0f,0.0f};
	//Accelerometer
	public SensorManager cSensorManager;
	public MyCompassListener comp;
	public float[] compValues = {0.0f,0.0f,0.0f};
	//GPS Location
	public LocationManager locationMangaer=null;
	public LocationListener locationListener=null;	
	public double[] gpsValues = {0.0f,0.0f,0.0f};
	public double[] gpsValues2 = {0.0f,0.0f,0.0f};
	public String cityName;
	public int speed = 0;
	public Location location;
	public Location locationA;
	public Location locationB;
	public double distance = 0.0;
	public String[] destenation;
	//===========================

	//.: Battery variables.
	public int[] battValues = {0,0,0};
	//=========================== 	


	//*************************** ..:: onCreate ::.. *************************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when the activity is starting. This is where most initialization should go: calling setContentView(int) to inflate the activity's UI, using findViewById(int) to programmatically interact with widgets in the UI, calling managedQuery(android.net.Uri, String[], String, String[], String) to retrieve cursors for data being displayed, etc. 	
	 * You can call finish() from within this function, in which case onDestroy() will be immediately called without any of the rest of the activity lifecycle (onStart(), onResume(), onPause(), etc) executing. 
	 * Derived classes must call through to the super class's implementation of this method. If they do not, an exception will be thrown.
	 * 
	 * This method starts the initComponents method, starts the Accelerometer and compass sensors and start
	 * the method createEvants.   
	 * 
	 * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
	 * @since           1.0
	 */	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initComponents();
		acc.startSimulation();
		comp.startSimulation();
		createEvents();		
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
		myEditValue = "Plane:check";
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		acc = new MyAccelerometerListener(mSensorManager); 
		cSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		comp = new MyCompassListener(cSensorManager); 
		locationMangaer = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener(getBaseContext());
		locationMangaer.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);			
		this.registerReceiver(this.batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		serial = myRandom.nextInt(1000000000)+1;
		textDisplay1 = (TextView)    this.findViewById(R.id.textView2);
		location = locationMangaer.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		locationA = new Location("point A");
		locationB = new Location("point A");
	}
	//************************************************************************//

	//********************* ..:: Create Events ::.. **************************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to start the activity events that runs in background.
	 * This method start a client thread that send commands to the arduino adk.
	 * This method start a client thread that send the server a message every 1 second to check if the user give us a command.
	 * This method start a client thread that send the server a message every 10 second to update the user and the web site with the new sensors information. 
	 * 
	 * 
	 * @since           1.0
	 */	    
	public void createEvents(){	
		runOnUiThread(new Runnable() {
			public void run() {
				textDisplay1.append("" + serial);
			}
		});		

		try {
			mServer = new Server(4568); // Use ADK port
			mServer.start();
		} catch (IOException e) {
			Log.e(TAG, "Unable to start TCP server", e);
			System.exit(-1);
		}

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						myEditValue = "Plane:check";
						new massege().execute("1");
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						accValues = acc.getAccelerometer(); 
						compValues = comp.getCompass();
						gpsValues =  ((MyLocationListener) locationListener).getLocationAttributes();
						cityName = ((MyLocationListener) locationListener).getLocationCityName();
						speed = ((MyLocationListener) locationListener).getSpeed();

						myEditValue2 = "Plane:Sensors accx:(" + accValues[0] + ") accy:(" + accValues[1] + ") accz:(" + accValues[2] +")" +
								" compx:(" + compValues[0] + ") compy:(" + compValues[1] + ") compz:(" + compValues[2] +")" +
								" gpslat:(" + gpsValues[0] + ") gpslon:(" + gpsValues[1] + ") gpsalt:(" + gpsValues[2] +") city:(" + cityName + ")" +
								" battlevel:(" + battValues[0] + ") battvoltage:(" + battValues[1] + ") battstatus:(" + battValues[2] +") speed: (" + speed + ") distance:(" + distance + ") serial: (" + serial + ")";

						new massege2().execute("2");						
						Thread.sleep(10000);

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	//************************************************************************//

	//*********************** ..:: onBackPressed ::.. ************************//
	/**
	 *
	 * Added in API level 5
	 * Called when the activity has detected the user's press of the back key. The default implementation simply finishes the current activity, but you can override this to do whatever you want.
	 *
	 * This method create an alert dialog that asks the user if he want to close the application.
	 *
	 * @since           1.0
	 *  
	 */	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
		.setTitle("AA-UAV Application")
		.setMessage("Close this application?")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();				
			}
		})
		.setNegativeButton("No", null)
		.show();
	}
	//************************************************************************//    

	//************************ ..:: Server socket ::.. ***********************//
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
			try {
				mySkt = new Socket(ipaddress, port);
				myPS = new PrintStream(mySkt.getOutputStream());

				myPS.println(myEditValue);

				myBR = new BufferedReader(new InputStreamReader(mySkt.getInputStream()));

				temp = myBR.readLine();

				myBR.close();
				myPS.close();
				mySkt.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			if (temp != null)
				return temp;
			else
				return urls[0];
		}

		protected void onPostExecute(String result) {
			if(result.compareTo("none2") != 0)
			{
				if(result.compareTo("up")       == 0)    { action1=1;  sendToADK();}
				if(result.compareTo("down")     == 0)    { action1=2;  sendToADK();}
				if(result.compareTo("rright")   == 0)    { action1=3;  sendToADK();}
				if(result.compareTo("rleft")    == 0)    { action1=4;  sendToADK();}
				if(result.compareTo("forward")  == 0)    { action1=5;  sendToADK();}
				if(result.compareTo("backward") == 0)    { action1=6;  sendToADK();}
				if(result.compareTo("right")    == 0)    { action1=7;  sendToADK();}
				if(result.compareTo("left")     == 0)    { action1=8;  sendToADK();}
				if(result.compareTo("takeoff")  == 0)    { action1=9;  sendToADK();}
				if(result.compareTo("lend")     == 0)    { action1=10; sendToADK();}
				if(result.compareTo("emergency")== 0)    { action1=12; sendToADK();}
				if(result.contains(",")      == true)    { startAutoNavigation(result);  autoFlight = true;}
				if(result.contains("stopautof") == true) { autoFlight = false; action1=11; sendToADK();}
			}
		}
	}
	//************************************************************************//

	//************************ ..:: Server socket ::.. ***********************// 
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
	private class massege2 extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... urls) {
			try {
				mySkt2 = new Socket(ipaddress2, port2);
				myPS2 = new PrintStream(mySkt2.getOutputStream());

				myPS2.println(myEditValue2);

				myBR2 = new BufferedReader(new InputStreamReader(mySkt2.getInputStream()));

				temp = myBR2.readLine();

				myBR2.close();
				myPS2.close();
				mySkt2.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			if (temp != null)
				return temp;
			else
				return urls[0];
		}

		protected void onPostExecute(String result) {
			if(result.compareTo("none2") != 0)
			{   			  	
			}
		}
	}
	//************************************************************************//     	

	//******************* ..:: startAutoNavigation ::.. **********************//
	/** 
	 * Added in API level 17
	 * 
	 * Called when we want to start auto navigation.
	 * This method start a thread that handle the auto navigation by calculating the distance left
	 * and send to the arduino the right command for flying the drone. 
	 * 
	 * @since           1.0
	 */	    
	public void startAutoNavigation(String destpoint){
		destenation = destpoint.split("\\s*,\\s*");
		locationB.setLatitude(Double.parseDouble(destenation[0]));
		locationB.setLongitude(Double.parseDouble(destenation[1]));
		action1=9; 
		sendToADK();                        	

		new Thread(new Runnable() {
			public void run() {
				while (autoFlight) {
					try {
						gpsValues2 =  ((MyLocationListener) locationListener).getLocationAttributes();
						locationA.setLatitude(gpsValues2[0]);
						locationA.setLongitude(gpsValues2[1]);						
						distance = locationA.distanceTo(locationB);

						if(distance > 70){
							action1=5; 
							sendToADK();                        	
						}
						if(distance < 70){
							action1=11; 
							sendToADK();                        	
							Thread.sleep(5000);
							action1=10; 
							sendToADK();
							autoFlight = false;
						}
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();		
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
			mServer.send(new byte[] { (byte) action1, (byte) action2, (byte) action3 });
		} catch (IOException e) {
			Log.e(TAG, "problem sending TCP message", e);
		}				   		
	}
	//************************************************************************//

	//********************* ..:: Battery Information ::.. ********************//
	/**
	 * Added in API level 1
	 * 
	 * If you don't need to send broadcasts across applications, consider using this class with LocalBroadcastManager instead of the more general facilities described below.
	 * This will give you a much more efficient implementation (no cross-process communication needed) and allow you to avoid thinking about any security issues related to other applications being able to receive or send your broadcasts. 
	 *  
	 * A BroadcastReceiver object is only valid for the duration of the call to onReceive(Context, Intent). Once your code returns from this function, the system considers the object to be finished and no longer active.
	 * A process that is currently executing a BroadcastReceiver (that is, currently running the code in its onReceive(Context, Intent) method) is considered to be a foreground process and will be kept running by the system except under cases of extreme memory pressure.
	 *
	 * This method Override the onReceive method in order to save the battery value in a public array every time we receive a new info from the battery.  
	 * @since           1.0
	 */	
	public BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {        

		@Override
		public void onReceive(Context context, Intent intent) {

			int  level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
			int  status= intent.getIntExtra(BatteryManager.EXTRA_STATUS,0);
			int  voltage= intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);

			battValues[0] = level;
			battValues[1] = voltage;
			battValues[2] = status;

		}
	};
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
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	//************************************************************************//
}
