package il.co.server;

import java.sql.*;


/**
 * ActivityDBManager is a class that handle all of the data base activity such as : insert,update,delete.
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */
public class ActivityDBManager{

	//.: DB Activity manager variables.
	Connection con = null;
	PreparedStatement ps = null;
	Statement stmt = null;
	boolean b = false;
	ResultSet rs = null;
	//===========================


	//************************ ..:: ActivityDBManager ::.. *************************// 
	/** 
	 * Added in API level 1
	 * 
	 * constructor 
	 * 
	 * @since           1.0
	 */	    
	public ActivityDBManager(){

	} 
	//************************************************************************//   

	//************************ ..:: insertActivity ::.. *************************// 
	/** 
	 * Added in API level 1
	 * 
	 * Called when we wand to insert new information to the database. 
	 * 
	 * @param serial - Serial that the drone application generated. 
	 * @param gpsCity - GPS City.
	 * @param gpsAlt - GPS Altitude.
	 * @param speed - drone speed.
	 * @param gpsLat - GPS Start Latitude.
	 * @param gpsLon - GPS Start Longitude.
	 * @param gpsLat2 - GPS Current Latitude.
	 * @param gpsLon2 - GPS Current Longitude.
	 * @param gpsLat3 - GPS End Latitude.
	 * @param gpsLon3 - GPS End Longitude.
	 * @param battL - Battery Phone Level.
	 * @param batt - Battery Drone Level.
	 * @param dis1 - distance from start to end point.
	 * @param dis2 - distance from current to end point.
	 * @param type - type of the drone : game,police,transfer.
	 * @param status - drone status on/off.

	 * @since           1.0
	 */	    
	public void insertActivity(String serial,String gpsCity,String speed,String gpsAlt,String gpsLat,String gpsLon,String gpsLat2,String gpsLon2,String gpsLat3,String gpsLon3,String dis1,String dis2,String battL,String batt,String type,String status) throws SQLException {
		con = ConnectionManagerST.getInstance().getConnection();
		try {
			ps = con.prepareStatement("INSERT INTO droneinfo (droneid,city,Height,speed,startpointx,startpointy,currentpointx,currentpointy,endpointx,endpointy,distance,distancemade,dbattery,pbattery,dtype,status)" +
					" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			ps.setString(1,serial);
			ps.setString(2,gpsCity);
			ps.setString(4,gpsAlt);
			ps.setString(3,speed);
			ps.setString(5,gpsLat);
			ps.setString(6,gpsLon);
			ps.setString(7,gpsLat2);
			ps.setString(8,gpsLon2);
			ps.setString(9,gpsLat3);
			ps.setString(10,gpsLon3);
			ps.setString(11,dis1);
			ps.setString(12,dis2);
			ps.setString(13,battL);
			ps.setString(14,batt);
			ps.setString(15,type);
			ps.setString(16,status);
			ps.execute();

		} catch (SQLException e) {
			System.out.println(e.toString());
		} 
		finally {
		}
	}	
	//************************************************************************//   


	//************************ ..:: updateActivity ::.. *************************// 
	/** 
	 * Added in API level 1
	 * 
	 * Called when we wand to update specific information by id in the database. 
	 * 
	 * @param gpsCity - GPS City.
	 * @param gpsAlt - GPS Altitude.
	 * @param speed - drone speed.
	 * @param gpsLat - GPS Start Latitude.
	 * @param gpsLon - GPS Start Longitude.
	 * @param gpsDestLat - GPS End Latitude.
	 * @param gpsDestLon - GPS End Longitude.
	 * @param dis1 - distance from start to end point.
	 * @param dis2 - distance from current to end point.
	 * @param battd - Battery Drone Level.
	 * @param battp - Battery Phone Level.
	 * @param type - type of the drone : game,police,transfer.
	 * @param status - drone status on/off.
	 * @param serial - Serial that the drone application generated. 
	 * 
	 * @since           1.0
	 */	    
	public void updateActivity(String serial,String gpsCity,String speed,String gpsAlt,String gpsLat,String gpsLon,String gpsDestLat,String gpsDestLon,String dis1,String dis2,String battd,String battp,String type,String status) throws SQLException {
		con = ConnectionManagerST.getInstance().getConnection();
		try {
			ps = con.prepareStatement("UPDATE droneinfo SET city = ?,Height = ?,speed = ?,currentpointx = ?,currentpointy = ?,endpointx = ?,endpointy = ?,distance = ?,distancemade = ?,dbattery = ?,pbattery = ?,dtype = ?,status = ?" +
					" WHERE droneid = ?");

			ps.setString(1,gpsCity);
			ps.setString(3,gpsAlt);
			ps.setString(2,speed);
			ps.setString(4,gpsLat);
			ps.setString(5,gpsLon);
			ps.setString(6,gpsDestLat);
			ps.setString(7,gpsDestLon);
			ps.setString(8,dis1);
			ps.setString(9,dis2);
			ps.setString(10,battd);
			ps.setString(11,battp);
			ps.setString(12,type);
			ps.setString(13,status);
			ps.setString(14,serial);
			ps.execute();


		} catch (SQLException e) {
			System.out.println(e.toString());
		} 
		finally {
		}
	} 
	//************************************************************************//   


	//************************ ..:: getActivity ::.. *************************// 
	/** 
	 * Added in API level 1
	 * 
	 * Called when we wand to get information from the database by serial. 
	 * 
	 * @param serial - Serial that the drone application generated. 
	 * @since           1.0
	 */	    
	public boolean getActivity(String serial) throws SQLException {
		con = ConnectionManagerST.getInstance().getConnection();
		boolean b = false;
		try {
			ps = con.prepareStatement("SELECT * FROM droneinfo WHERE droneid = ?");
			ps.setString(1,serial);
			rs = ps.executeQuery();
			rs.next();

			if(rs.getString("droneid") == null){
				b = false;
			}else{
				b = true;
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
		} 
		return b;
	} 
	//************************************************************************//   



	//************************ ..:: upsertActivity ::.. *************************// 
	/** 
	 * Added in API level 1
	 * 
	 * Called when we wand to upsert information to the database.
	 * upsert means that we check by serial if the information is in the database,
	 * if no then he use insert method and if yes then he use the update method.   
	 *
	 * @param serial - Serial that the drone application generated. 
	 * @param gpsCity - GPS City.
	 * @param gpsAlt - GPS Altitude.
	 * @param speed - drone speed.
	 * @param gpsLat - GPS Start Latitude.
	 * @param gpsLon - GPS Start Longitude.
	 * @param gpsDestLat - GPS End Latitude.
	 * @param gpsDestLon - GPS End Longitude.
	 * @param dis1 - distance from start to end point.
	 * @param dis2 - distance from current to end point.
	 * @param battd - Battery Drone Level.
	 * @param battp - Battery Phone Level.
	 * @param type - type of the drone : game,police,transfer.
	 * @param status - drone status on/off.
	 * 
	 * @since           1.0
	 */	    
	public void upsertActivity(String serial,String gpsCity,String gpsAlt,String speed,String gpsLat,String gpsLon,String gpsDestLat,String gpsDestLon,String dis1,String dis2,String battd,String battp,String type,String status) throws SQLException {

		if(getActivity(serial) == false){
			insertActivity(serial,gpsCity,gpsAlt,speed,gpsLat,gpsLon,gpsLat,gpsLon,gpsDestLat,gpsDestLon,dis1,dis2,battd,battp,type,status);
		}else{
			updateActivity(serial,gpsCity,gpsAlt,speed,gpsLat,gpsLon,gpsDestLat,gpsDestLon,dis1,dis2,battd,battp,type,status);
		}

	}	
	//************************************************************************//   
}