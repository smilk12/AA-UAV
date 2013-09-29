package il.co.server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;

/**
 * Controller is a class that manage the massages transfer between the user application and the plane application
 * Along with that the controller also transfer specific massages about the plane information to the mySQL database. 
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */
public class Controller {

	//.: Flag variables.
	public String value; 
	public static int upFlag = 0;
	public static int downFlag = 0;
	public static int forwardFlag = 0;
	public static int backwardFlag = 0;
	public static int rightFlag = 0;
	public static int leftFlag = 0;
	public static int rrightFlag = 0;
	public static int rleftFlag = 0;
	public static int takeoffFlag = 0;
	public static int lendFlag = 0;
	public static int autoFlight = 0;
	public static int emergencyFlag = 0;
	public static int stopAutoFFlag = 0;
	public static int SensorsFlag = 0;
	public static int serialVerificationFlag = 0;
	//===========================

	//.: String variables.
	public static String Sensors = "";
	public static String destenation = "";
	public static String speed = "";
	public static String distance = "";
	public static String[] destpoint = {"0.0","0.0"};
	public static String serial = "";
	public static String serialVerification = "";
	//===========================

	//.: View variables.
	public JLabel AccX;
	public JLabel AccY;
	public JLabel AccZ;
	public JLabel gpsLat;
	public JLabel gpsLon;
	public JLabel gpsAlt;
	public JLabel gpsCity;	
	public JLabel compX;	
	public JLabel compY;	
	public JLabel compZ;	
	public JLabel battL;	
	public JLabel battV;	
	public JLabel battS;	
	//===========================


	//.: Database variables.
	public ActivityDBManager db = new ActivityDBManager();
	//===========================

	//************************ ..:: Controller ::.. *************************// 
	/** 
	 * Added in API level 1
	 * 
	 * constructor 
	 * 
	 * @since           1.0
	 */	    
	public Controller(){		
	}
	//************************************************************************//   

	//************************* ..:: Initialize ::.. *************************// 
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to initialize all of the view variables in the controller.
	 * 
	 * @param AccX - Accelerometer X JLabel
	 * @param AccY - Accelerometer Y JLabel
	 * @param AccZ - Accelerometer Z JLabel
	 * @param gpsLat - GPS Latitude JLabel
	 * @param gpsLon - GPS Longitude JLabel
	 * @param gpsAlt - GPS Altitude  JLabel
	 * @param gpsCity - GPS City JLabel
	 * @param compX - Compass Yaw JLabel
	 * @param compY - Compass Roll JLabel
	 * @param compZ - Compass Pitch JLabel
	 * @param battL - Battery Level JLabel
	 * @param battV - Battery Status JLabel
	 * @param battS - Battery Voltage JLabel	 	
	 * 
	 * @since           1.0
	 */	    
	public void Initialize(JLabel AccX,JLabel AccY,JLabel AccZ,JLabel gpsLat,JLabel gpsLon,JLabel gpsAlt,JLabel gpsCity,JLabel compX,JLabel compY,JLabel compZ,JLabel battL,JLabel battV,JLabel battS) throws IOException
	{
		this.AccX = AccX;
		this.AccY = AccY;
		this.AccZ = AccZ;
		this.gpsLat = gpsLat;
		this.gpsLon = gpsLon;
		this.gpsAlt = gpsAlt;
		this.gpsCity = gpsCity;
		this.compX = compX;
		this.compY = compY;
		this.compZ = compZ;
		this.battL = battL;
		this.battV = battV;
		this.battS = battS;
	}	
	//************************************************************************//   

