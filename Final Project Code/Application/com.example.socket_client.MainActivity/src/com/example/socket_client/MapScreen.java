package com.example.socket_client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import com.example.components.VerticalProgressBar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MapScreen is a class that display google map and handle all of the map events such as making a marker,
 * moving a marker, display the plane information and sending the destenation point for the auto flight.
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */
public class MapScreen extends Activity implements OnMapClickListener, OnMapLongClickListener, OnMarkerDragListener{

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
	public boolean visibaleFlag = false;
	public TextView textDisplay1;    
	public TextView textDisplay2;   
	public String[] sensors;
	//===========================

	//.: Layout View
	public VerticalProgressBar vProgressBar;
	public ProgressBar hProgressBar;
	public ImageView image1;
	public GridLayout g1;
	public GridLayout g2;
	//===========================

	//.: Menu
	private MenuItem mItemEnInfo;
	private MenuItem mItemDisInfo;
	//===========================

	//.: Map
	final int RQS_GooglePlayServices = 1;
	private GoogleMap myMap;	
	public Location myLocation;
	public TextView tvLocInfo;	
	public boolean markerClicked;
	public boolean markerClickedOnce;
	public PolygonOptions polygonOptions;
	public Polygon polygon; 	
	public String destpoint = "";
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_screen);			
		initComponents();        
		createEvents();
	}
	//************************************************************************//

	//*********************** ..:: onBackPressed ::.. ************************// 
	/**
	 *
	 * Added in API level 5
	 * Called when the activity has detected the user's press of the back key. The default implementation simply finishes the current activity, but you can override this to do whatever you want.
	 *
	 * This method create an alert dialog that asks the user want to stop the auto flight and go back to manual control.
	 *
	 * @since           1.0
	 *  
	 */	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
		.setTitle("AA-UAV Application")
		.setMessage("Stop auto flight and back to manual control ?")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				myEditValue = "User:stopAutoF";
				new massege().execute("4");
				startActivity(new Intent("com.example.socket_client.controllerscreen.ControllerScreen"));
				finish();				
			}
		})
		.setNegativeButton("No", null)
		.show();
	}
	//************************************************************************//       	

	//******************** ..:: Initialize Components ::.. *******************// 
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to initialize all of the view and map variables.
	 * 
	 * @since           1.0
	 */	    
	public void initComponents(){
		vProgressBar = (VerticalProgressBar)findViewById(R.id.verticalProgressBar1);
		vProgressBar.setMax(100);
		hProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
		hProgressBar.setMax(100);
		vProgressBar.setProgress(0);
		hProgressBar.setProgress(0);
		image1       = (ImageView)   this.findViewById(R.id.imageView1);
		image1.setVisibility(View.INVISIBLE);
		textDisplay1 = (TextView)    this.findViewById(R.id.textView1);
		textDisplay2 = (TextView)    this.findViewById(R.id.textView2);
		textDisplay1.setVisibility(View.INVISIBLE);
		textDisplay2.setVisibility(View.INVISIBLE);
		g1 = (GridLayout)    this.findViewById(R.id.gridLayout1);
		g2 = (GridLayout)    this.findViewById(R.id.gridLayout2);    
		g1.setVisibility(View.INVISIBLE);
		g2.setVisibility(View.INVISIBLE);

		tvLocInfo = (TextView)findViewById(R.id.textView3);

		FragmentManager myFragmentManager = getFragmentManager();
		MapFragment myMapFragment = (MapFragment)myFragmentManager.findFragmentById(R.id.fragment1);
		myMap = myMapFragment.getMap();

		myMap.setMyLocationEnabled(true);

		//		myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		myMap.setOnMapClickListener(this);
		myMap.setOnMapLongClickListener(this);
		myMap.setOnMarkerDragListener(this);

		markerClicked = false;        
		markerClickedOnce = false;		
	}
	//************************************************************************//    

	//********************* ..:: Create Events ::.. **************************// 
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to start the activity events that runs in background.
	 * This method start a client thread that send the server a message every 10 second to update the user and the web site with the new sensors information. 
	 * 
	 * 
	 * @since           1.0
	 */	    
	public void createEvents(){
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

	//******************* ..:: onOptionsItemSelected ::.. ********************//
	/**
	 * Added in API level 1
	 * This hook is called whenever an item in your options menu is selected. The default implementation simply returns false to have the normal processing happen (calling the item's Runnable or sending a message to its Handler as appropriate). You can use this method for any items for which you would like to do processing without those other facilities. 
	 * Derived classes should call through to the base class for it to perform the default menu handling.
	 * 
	 * This method handled all of the menu events.
	 *  
	 * @param item  The menu item that was selected. 
	 * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
	 * @since           1.0
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if ( item == mItemEnInfo ) {
			image1.setVisibility(View.VISIBLE);
			g1.setVisibility(View.VISIBLE);
			g2.setVisibility(View.VISIBLE);
			textDisplay1.setVisibility(View.VISIBLE);
			textDisplay2.setVisibility(View.VISIBLE);    		
		} else if ( item == mItemDisInfo ) {
			image1.setVisibility(View.INVISIBLE);
			g1.setVisibility(View.INVISIBLE);
			g2.setVisibility(View.INVISIBLE);
			textDisplay1.setVisibility(View.INVISIBLE);
			textDisplay2.setVisibility(View.INVISIBLE);
		}
		return super.onOptionsItemSelected(item);
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
	 * This method gives us two options to enable the plane information or to disable it, the default is disable.
	 *
	 * @param  menu  The options menu in which you place your items. 
	 * @return You must return true for the menu to be displayed; if you return false it will not be shown.
	 * @since           1.0
	 */	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		mItemEnInfo = menu.add("Info Enable");
		mItemDisInfo = menu.add("Info Disable");
		return (super.onCreateOptionsMenu(menu));
	}
	//************************************************************************//

	//********************* ..:: onMarkerDragStart ::.. **********************//
	/**
	 * Added in API level 1
	 * Called after onRestoreInstanceState(Bundle), onRestart(), or onPause(), for your activity to start interacting with the user. This is a good place to begin animations, open exclusive-access devices (such as the camera), etc. 
	 * Keep in mind that onResume is not the best indicator that your activity is visible to the user; a system window such as the keyguard may be in front. Use onWindowFocusChanged(boolean) to know for certain that your activity is visible to the user (for example, to resume a game). 
	 * Derived classes must call through to the super class's implementation of this method. If they do not, an exception will be thrown.
	 *
	 * This method checks if we have a connection to the google map service and display a massage according to the current status.
	 *
	 */
	@Override
	protected void onResume() {

		super.onResume();

		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

		if (resultCode == ConnectionResult.SUCCESS){
			Toast.makeText(getApplicationContext(), 
					"isGooglePlayServicesAvailable SUCCESS", 
					Toast.LENGTH_LONG).show();
		}else{
			GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
		}

	}
	//************************************************************************//    

	//********************* ..:: onMarkerDragStart ::.. **********************//
	/**
	 * Added in API level 1
	 * Called repeatedly while a marker is being dragged. The marker's location can be accessed via getPosition().
	 *
	 * This method display us the marker id on the map view.
	 * 
	 * @param marker  The marker being dragged.  
	 */
	public void onMarkerDrag(Marker marker) {
		// TODO Auto-generated method stub
		tvLocInfo.setText("Marker " + marker.getId() + " Drag@" + marker.getPosition());		
	}
	//************************************************************************//    

	//********************* ..:: onMarkerDragStart ::.. **********************//
	/**  
	 * Added in API level 1
	 * Called when a marker has finished being dragged. The marker's location can be accessed via getPosition().
	 *
	 * This method move the selected marker to the last place where we touched the map screen and
	 * ask us with a alert dialog if we want to start auto flight to that marker destination. 
	 * 
	 * @param marker  The marker that was dragged.  
	 */
	public void onMarkerDragEnd(Marker marker) {
		// TODO Auto-generated method stub
		tvLocInfo.setText("Marker " + marker.getId() + " DragEnd");		
		destpoint = "" + marker.getPosition();

		new AlertDialog.Builder(this)
		.setTitle("AA-UAV Application")
		.setMessage("Start Auto Flight to destenation point ?")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				myEditValue = "User:" + destpoint;
				new massege().execute("4");			}
		})
		.setNegativeButton("No", null)
		.show();
	}
	//************************************************************************//    

	//********************* ..:: onMarkerDragStart ::.. **********************//
	/**
	 * Added in API level 1
	 * Called when a marker starts being dragged. The marker's location can be accessed via getPosition(); this position may be different to the position prior to the start of the drag because the marker is popped up above the touch point.
	 *
	 * This method display us the marker id on the map view.
	 * 
	 * @param marker  The marker being dragged.  
	 */
	public void onMarkerDragStart(Marker marker) {
		// TODO Auto-generated method stub
		tvLocInfo.setText("Marker " + marker.getId() + " DragStart");		
	}
	//************************************************************************//    

	//*********************** ..:: onMapLongClick ::.. ***********************//
	/**
	 * Added in API level 1
	 * Called when the user makes a long-press gesture on the map, but only if none of the overlays of the map handled the gesture. Implementations of this method are always invoked on the main thread.
	 *
	 * This method create a marker after a long click on the map screen and
	 * ask us with alert dialog if we want to start auto flight to that marker destination. 
	 * 
	 * @param point  The point on the ground (projected from the screen point) that was tapped.  
	 */
	public void onMapLongClick(LatLng point) {
		// TODO Auto-generated method stub
		if(markerClickedOnce == false){
			tvLocInfo.setText(point.toString());
			myMap.addMarker(new MarkerOptions()
			.position(point)
			.draggable(true)
			.title("Destenetion Piont")
			.snippet("Point: " + point.toString())
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.plane)));
			markerClickedOnce = true;
			destpoint = "" + point.toString();

			new AlertDialog.Builder(this)
			.setTitle("AA-UAV Application")
			.setMessage("Start Auto Flight to destenation point ?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					myEditValue = "User:" + destpoint;
					new massege().execute("4");			}
			})
			.setNegativeButton("No", null)
			.show();
		}
		markerClicked = false;		
	}
	//************************************************************************//    

	//************************** ..:: onMapClick ::.. ************************//
	/**
	 * Added in API level 1
	 * Called when the user makes a tap gesture on the map, but only if none of the overlays of the map handled the gesture. Implementations of this method are always invoked on the main thread.
	 *
	 * This method display on the map screen the lat\lon coordinates.
	 *
	 * @param point  The point on the ground (projected from the screen point) that was tapped.  
	 *
	 */
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub
		tvLocInfo.setText(point.toString());
		myMap.animateCamera(CameraUpdateFactory.newLatLng(point));

		markerClicked = false;		
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
}