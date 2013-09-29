package il.co.server;

import java.sql.*;

/**
 * ConnectionManagerST is a class that handle a single tone connection to the mySQL server database. 
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */
public class  ConnectionManagerST {

	//.: DB connection manager variables.
	private static ConnectionManagerST mySelf = null;
	private static Connection conn = null;
	//===========================

	//******************* ..:: ConnectionManagerST ::.. *********************// 
	/** 
	 * Added in API level 1
	 * 
	 * constructor initialize and get connection to the database. 
	 * 
	 * @since           1.0
	 */	    
	private ConnectionManagerST(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/aauavdb?user=pass&password=pass");
		} catch (ClassNotFoundException e){
			System.out.println("connection faild");
			System.out.println(e.toString());
		} catch (SQLException e){
			System.out.println("connection faild");
			System.out.println(e.toString());
		}	// try and catch
		System.out.println("connection succeed");
	}
	//************************************************************************//   

	//******************* ..:: ConnectionManagerST ::.. **********************// 
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want the connection manager object. 
	 * 
	 * @since           1.0
	 */	    
	public static ConnectionManagerST getInstance(){
		if (mySelf == null)
			mySelf = new ConnectionManagerST();
		return mySelf;
	} 
	//************************************************************************//   

	//*********************** ..:: getConnection ::.. ************************// 
	/** 
	 * Added in API level 1
	 * 
	 * called when we want the DB connection.
	 * 
	 * @since           1.0
	 */	    
	public Connection getConnection(){
		return conn;
	}	// get connection
	//************************************************************************//   

	//************************* ..:: disconnect ::.. *************************// 
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to disconnect from the DB.
	 * 
	 * @since           1.0
	 */	    
	public static void disconnect(){
		try {
			conn.close();
			conn = null;
			mySelf = null;
		} catch (SQLException e) {
			e.printStackTrace();
		};
	}
	//************************************************************************//   
}	
