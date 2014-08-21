package edu.pitt.sis.paws.dbsync;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class updateEventSessionsFromAdminForCN3 {
	public static void main(String[] args) throws IOException {
		

	        int i=0;
	        ConnectHalleyDB conn = new ConnectHalleyDB();
	        //String sql = "select cl.linkURL,cl.contentID from contentlink cl JOIN content c ON c.contentID = cl.contentID and c.conferenceID = 98 and cl.userID in (100000963,100001234,100001479)";
	        String sql = "select `mea`.`testingEventSessionID` AS `testingEventSessionID`,`ae`.`displayingRow` AS `displayingRow`,`ae`.`columnOrder` AS `columnOrder` from (`admin_eventSession` `ae` join `mapeventsession_admin` `mea` on(((`mea`.`adminEventSessionID` = `ae`.`id`) and (`mea`.`adminConferenceID` = 109))))";

	        ResultSet rs = conn.getResultSet(sql);

	       
				try {
					while(rs.next()){
						String testingEventSessionID = rs.getString("testingEventSessionID");
						String displayingRow = rs.getString("displayingRow");
						String columnOrder = rs.getString("columnOrder");
						
						//DateFormat date = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
						String eventSql = null;
						eventSql = "UPDATE eventsession SET displayingRow=?, columnOrder = ? where eventSessionID = ?";
						PreparedStatement insertEventpstmt = conn.conn.prepareStatement(eventSql); 
						insertEventpstmt.setString(1,displayingRow);
						insertEventpstmt.setString(2,columnOrder);
						insertEventpstmt.setString(3,testingEventSessionID);

						String insertEventSql = insertEventpstmt.toString();
			        	System.out.println(insertEventSql);

			        	insertEventpstmt.executeUpdate();
			        	insertEventpstmt.close();

						
						
						
						
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

	     
	        


	        
	    }

	    private static void print(String msg, Object... args) {
	        System.out.println(String.format(msg, args));
	    }

	    private static String trim(String s, int width) {
	        if (s.length() > width)
	            return s.substring(0, width-1) + ".";
	        else
	            return s;
		
	}
}
