package com.example.socket_client;


import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * SplashScreen is a class that display the application splash screen.
 * splash screen is the first screen of the application that displays the
 * application trademark with a little animation.  
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */
public class SplashScreen extends Activity {

	//********************* ..:: onAttachedToWindow ::.. *********************//
	/** 
	 * Added in API level 5
	 * 
	 * Called when the main window associated with the activity has been attached to the window manager.
	 * See View.onAttachedToWindow() for more information.
	 * 
	 */		
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
	//************************************************************************//    

	//*************************** ..:: onCreate ::.. *************************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when the activity is starting. This is where most initialization should go: calling setContentView(int) to inflate the activity's UI, using findViewById(int) to programmatically interact with widgets in the UI, calling managedQuery(android.net.Uri, String[], String, String[], String) to retrieve cursors for data being displayed, etc. 	
	 * You can call finish() from within this function, in which case onDestroy() will be immediately called without any of the rest of the activity lifecycle (onStart(), onResume(), onPause(), etc) executing. 
	 * Derived classes must call through to the super class's implementation of this method. If they do not, an exception will be thrown.
	 * 
	 * This method starts the splash screen animation and running a thread for 5 seconds, when the 5 seconds is over we start the next activity. 
	 * 
	 * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
	 * @since           1.0
	 */	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		StartAnimations();
		// thread for displaying the SplashScreen

		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(5000);
					//Thread.yield();
					finish();
					startActivity(new Intent("com.example.socket_client.connectiontest.ConnectionTest"));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();        
	}
	//************************************************************************//    

	//********************** ..:: StartAnimations ::.. ***********************// 
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to start the splash screen animation.
	 * This method load and start the animation.
	 * 
	 * @since           1.0
	 */	    
	private void StartAnimations() {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
		anim.reset();
		LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
		l.clearAnimation();
		l.startAnimation(anim);

		anim = AnimationUtils.loadAnimation(this, R.anim.translate);
		anim.reset();
		ImageView iv = (ImageView) findViewById(R.id.logo);
		iv.clearAnimation();
		iv.startAnimation(anim);        

	}
	//************************************************************************//    

	//************************ ..:: onTouchEvent ::.. ************************// 
	/** 
	 * Added in API level 1
	 * 
	 * Called when a touch screen event was not handled by any of the views under it.
	 * This is most useful to process touch events that happen outside of your window bounds, where there is no view to receive it.
	 * 
	 * @param event  The touch screen event being processed. 
	 * @return Return true if you have consumed the event, false if you haven't. The default implementation always returns false.
	 * @since           1.0
	 */	    
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			//   _active = false;
		}
		return true;
	}
	//************************************************************************//    
}