	//************************** ..:: getAction ::.. *************************// 
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to get the right response form the client request.
	 * The clients are the user and plane applications.
	 * 
	 * @since           1.0
	 */	    
	public String getAction(String msg) throws SQLException{

		//------------------------------------------ User Implementation ---------------------------------------//
		if (msg.contains("User:") == true){
			value = msg.substring(5);

			//Change to switch()
			if(value.compareTo("Up")       == 0) {upFlag = 1;       value = "up";       return value;}
			if(value.compareTo("Down")     == 0) {downFlag = 1;     value = "down";     return value;}
			if(value.compareTo("Forward")  == 0) {forwardFlag = 1;  value = "forward";  return value;}
			if(value.compareTo("Backward") == 0) {backwardFlag = 1; value = "backward"; return value;}
			if(value.compareTo("Right")    == 0) {rightFlag = 1;    value = "right";    return value;}
			if(value.compareTo("Left")     == 0) {leftFlag = 1;     value = "left";     return value;}
			if(value.compareTo("R_Left")   == 0) {rleftFlag = 1;    value = "rleft";    return value;}
			if(value.compareTo("R_Right")  == 0) {rrightFlag = 1;   value = "rright";   return value;}
			if(value.compareTo("Take_off") == 0) {takeoffFlag = 1;  value = "takeoff";  return value;}
			if(value.compareTo("Lend")     == 0) {lendFlag = 1;     value = "lend";     return value;}
			if(value.compareTo("Emergency")== 0) {emergencyFlag = 1;value = "emergency";return value;}
			if(value.compareTo("stopAutoF")== 0) {stopAutoFFlag = 1;value = "stopautof";return value;}

			if(value.compareTo("CheckServerConnection")     == 0) {value = "ConnectionOK";     return value;}

			if(value.compareTo("getSensors")     == 0)
			{
				if(SensorsFlag == 1)
				{
					SensorsFlag = 0;
					//				   value = Sensors;
					value = speed + "," + gpsAlt.getText() + "," + compX.getText() + "," + compY.getText() + "," + compZ.getText();
					Sensors = "";
					return value;
				}
			}

			if(value.contains("SerialVerification") == true)
			{
				Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(msg);
				m.find();  serialVerification = "" + m.group(1);
				System.out.println(serialVerification);

				if(serialVerification != ""){
					if(db.getActivity(serialVerification) == false){
						serialVerificationFlag = 0;
					}else{
						serialVerificationFlag = 1;	         		   
					}	            	   
				}

				if(serialVerificationFlag == 0) {value = "SerialNotOK";}
				if(serialVerificationFlag == 1) {serialVerificationFlag = 0; serialVerification = ""; value = "SerialOK";}


				return value;
			}

			if(value.contains("lat") == true){
				System.out.println(value);		
				autoFlight = 1;               
				destenation = value.substring(10,value.length()-1);
				destpoint = destenation.split("\\s*,\\s*");
				System.out.println(destenation);
				//add distane and change what need in the ativitydbmanager.java
				//in the user application split the sensor data and insert distance and speed to the scrollers
				db.upsertActivity(serial,gpsCity.getText(),gpsAlt.getText(),speed,gpsLat.getText(),gpsLon.getText(),destpoint[0],destpoint[1],distance,distance,"0",battL.getText(),"gamer","on");
			}


			return "none1";
		}
		//------------------------------------------------------------------------------------------------------//

		//----------------------------------------- Plane Implementation ---------------------------------------//
		if (msg.contains("Plane:") == true){
			value = msg.substring(6);			

			if(value.contains("Sensors") == true)
			{
				//			   value = msg.substring(19);
				Sensors = value;
				SensorsFlag = 1;
				Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(msg);
				m.find();  AccX.setText(m.group(1));
				m.find();  AccY.setText(m.group(1));
				m.find();  AccZ.setText(m.group(1));
				m.find();  compX.setText(m.group(1));
				m.find();  compY.setText(m.group(1));
				m.find();  compZ.setText(m.group(1));
				m.find();  gpsLat.setText(m.group(1));
				m.find();  gpsLon.setText(m.group(1));
				m.find();  gpsAlt.setText(m.group(1));
				m.find();  gpsCity.setText(m.group(1));
				m.find();  battL.setText(m.group(1));
				m.find();  battV.setText(m.group(1));
				m.find();  if(m.group(1).compareTo("2") == 0) battS.setText("USB Plugged"); else battS.setText("USB UnPlugged"); 
				m.find();  speed = m.group(1);
				m.find();  distance = m.group(1);
				m.find();  serial = m.group(1);

//				if(gpsLat.getText().compareTo("0.0") != 0)
				db.upsertActivity(serial,gpsCity.getText(),gpsAlt.getText(),speed,gpsLat.getText(),gpsLon.getText(),destpoint[0],destpoint[1],distance,distance,"0",battL.getText(),"gamer","on");


				return value;
			}

			if(value.compareTo("check") == 0) 
			{
				if(upFlag        == 1) {upFlag = 0;        value = "up";        return value;}
				if(downFlag      == 1) {downFlag = 0;      value = "down";      return value;}
				if(forwardFlag   == 1) {forwardFlag = 0;   value = "forward";   return value;}
				if(backwardFlag  == 1) {backwardFlag = 0;  value = "backward";  return value;}
				if(rightFlag     == 1) {rightFlag = 0;     value = "right";     return value;}
				if(leftFlag      == 1) {leftFlag = 0;      value = "left";      return value;}
				if(rleftFlag     == 1) {rleftFlag = 0;     value = "rleft";     return value;}
				if(rrightFlag    == 1) {rrightFlag = 0;    value = "rright";    return value;}
				if(takeoffFlag   == 1) {takeoffFlag = 0;   value = "takeoff";   return value;}
				if(lendFlag      == 1) {lendFlag = 0;      value = "lend";      return value;}
				if(autoFlight    == 1) {autoFlight = 0;    value = destenation; return value;}
				if(emergencyFlag == 1) {emergencyFlag = 0; value = "emergency"; return value;}
				if(stopAutoFFlag == 1) {stopAutoFFlag = 0; value = "stopautof"; return value;}			   
			}

			if(value.compareTo("CheckServerConnection")     == 0) {value = "ConnectionOK";     return value;}

			return "none2";
		}

		return "none";
		//------------------------------------------------------------------------------------------------------//
	}
	//************************************************************************//   
}
