package com.example.sensors_listener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * MyCompassListener is a class that implements SensorEventListener
 * and gives us access to the phone Compass sensor values.
 * 
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     %I%, %G%
 * @since       1.0
 */
public class MyCompassListener implements SensorEventListener{

	//.: Sensors variables.
	private SensorManager mSensorManager;
	private Sensor mSensor;
	public static float[] values = {0.0f,0.0f,0.0f};
	//===========================

	//********************** ..:: MyCompassListener ::.. ********************//
	/** 
	 * MyCompassListener constructor initialize class variables.
	 *
	 * @param sensor the sensor service.    
	 * @see             SensorManager.
	 * @since           1.0
	 */		
	public MyCompassListener(SensorManager sensor){
		mSensorManager = sensor;
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	}
	//************************************************************************//    

	//************************ ..:: getCompass ::.. **************************//
	/** 
	 * getCompass method returns the sensor values.
	 *
	 * @return float[]  array with the sensor values.       
	 * @since           1.0
	 */	
	public float[] getCompass(){
		return values;
	}
	//************************************************************************//    

	//*********************** ..:: startSimulation ::.. **********************//
	/** 
	 * startSimulation method starts the sensor listening.
	 *
	 * @since           1.0
	 */	
	public void startSimulation() {
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
		//		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_FASTEST);
	}
	//************************************************************************//    

	//************************ ..:: stopSimulation ::.. **********************//
	/** 
	 * stopSimulation method stops the sensor listening.
	 *
	 * @since           1.0
	 */	
	public void stopSimulation() {
		mSensorManager.unregisterListener(this);
	}
	//************************************************************************//    

	//********************** ..:: onAccuracyChanged ::.. *********************//
	/**
	 * Added in API level 3
	 * Called when the accuracy of a sensor has changed. 
	 * See SensorManager for details.
	 *
	 * @param accuracy  The new accuracy of this sensor  
	 * @since           1.0
	 */
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
	//************************************************************************//    

	//********************** ..:: onSensorChanged ::.. ***********************//
	/**
	 * Added in API level 3
	 * Called when sensor values have changed. 
	 * See SensorManager for details on possible sensor types. 
	 * See also SensorEvent. 
	 *
	 * NOTE: The application doesn't own the event object passed as a parameter and therefore cannot hold on to it. The object may be part of an internal pool and may be reused by the framework.
	 * 
	 * onSensorChanged method stores the sensor values in a public float array variable.
	 *
	 * @param event  the SensorEvent.  
	 * @since           1.0
	 */
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		values[0] = event.values[0];
		values[1] = event.values[1];
		values[2] = event.values[2];		
	}
	//************************************************************************//    
}
