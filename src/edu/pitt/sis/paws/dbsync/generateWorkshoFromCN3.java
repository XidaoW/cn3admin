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

public class generateWorkshoFromCN3 {
	public static void main(String[] args) throws IOException {
		

	        int i=0;
	        ConnectHalleyDB conn = new ConnectHalleyDB();
	        String sql = "select * from event where parentEventID = 98";
			ResultSet rs = conn.getResultSet(sql);

	       
				try {
					while(rs.next()){
						//String contentID = rs.getString("contentID");
						String title = rs.getString("title");
						String beginTime = rs.getString("beginDate");
						String endTime = rs.getString("endDate");
						String location = rs.getString("location");
						String sessionName = "Workshop -- " + title;
						//DateFormat date = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
						String eventSql = null;
						eventSql = "INSERT INTO eventsession " +
								"(sessionName,eventID,beginTime,endTime,location) " +
								"VALUES (?,98,?,?,?)";	        
						PreparedStatement insertEventpstmt = conn.conn.prepareStatement(eventSql);
						insertEventpstmt.setString(1,sessionName);
						insertEventpstmt.setString(2,beginTime);
						insertEventpstmt.setString(3,endTime);
						insertEventpstmt.setString(4,location);
						String insertEventSql = insertEventpstmt.toString();
			        	//System.out.println(insertEventSql);

			        	insertEventpstmt.executeUpdate();
			        	insertEventpstmt.close();
						String sqlID = null;
						sqlID = "SELECT LAST_INSERT_ID()";//"SELECT " + paperID;//
						//paperID++;
						ResultSet rsContent = conn.getResultSet(sqlID);
						String eventSessionID = null;
						if(rsContent.next()){
							eventSessionID = rsContent.getString(1);
							System.out.println("eventSessionID: " + eventSessionID);
						}
						String ContentSql = null;
						ContentSql = "SELECT contentID FROM `content` where contentType = 'no-paper' and title like '%"+title+"%'";
						ResultSet rsContentID = conn.getResultSet(ContentSql);
			        	//System.out.println(ContentSql);

						while(rsContentID.next()){
							String contentID = null;

							contentID = rsContentID.getString(1);
							System.out.println("contentID: " + contentID);
							String presentSql = null;
							
							presentSql = "INSERT INTO presentation " +
									"(beginTime,endTime,contentID,eventSessionID,presentationType) " +
									"VALUES (?,?,?,?,?)";	 
							PreparedStatement insertPresentpstmt = conn.conn.prepareStatement(eventSql);

							insertPresentpstmt = conn.conn.prepareStatement(presentSql);
							insertPresentpstmt.setString(1,beginTime);
							insertPresentpstmt.setString(2,endTime);
							insertPresentpstmt.setString(3,contentID);
							insertPresentpstmt.setString(4,eventSessionID);
							insertPresentpstmt.setString(5,"Workshop");

							String insertPSql = insertPresentpstmt.toString();
				        	System.out.println(insertPSql);

				        	insertPresentpstmt.executeUpdate();
				        	insertPresentpstmt.close();
							
						}
						
						
						
						
						
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
