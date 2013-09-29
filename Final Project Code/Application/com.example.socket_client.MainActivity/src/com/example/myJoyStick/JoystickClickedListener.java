package com.example.myJoyStick;

/**
 * 
 * JoystickClickedListener interface.
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */
public interface JoystickClickedListener {
	//************************* ..:: OnClicked ::.. **************************//
	/**
	 * Called when the user clicked the joystick.
	 * 
	 * @since       1.0
	 */
	public void OnClicked();
	//************************************************************************//
	
	//************************** ..:: OnReleased ::.. ************************//
	/**
	 * Called when the user released the joystick.
	 * 
	 * @since       1.0
	 */
	public void OnReleased();
	//************************************************************************//    
}
