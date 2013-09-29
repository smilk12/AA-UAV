package com.example.myJoyStick;

/**
 * 
 * JoystickMovedListener interface.
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */
public interface JoystickMovedListener {
	//************************** ..:: OnMoved ::.. ***************************//
	/**
	 * Called when the user moved the joystick.
	 * 
	 * @param pan 
	 * @param tilt 
	 * @since       1.0
	 */
	public void OnMoved(int pan, int tilt);
	//************************************************************************//
	
	//************************** ..:: OnReleased ::.. ************************//
	/**
	 * Called when the user released the joystick.
	 * 
	 * @since       1.0
	 */
	public void OnReleased();
	//************************************************************************//
	
	//******************** ..:: OnReturnedToCenter ::.. **********************//
	/**
	 * Called when the joystick returned to the center.
	 * 
	 * @since       1.0
	 */
	public void OnReturnedToCenter();
	//************************************************************************//    
}
