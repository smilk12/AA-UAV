package com.example.socket_client;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;

import com.example.components.VerticalProgressBar;
import com.example.myJoyStick.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * ControllerScreen is a class that display the application controller screen.
 * The controller screen controlling the drone with two joysticks and a couple of buttons.
 * There is two joysticks,each joystick controller differently on the drone.
 * The right joystick controller : Forward , Backward , Rotate Left, Rotate Right.  
 * The left  joystick controller : Up , Down , Left, Right.
 * The additional buttons are : Take Off, Lend, Information, Map.
 * All of the joysticks and buttons are sending their commands to the drone server except from the
 * Information that gets data from the drone server and Map that transfer us to the Map Activity.   
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */
public class ControllerScreen extends Activity implements OnSharedPreferenceChangeListener{

	//.: debug / logs
	private final boolean D = false;
	private static final String TAG = ControllerScreen.class.getSimpleName();
	//===========================

	//.: Connection variables.
	public static String ipaddress = "46.121.229.158";
	public static int port = 5555;
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
	ImageButton takeoff;
	ImageButton land;
	public boolean visibaleFlag = false;
	public TextView textDisplay1;    
	public TextView textDisplay3;   
	public String[] sensors;
	//===========================

	//.: Layout View
	public DualJoystickView mDualJoystick;
	public VerticalProgressBar vProgressBar;
	public ProgressBar hProgressBar;
	public ImageView image1;
	public GridLayout g1;
	public GridLayout g2;
	//===========================

	//.: Menu
	private MenuItem mItemConnect;
	private MenuItem mItemOptions;
	private MenuItem mItemAbout; 	
	//===========================

	//.: polar coordinates
	private double mRadiusL = 0, mRadiusR = 0;
	private double mAngleL = 0, mAngleR = 0;
	private boolean mCenterL = true, mCenterR = true;
	//===========================

	//.: timer task
	private int mTimeoutCounter = 0;
	private int mMaxTimeoutCount; // actual timeout = count * updateperiod 
	//===========================

