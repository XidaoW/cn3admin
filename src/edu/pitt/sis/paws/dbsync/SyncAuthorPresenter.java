package edu.pitt.sis.paws.dbsync;

import java.sql.*;

public class SyncAuthorPresenter {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int conferenceID = 127;

		ConnectHalleyDB conn = new ConnectHalleyDB();

		try {
			/*String sql = "DELETE FROM authorpresenter WHERE contentID IN (SELECT contentID FROM content WHERE conferenceID=" + conferenceID + 
					//" AND contentType='Workshop'" +
					")";
			conn.executeUpdate(sql);*/
			
			String sql = "SELECT c.contentID,a.name,a.address,a.departmentAffiliation,a.universityAffiliation,a.position,ap.authorNo " +
							"FROM cn3Testing_new_dump.content c JOIN cn3Testing_new_dump.authorpresenter ap ON c.contentID = ap.contentID " +
							"JOIN cn3Testing_new_dump.author a ON ap.authorID = a.authorID " +
							"WHERE c.conferenceID =" + conferenceID + 
							" AND c.contentType='Poster'" +
							"";
			System.out.println("Getting poster authors from cn3Testing_new_dump");
			ResultSet rs = conn.getResultSet(sql);
			while(rs.next()){
				long contentID = rs.getLong("contentID");
				//contentID += 29;//5770 - 5741 = 29
				
				String name = rs.getString("name");
				String address = rs.getString("address");
				String department = rs.getString("departmentAffiliation");
				String university = rs.getString("universityAffiliation");
				String position = rs.getString("position");
				long authorNo = rs.getLong("authorNo");
				
				System.out.println("Authors: " + name);
				String authorID = "-1";
				sql = "SELECT authorID FROM author WHERE LOWER(name)LIKE LOWER('" + name.trim().replaceAll("'","''") + "%')";
				ResultSet rsAuthor = conn.getResultSet(sql);
				if(rsAuthor.next()){
					authorID = rsAuthor.getString("authorID");
				}else{
					sql = "INSERT INTO author (name,address,departmentAffiliation,universityAffiliation,position) VALUES " +
							"(?,?,?,?,?) ";
					//System.out.println(sql);		
					//conn.executeUpdate(sql);
					PreparedStatement pstmt = conn.conn.prepareStatement(sql);
					pstmt.setString(1, name);
					pstmt.setString(2, address);
					pstmt.setString(3, department);
					pstmt.setString(4, university);
					pstmt.setString(5, position);
					pstmt.executeUpdate();
					
					sql = "SELECT LAST_INSERT_ID()";
					rsAuthor = conn.getResultSet(sql);
					if(rsAuthor.next()){
						authorID = rsAuthor.getString(1);
						address = "N/A";
					}
				}
				
				System.out.println("authorID: " + authorID);		
				
				sql = "INSERT INTO authorpresenter (contentID,authorID,authorNo) VALUES (" + contentID + "," + authorID + "," + authorNo + ")";
				System.out.println(sql);		
				conn.executeUpdate(sql);
			}
			conn.conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
