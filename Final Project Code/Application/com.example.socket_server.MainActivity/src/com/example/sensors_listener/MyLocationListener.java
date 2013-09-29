package com.example.sensors_listener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.content.Context;

/**
 * MyLocationListener is a class that implements LocationListener
 * and gives us access to the phone GPS sensor values.
 * 
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     %I%, %G%
 * @since       1.0
 */
public class MyLocationListener implements LocationListener {

	public static double[] values = {0.0f,0.0f,0.0f};
	public int speed = 0;
	public static String   cityName=null;
	public Context mContext;

	//******************* ..:: MyLocationListener ::.. **********************//
	/** 
	 * MyLocationListener constructor initialize class variables.
	 *
	 * @param context     
	 * @since           1.0
	 */		
	public MyLocationListener (Context context) {
		super();
		mContext = context;
	}
	//************************************************************************//    

	//****************** ..:: getLocationAttributes ::.. *********************//
	/** 
	 * getLocationAttributes method returns the sensor values.
	 *
	 * @return double[]  array with the sensor values.       
	 * @since           1.0
	 */	
	public double[] getLocationAttributes(){
		return values;
	}    
	//************************************************************************//    

	//******************* ..:: getLocationCityName ::.. **********************//
	/** 
	 * getLocationCityName method returns the city name.
	 *
	 * @return string with the city name.       
	 * @since           1.0
	 */	
	public String getLocationCityName(){
		return cityName;
	}    
	//************************************************************************//    

	//************************** ..:: getSpeed ::.. **************************//
	/** 
	 * getSpeed method returns the current speed.
	 *
	 * @return int with the speed values.       
	 * @since           1.0
	 */	
	public int getSpeed(){
		return speed;
	}    
	//************************************************************************//    

	//********************* ..:: onLocationChanged ::.. **********************//
	/**
	 * Added in API level 1
	 * Called when the location has changed. 
	 * There are no restrictions on the use of the supplied Location object.
	 *
	 * This method store the GPS coordinates,speed and city name in the public variable. 
	 *
	 * @param loc  The new location, as a Location object.  
	 * @since           1.0
	 */
	public void onLocationChanged(Location loc) {

		values[0] = loc.getLatitude();
		values[1] = loc.getLongitude();
		values[2] = loc.getAltitude();
		speed = (int)((loc.getSpeed()*3600)/1000);


		/*----------to get City-Name from coordinates ------------- */
		Geocoder gcd = new Geocoder(mContext, Locale.getDefault());      		     
		List<Address>  addresses;  
		try {  
			addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);  
			if (addresses.size() > 0)  
				cityName=addresses.get(0).getLocality();  
		} catch (IOException e) {    		      
			e.printStackTrace();  
		} 
	}
	//************************************************************************//    

	//******************* ..:: onProviderDisabled ::.. ***********************//
	/**
	 * Added in API level 1
	 * Called when the provider is disabled by the user. If requestLocationUpdates is called on an already disabled provider, this method is called immediately.
	 *
	 * @param provider  the name of the location provider associated with this update.  
	 * @since           1.0
	 */
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub        	
	}
	//************************************************************************//    

	//******************** ..:: onProviderEnabled ::.. ***********************//
	/**
	 * Added in API level 1
	 * Called when the provider is enabled by the user.
	 *
	 * @param provider  the name of the location provider associated with this update.  
	 * @since           1.0
	 */
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub        	
	}
	//************************************************************************//    

	//********************* ..:: onStatusChanged ::.. ************************//
	/**
	 * Added in API level 1
	 * Called when the provider status changes. This method is called when a provider is unable to fetch a location or if the provider has recently become available after a period of unavailability.
	 *
	 * @param provider  the name of the location provider associated with this update. 
	 * @param status  OUT_OF_SERVICE if the provider is out of service, and this is not expected to change in the near future; TEMPORARILY_UNAVAILABLE if the provider is temporarily unavailable but is expected to be available shortly; and AVAILABLE if the provider is currently available. 
	 * @param extras  an optional Bundle which will contain provider specific status variables. 
	 * A number of common key/value pairs for the extras Bundle are listed below. Providers that use any of the keys on this list must provide the corresponding value as described below. 
	 *
	 * satellites - the number of satellites used to derive the fix  
	 * @since           1.0
	 */
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub        	
	}
	//************************************************************************//    
}