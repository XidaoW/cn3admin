package edu.pitt.sis.paws.dbsync;

import java.sql.*;

public class SyncPresentation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int conferenceID = 127;
		int i =0;
		
		ConnectHalleyDB conn = new ConnectHalleyDB();
		String sql = "DELETE p FROM presentation p JOIN eventsession e ON p.eventsessionID=e.eventsessionID WHERE e.eventID=" + conferenceID;
		conn.executeUpdate(sql);
		
		sql = "SELECT e.eventSessionID, e.eventID, e.sessionName,e.sessionDate,e.beginTime,e.endTime,e.location,e.displayingRow,e.columnOrder FROM cn3Testing_new_dump.eventsession e where e.eventID =" + conferenceID;
		ResultSet rs = conn.getResultSet(sql);
		try {
			while(rs.next()){
				String eventSessionID = rs.getString("eventSessionID");
				String eventID = rs.getString("eventID");
				String sessionName = rs.getString("sessionName");
				String sessionDate = rs.getString("sessionDate");
				String beginTime = rs.getString("beginTime");
				String endTime = rs.getString("endTime");
				String location = rs.getString("location");
				String displayingRow = rs.getString("displayingRow");
				String columnOrder = rs.getString("columnOrder");
			
				
				sql = "INSERT INTO eventsession (eventID,sessionName, sessionDate,beginTime,endTime,location,displayingRow,columnOrder) VALUES " + "(?,?,?,?,?,?,?,?)";
				System.out.println(sql);
				PreparedStatement pstmt = conn.conn.prepareStatement(sql);
				pstmt.setString(1, eventID);
				pstmt.setString(2, sessionName);
				pstmt.setString(3, sessionDate);
				pstmt.setString(4, beginTime);
				pstmt.setString(5, endTime);
				pstmt.setString(6, location);
				pstmt.setString(7, displayingRow);
				pstmt.setString(8, columnOrder);
				pstmt.executeUpdate();
				
				sql = "SELECT LAST_INSERT_ID()";
				ResultSet rss = conn.getResultSet(sql);
				if(rss.next()){
					String eventProductionID = rss.getString(1);
					sql = "INSERT INTO mapeventsession (testingID, productionID) VALUES "+"(?,?)";
					System.out.println(sql);
					pstmt = conn.conn.prepareStatement(sql);
					pstmt.setString(1, eventSessionID);
					pstmt.setString(2, eventProductionID);
					pstmt.executeUpdate();

					System.out.println(sql);
//					sql = "INSERT INTO presentation (presentationDate,beginTime,endTime,contentID,eventSessionID,track,presentationType) " +
//							"SELECT p.presentationDate,p.beginTime,p.endTime,mc.productionID,"+eventProductionID+",p.track,p.presentationType " +
//							"FROM cn3Testing_new_dump.presentation p JOIN mapcontent mc ON p.contentID=mc.testingID " +
//							"JOIN mapeventsession me ON p.eventsessionID=me.testingID;";
//					System.out.println(sql);
//					i++;
//					System.out.println(i);
//
//					conn.executeUpdate(sql);
					
//					Run the sql in mysql query console
//					INSERT INTO presentation (presentationDate,beginTime,endTime,contentID,eventSessionID,track,presentationType)
//					SELECT p.presentationDate,p.beginTime,p.endTime,mc.productionID,me.productionID,p.track,p.presentationType
//					FROM cn3Testing_new_dump.presentation p JOIN mapcontent mc ON p.contentID=mc.testingID
//					JOIN mapeventsession me ON p.eventsessionID=me.testingID;
					
				}
				
				
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Need to create mapcontent and mapeventsession first
		

	}

}
