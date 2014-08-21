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

public class moveURL {
	public static void main(String[] args) throws IOException {
		

	        int i=0;
	        ConnectHalleyDB conn = new ConnectHalleyDB();
	        String sql = "select cl.acmlink,cl.contentID from content cl where cl.conferenceID = 98 and cl.acmlink <> ''";
			ResultSet rs = conn.getResultSet(sql);

	       
				try {
					while(rs.next()){
						String linkURL = rs.getString("acmlink");
						String contentID = rs.getString("contentID");
						
						//DateFormat date = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
						String eventSql = null;
						eventSql = "INSERT INTO contentlink (contentID,linkURL,linkCaption,type,userID,time) VALUES (?,?,\"PDF\",\"Paper\",\"100000963\",NOW())";
						PreparedStatement insertEventpstmt = conn.conn.prepareStatement(eventSql); 
						insertEventpstmt.setString(1,contentID);
						insertEventpstmt.setString(2,linkURL);

						String insertEventSql = insertEventpstmt.toString();
			        	System.out.println(insertEventSql);

			        	insertEventpstmt.executeUpdate();
			        	
			        	insertEventpstmt.close();

						eventSql = "UPDATE content SET acmlink='', DOI ='', contentlink = '' where contentID = ?";
						insertEventpstmt = conn.conn.prepareStatement(eventSql); 
						insertEventpstmt.setString(1,contentID);
						insertEventSql = insertEventpstmt.toString();

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
