package com.example.socket_client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;



import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * SerialVerification is a class that verify that the user application is connected to the right drone.
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */
public class SerialVerification extends Activity {

	//.: Connection variables.
	public static String ipaddress = "46.121.229.158";
	public static int port = 5555;
	public Socket mySkt;
	public PrintStream myPS;
	public BufferedReader myBR;
	//===========================

	//.: View variables.
	public TextView textDisplay1;    
	public TextView textDisplay2;    
	public TextView textDisplay3;    
	public ProgressBar progBar1;
	public EditText editText1;
	public Button b1;
	public Button b2;
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
	 * This method starts the initComponents method and createEvants method.   
	 * 
	 * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
	 * @since           1.0
	 */	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_serial_verification);

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
		textDisplay1 = (TextView)    this.findViewById(R.id.textView1);
		textDisplay2 = (TextView)    this.findViewById(R.id.textView2);
		textDisplay3 = (TextView)    this.findViewById(R.id.textView3);
		progBar1     = (ProgressBar) this.findViewById(R.id.progressBar1);
		editText1     = (EditText) this.findViewById(R.id.editText1);
	}
	//************************************************************************//   

	//********************* ..:: Create Events ::.. **************************// 
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to start the activity events that runs in background.
	 * This method start a runOnUiThread that update the UI view. 
	 * This method handle all of the button event.  
	 * 
	 * 
	 * @since           1.0
	 */	    
	public void createEvents(){
		b1 = (Button)findViewById(R.id.button1);    	
		b2 = (Button)findViewById(R.id.button2);    	

		runOnUiThread(new Runnable() {
			public void run() {
				b2.setVisibility(View.INVISIBLE);
				progBar1.setVisibility(View.INVISIBLE);
				textDisplay2.setVisibility(View.INVISIBLE);
				textDisplay3.setVisibility(View.INVISIBLE);
			}
		});

		b1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				runOnUiThread(new Runnable() {
					public void run() {
						b1.setVisibility(View.INVISIBLE);
						editText1.setVisibility(View.INVISIBLE);
						progBar1.setVisibility(View.VISIBLE);
					}
				});
				CheckResult = false;
				startChecking();	              
			}
		});                  	

		b2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				runOnUiThread(new Runnable() {
					public void run() {
						textDisplay2.setVisibility(View.INVISIBLE);
						textDisplay3.setVisibility(View.INVISIBLE);
						b2.setVisibility(View.INVISIBLE);
						b1.setVisibility(View.VISIBLE);
						editText1.clearComposingText();
						editText1.setVisibility(View.VISIBLE);      			             
					}
				});
			}
		});                  	

	}
	//************************************************************************//   

	//********************* ..:: StartChecking ::.. **************************// 
	/** 
	 * Added in API level 1
	 * startChecking method checks if the serial number that we got from the plane application 
	 * is correct and stored in the server database, if it is then we transfer to the next activity,
	 * if not we get a button to check again.
	 *
	 * @since           1.0
	 */	
	public void startChecking(){    	
		new Thread(new Runnable() {

			public void run() {
				try {						
					//----------------------------- Checking Server Connection ------------------------------// 
					if(CheckResult == false){
						myEditValue = "User:SerialVerification(" + editText1.getText().toString() + ")";
						new massege().execute("4");
						Thread.sleep(4000);
					}

					if(CheckResult == true){
						runOnUiThread(new Runnable() {
							public void run() {
								progBar1.setVisibility(View.INVISIBLE);
								textDisplay3.setText("Serial:" + editText1.getText().toString() + " Found");		        		         
								textDisplay3.setVisibility(View.VISIBLE);
							}
						});
						Thread.sleep(2000);
						finish();                 
						startActivity(new Intent("com.example.socket_client.controllerscreen.ControllerScreen"));
					}else{	
						runOnUiThread(new Runnable() {
							public void run() {
								progBar1.setVisibility(View.INVISIBLE);
								textDisplay3.setText("Serial:" + editText1.getText().toString() + " Not Found");		        		         
								textDisplay3.setVisibility(View.VISIBLE);
								b2.setVisibility(View.VISIBLE);
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
			if(result.compareTo("SerialOK") == 0) { CheckResult = true;}
		}
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.serial_verification, menu);
		return true;
	}
	//************************************************************************//  
}
