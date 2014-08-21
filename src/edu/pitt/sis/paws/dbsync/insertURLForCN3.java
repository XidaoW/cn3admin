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

public class insertURLForCN3 {
	public static void main(String[] args) throws IOException {
		

	        int i=0;
	        ConnectHalleyDB conn = new ConnectHalleyDB();
	        //String sql = "select cl.linkURL,cl.contentID from contentlink cl JOIN content c ON c.contentID = cl.contentID and c.conferenceID = 98 and cl.userID in (100000963,100001234,100001479)";
	        String sql = "SELECT * FROM cn3Testing_new_dump.content c WHERE c.conferenceID = 97 and c.acmlink is not null";

	        ResultSet rs = conn.getResultSet(sql);

	       
				try {
					while(rs.next()){
						String linkURL = rs.getString("acmlink");
						String contentID = rs.getString("contentID");
						linkURL = linkURL.replaceAll("&CFID=274779637&CFTOKEN=44349477", "");
						//DateFormat date = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
						String eventSql = null;
						eventSql = "UPDATE content SET acmlink=? where contentID = ?";
						PreparedStatement insertEventpstmt = conn.conn.prepareStatement(eventSql); 
						insertEventpstmt.setString(1,linkURL);
						insertEventpstmt.setString(2,contentID);

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