	//*************************** ..:: onCreate ::.. *************************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when the activity is starting. This is where most initialization should go: calling setContentView(int) to inflate the activity's UI, using findViewById(int) to programmatically interact with widgets in the UI, calling managedQuery(android.net.Uri, String[], String, String[], String) to retrieve cursors for data being displayed, etc. 	
	 * You can call finish() from within this function, in which case onDestroy() will be immediately called without any of the rest of the activity lifecycle (onStart(), onResume(), onPause(), etc) executing. 
	 * Derived classes must call through to the super class's implementation of this method. If they do not, an exception will be thrown.
	 * 
	 * This method starts the initComponents method and createEvants method.   
	 * 
	 * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
	 * @since           1.0
	 */	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_controller_screen);

		initComponents();
		createEvents();
	}
	//************************************************************************//    

	//******************** ..:: Initialize Components ::.. *******************// 
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to initialize all of the view variables.
	 * 
	 * @since           1.0
	 */	    
	public void initComponents(){
		mDualJoystick = (DualJoystickView)findViewById(R.id.dualJoystickView1);
		mDualJoystick.setOnJostickMovedListener(_listenerLeft, _listenerRight);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);        
		mMaxTimeoutCount = Integer.parseInt(prefs.getString( "maxtimeout_count", "20" ));
		vProgressBar = (VerticalProgressBar)findViewById(R.id.verticalProgressBar1);
		vProgressBar.setMax(100);
		hProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
		hProgressBar.setMax(100);
		vProgressBar.setProgress(0);
		hProgressBar.setProgress(0);
		image1       = (ImageView)   this.findViewById(R.id.imageView2);
		image1.setVisibility(View.INVISIBLE);
		textDisplay1 = (TextView)    this.findViewById(R.id.textView1);
		textDisplay3 = (TextView)    this.findViewById(R.id.textView3);
		textDisplay1.setVisibility(View.INVISIBLE);
		textDisplay3.setVisibility(View.INVISIBLE);
		g1 = (GridLayout)    this.findViewById(R.id.gridLayout1);
		g2 = (GridLayout)    this.findViewById(R.id.gridLayout2);    
		g1.setVisibility(View.INVISIBLE);
		g2.setVisibility(View.INVISIBLE);
	}
	//************************************************************************//    

	//********************* ..:: Create Events ::.. **************************// 
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to start the activity events that runs in background.
	 * This method start a client thread that send the server a message every 10 second to update the user and the web site with the new sensors information.
	 * This method start a joystick thread that update the joysticks view.
	 * This method handle all of the button event.  
	 * 
	 * 
	 * @since           1.0
	 */	    
	public void createEvents(){
		takeoff = (ImageButton)findViewById(R.id.imageButton1);    	
		takeoff.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				myEditValue = "User:Take_off";
				takeoff.setVisibility(View.INVISIBLE);
				land.setVisibility(View.VISIBLE);
				new massege().execute("4");
			}
		});                  	

		land = (ImageButton)findViewById(R.id.imageButton5);    	
		land.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				myEditValue = "User:Lend";
				takeoff.setVisibility(View.VISIBLE);
				land.setVisibility(View.INVISIBLE);
				new massege().execute("4");
			}
		});                  	

		ImageButton info = (ImageButton)findViewById(R.id.imageButton2);    	
		info.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if(visibaleFlag == true){
					image1.setVisibility(View.INVISIBLE);
					g1.setVisibility(View.INVISIBLE);
					g2.setVisibility(View.INVISIBLE);
					textDisplay1.setVisibility(View.INVISIBLE);
					textDisplay3.setVisibility(View.INVISIBLE);
					visibaleFlag = false;
				}
				else{
					image1.setVisibility(View.VISIBLE);
					g1.setVisibility(View.VISIBLE);
					g2.setVisibility(View.VISIBLE);
					textDisplay1.setVisibility(View.VISIBLE);
					textDisplay3.setVisibility(View.VISIBLE);
					visibaleFlag = true;	        		  
				}
			}
		});                  	

		ImageButton map = (ImageButton)findViewById(R.id.imageButton3);    	
		map.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				finish();  
				startActivity(new Intent("com.example.socket_client.mapscreen.MapScreen"));
			}
		});                  	

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						UpdateMethod();
						Thread.sleep(2000);
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
						myEditValue2 = "User:getSensors";
						new massege2().execute("1");
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
	 * This method make a menu with three options : Connect, Options and About.
	 *
	 * @param  menu  The options menu in which you place your items. 
	 * @return You must return true for the menu to be displayed; if you return false it will not be shown.
	 * @since           1.0
	 */	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mItemConnect = menu.add("Connect");
		mItemOptions = menu.add("Options");
		mItemAbout = menu.add("About");
		return (super.onCreateOptionsMenu(menu));    	
	}    
	//************************************************************************//   

	//******************* ..:: onOptionsItemSelected ::.. ********************//
	/**
	 * Added in API level 1
	 * This hook is called whenever an item in your options menu is selected. The default implementation simply returns false to have the normal processing happen (calling the item's Runnable or sending a message to its Handler as appropriate). You can use this method for any items for which you would like to do processing without those other facilities. 
	 * Derived classes should call through to the base class for it to perform the default menu handling.
	 * 
	 * This method handled all of the menu events, in this method only the about option display information.
	 *  
	 * @param item  The menu item that was selected. 
	 * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
	 * @since           1.0
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if ( item == mItemConnect ) {
			//	Intent serverIntent = new Intent(this, DeviceListActivity.class);
			//	startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
		} else if ( item == mItemOptions ) {
			//	startActivity( new Intent(this, OptionScreen.class) );
		} else if ( item == mItemAbout ) {
			AlertDialog about = new AlertDialog.Builder(this).create();
			about.setCancelable(false);
			about.setMessage("AA-UAV v.1 by Shmulik Melamed & Lital Motola - www.aa-uav.jupalo.com");
			about.setButton(AlertDialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			about.show();
		}
		return super.onOptionsItemSelected(item);
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

	//******************* ..:: JoystickMovedListener ::.. ********************// 
	/**
	 *
	 * Added in API level 5
	 * Called when the activity has detected that the user moved the left joystick.
	 *
	 * This listener methods checks the joystick angle on the view and according to that she send to the server the right command,
	 * when the user not touching the joystick then this method return the joystick to the center. 
	 *
	 * @since           1.0
	 *  
	 */	
	private JoystickMovedListener _listenerLeft = new JoystickMovedListener() {

		public void OnMoved(int pan, int tilt) {
			mRadiusL = Math.sqrt((pan*pan) + (tilt*tilt));
			//mAngleL = Math.atan2(pan, tilt);
			mAngleL = Math.atan2(-pan, -tilt);
			mAngleL = mAngleL * 180 / Math.PI;
			if(mAngleL < 180 && mAngleL > 145 || mAngleL < -145 && mAngleL > -179){myEditValue = "User:Backward"; new massege().execute("4");}else
				if(mAngleL < 45 && mAngleL > 0 || mAngleL < 0 && mAngleL > -45){myEditValue = "User:Forward"; new massege().execute("4");}else
					if(mAngleL < -45 && mAngleL > -145){myEditValue = "User:R_Right"; new massege().execute("4");}else
						if(mAngleL < 145 && mAngleL > 45){myEditValue = "User:R_Left"; new massege().execute("4");}
			//textDisplay1.setText(String.format("( r%.0f, %.0f\u00B0 )", Math.min(mRadiusL, 10), mAngleL * 180 / Math.PI));
			mCenterL = false;
		}

		public void OnReleased() {
			// 
		}

		public void OnReturnedToCenter() {
			mRadiusL = mAngleL = 0;
			UpdateMethod();
			mCenterL = true;
		}
	};

	//************************************************************************//

	//******************** ..:: JoystickMovedListener ::.. *******************// 
	/**
	 *
	 * Added in API level 5
	 * Called when the activity has detected that the user moved the right joystick.
	 *
	 * This listener methods checks the joystick angle on the view and according to that she send to the server the right command,
	 * when the user not touching the joystick then this method return the joystick to the center. 
	 *
	 * @since           1.0
	 *  
	 */	
	private JoystickMovedListener _listenerRight = new JoystickMovedListener() {

		public void OnMoved(int pan, int tilt) {
			mRadiusR = Math.sqrt((pan*pan) + (tilt*tilt));
			//mAngleR = Math.atan2(pan, tilt);
			mAngleR = Math.atan2(-pan, -tilt);
			mAngleR = mAngleR * 180 / Math.PI;
			if(mAngleR < 180 && mAngleR > 145 || mAngleR < -145 && mAngleR > -179){myEditValue = "User:Down"; new massege().execute("4");}else
				if(mAngleR < 45 && mAngleR > 0 || mAngleR < 0 && mAngleR > -45){myEditValue = "User:Up"; new massege().execute("4");}else
					if(mAngleR < -45 && mAngleR > -145){myEditValue = "User:Right"; new massege().execute("4");}else
						if(mAngleR < 145 && mAngleR > 45){myEditValue = "User:Left"; new massege().execute("4");}
			//textDisplay2.setText(String.format("( r%.0f, %.0f\u00B0 )", Math.min(mRadiusR, 10), mAngleR * 180 / Math.PI ));
			mCenterR = false;
		}

		public void OnReleased() {
			//
		}

		public void OnReturnedToCenter() {
			mRadiusR = mAngleR = 0;
			UpdateMethod();
			mCenterR = true;
		}
	};
	//************************************************************************//

	//************************* ..:: UpdateMethod ::.. ***********************// 
	/**
	 *
	 * Added in API level 5
	 * Called when we want to update the joystick view when when we move it.
	 *
	 * @since           1.0
	 *  
	 */	
	private void UpdateMethod() {

		// if either of the joysticks is not on the center, or timeout occurred
		if(!mCenterL || !mCenterR || (mTimeoutCounter>=mMaxTimeoutCount && mMaxTimeoutCount>-1) ) {
			// limit to {0..10}
			byte radiusL = (byte) ( Math.min( mRadiusL, 10.0 ) );
			byte radiusR = (byte) ( Math.min( mRadiusR, 10.0 ) );
			// scale to {0..35}
			byte angleL = (byte) ( mAngleL * 18.0 / Math.PI + 36.0 + 0.5 );
			byte angleR = (byte) ( mAngleR * 18.0 / Math.PI + 36.0 + 0.5 );
			if( angleL >= 36 )	angleL = (byte)(angleL-36);
			if( angleR >= 36 )	angleR = (byte)(angleR-36);

			if (D) {
				Log.d(TAG, String.format("%d, %d, %d, %d", radiusL, angleL, radiusR, angleR ) );
			}

			mTimeoutCounter = 0;
		}
		else{
			if( mMaxTimeoutCount>-1 )
				mTimeoutCounter++;
		}	
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
				if(result.contains(",") == true) 
				{
					sensors = result.split("\\s*,\\s*");   				
					hProgressBar.setProgress((int)Math.round(Double.parseDouble(sensors[0])));
					vProgressBar.setProgress((int)Math.round(Double.parseDouble(sensors[1])));

				}
			}
		}
	}
	//************************************************************************//       

	//****************** ..:: onSharedPreferenceChanged ::.. *****************//
	/**
	 * Added in API level 1
	 * Called when a shared preference is changed, added, or removed. This may be called even if a preference is set to its existing value. 
	 *
	 * This callback will be run on your main thread.
	 *
	 * @param arg0  The SharedPreferences that received the change. 
	 * @param arg1  The key of the preference that was changed, added, or removed.  
	 */
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// TODO Auto-generated method stub
	}
	//************************************************************************//   
}