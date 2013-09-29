<?php

		    $ipAddress = "localhost";
		    $dbName = "aauavdb";
		    $userName = "pass";
		    $password ="pass";
		    $tableName ="droneinfo";
		    $fields = "droneid,city,Height,speed,startpointx,startpointy,currentpointx,currentpointy,endpointx,endpointy,distance,distancemade,dbattery,pbattery,dtype,status";
//		    $condition = "WHERE status = 'on' AND dtype != 'police'";
		    $condition = "WHERE status = 'on'";
		    $tokens = " , ";

			$con = mysql_connect($ipAddress,$userName,$password);
			if (!$con) { die('Could not connect: ' . mysql_error()); }
 
			mysql_select_db($dbName, $con);


            $sql = "SELECT " . $fields . " FROM " . $tableName . " " . $condition;  
			$result = mysql_query($sql);

			$arr = explode($tokens,$fields);
			
	   
			while($row = mysql_fetch_array($result))
			{
			     $dronearr[] = array('droneid' => $row['droneid'],
			     	                 'city' =>  $row['city'], 
			     	                 'hight' => $row['Height'], 
			     	                 'speed' => $row['speed'], 
			     	                 'stcordinationx' => $row['startpointx'],
			     	                 'stcordinationy' => $row['startpointy'],
			     	                 'cucordinationx' => $row['currentpointx'],
			     	                 'cucordinationy' => $row['currentpointy'],
			     	                 'decordinationx' => $row['endpointx'],
			     	                 'decordinationy' => $row['endpointy'],
			     	                 'distance' => $row['distance'],
			     	                 'distancemade' => $row['distancemade'], 
			     	                 'dbattery' => $row['dbattery'], 
			     	                 'pbattery' => $row['pbattery'], 
			     	                 'dronetype' => $row['dtype'], 
			     	                 'dronestatus' => $row['status']); 
            }
		
			mysql_close($con);
			$myarr = $dronearr;
	        echo json_encode($myarr);
?>