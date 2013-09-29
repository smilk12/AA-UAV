package com.example.socket_client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
	public ProgressBar progBar1;
	public ProgressBar progBar2;
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
	//===========================

	//.: Other variables.
	public String temp;
	public String myEditValue;
	public boolean CheckResult = false;
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
		progBar1     = (ProgressBar) this.findViewById(R.id.progressBar1);
		progBar2     = (ProgressBar) this.findViewById(R.id.progressBar2);
		image1       = (ImageView)   this.findViewById(R.id.imageView1);
		image2       = (ImageView)   this.findViewById(R.id.imageView2);
		image3       = (ImageView)   this.findViewById(R.id.imageView3);
		image4       = (ImageView)   this.findViewById(R.id.imageView4);
		image5       = (ImageView)   this.findViewById(R.id.imageView5);
		image6       = (ImageView)   this.findViewById(R.id.imageView6);
		image7       = (ImageView)   this.findViewById(R.id.imageView7);
		image8       = (ImageView)   this.findViewById(R.id.imageView8);
		image9       = (ImageView)   this.findViewById(R.id.imageView9);
	}
	//************************************************************************//   

	//********************* ..:: Create Events ::.. **************************// 
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to start the activity events that runs in background.
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
				image1.setVisibility(View.VISIBLE);
				image2.setVisibility(View.INVISIBLE);
				image3.setVisibility(View.INVISIBLE);
				image4.setVisibility(View.INVISIBLE);
				image5.setVisibility(View.INVISIBLE);
				image6.setVisibility(View.INVISIBLE);
				image7.setVisibility(View.INVISIBLE);
				image8.setVisibility(View.INVISIBLE);
				image9.setVisibility(View.INVISIBLE);
				textDisplay2.setVisibility(View.INVISIBLE);
				textDisplay4.setVisibility(View.INVISIBLE);
			}
		});

		b1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				runOnUiThread(new Runnable() {
					public void run() {
						b1.setVisibility(View.INVISIBLE);
						progBar1.setVisibility(View.VISIBLE);
						progBar2.setVisibility(View.INVISIBLE);
						image1.setVisibility(View.VISIBLE);
						image2.setVisibility(View.INVISIBLE);
						image3.setVisibility(View.INVISIBLE);
						image4.setVisibility(View.INVISIBLE);
						image5.setVisibility(View.INVISIBLE);
						image6.setVisibility(View.INVISIBLE);
						image7.setVisibility(View.INVISIBLE);
						image8.setVisibility(View.INVISIBLE);
						image9.setVisibility(View.INVISIBLE);
						textDisplay2.setVisibility(View.INVISIBLE);
						textDisplay4.setVisibility(View.INVISIBLE);
					}
				});
				CheckResult = false;
				startChecking();
			}
		});                  	

	}
	//************************************************************************//   

	//********************* ..:: StartChecking ::.. **************************// 
	/** 
	 * Added in API level 1
	 * startChecking method checks if there is a connection to all components that we need in order to use this application.
	 * This method checks connection to the Internet and Drone Server, if all connection are OK we will transfer to the 
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
							}
						});
					}else{
						runOnUiThread(new Runnable() {
							public void run() {
								progBar1.setVisibility(View.INVISIBLE);
								textDisplay2.setVisibility(View.VISIBLE);
								image5.setVisibility(View.VISIBLE);
								image8.setVisibility(View.VISIBLE);
							}
						});
						Thread.interrupted();
					}
					//---------------------------------------------------------------------------------------// 

					//----------------------------- Checking Server Connection ------------------------------// 
					if(CheckResult == false){
						myEditValue = "User:CheckServerConnection";
						new massege().execute("4");
						Thread.sleep(4000);//
					}

					if(CheckResult == true){
						runOnUiThread(new Runnable() {
							public void run() {
								progBar2.setVisibility(View.INVISIBLE);
								image3.setVisibility(View.VISIBLE);
								image6.setVisibility(View.VISIBLE);
								textDisplay4.setVisibility(View.VISIBLE);
							}
						});
						Thread.sleep(2000);
						finish();                 
						startActivity(new Intent("com.example.socket_client.serialverification.SerialVerification"));
					}else{	
						runOnUiThread(new Runnable() {
							public void run() {
								progBar2.setVisibility(View.INVISIBLE);
								image7.setVisibility(View.VISIBLE);
								image9.setVisibility(View.VISIBLE);
								textDisplay4.setVisibility(View.VISIBLE);
								b1.setVisibility(View.VISIBLE);
							}
						});
						Thread.interrupted();
					}
					//---------------------------------------------------------------------------------------// 
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
			if(result.compareTo("ConnectionOK") == 0) { CheckResult = true;}
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

	//*********************** ..:: onBackPressed ::.. ************************//
	/**
	 * Added in API level 5
	 * Called when the activity has detected the user's press of the back key. The default implementation simply finishes the current activity, but you can override this to do whatever you want. 
	 *
	 * This method called when we want to ask the user if he want to leave the application.
	 * 
	 * @since           1.0
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
