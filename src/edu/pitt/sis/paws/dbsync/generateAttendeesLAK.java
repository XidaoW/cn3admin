package edu.pitt.sis.paws.dbsync;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class generateAttendeesLAK {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConnectHalleyDB conn = new ConnectHalleyDB();
		String sql = "SELECT * from cn3Testing_new_dump.attendees_HT_14";// "SELECT " +100002393
		int index = 0; // paperID;//
		ResultSet rs = conn.getResultSet(sql);
		try {
			// int paperID = 5594;
			String conferenceID = "130";
			while (rs.next()) {

				String name = rs.getString("name");
				// String _authors =
				// "Alessandro Acquisti,Deirdre Kathleen Mulligan";//rs.getString("authors");
				String affiliation = rs.getString("affiliation");
				String email = rs.getString("email");
				String authorID = rs.getString("authorID");
				String userID = rs.getString("userID");
				String id = rs.getString("id");
				String eventID = conferenceID;
				index++;

				if (authorID != null) {

				} else {
					sql = "SELECT authorID FROM author WHERE name LIKE '%"
							+ name + "%'";
					ResultSet rsAuthor = conn.getResultSet(sql);
					try {
						if (!rsAuthor.next()) {
							authorID = "";
							
						} else {
							authorID = rsAuthor.getString("authorID");
							
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				if (userID == null) {

					sql = "SELECT userID FROM userinfo WHERE email LIKE '%"
							+ email + "%' OR name LIKE '%"+name+"%'";
					//System.out.println(sql);
					ResultSet rsContent = conn.getResultSet(sql);
					if (!rsContent.next()) {
						sql = "INSERT INTO userinfo (passCode,userroleID,bio,email, name) VALUES ("
								+ "'6457356cf32a863bedb4dadab1fd0981'"
								+ ","
								+ "1"
								+ ",'"
								+ "at "
								+ affiliation
								+ "','"
								+ email + "','" + name + "')";
						//System.out.println(sql);

						//conn.executeUpdate(sql);

						sql = "SELECT LAST_INSERT_ID()";
						//System.out.println(sql);
						//rsContent = conn.getResultSet(sql);
						if (rsContent.next()) {
							userID = rsContent.getString(1);
						}						
					} else {

						userID = rsContent.getString(1);

					}
					System.out.println("-- " + authorID);
					System.out.println("UPDATE author SET userID = "+userID+" WHERE authorID = " + authorID +";");

					sql = "UPDATE attendees_HT_14 SET userID =" + userID
							+ " WHERE id = " + id;
					//System.out.println(sql);
					//conn.executeUpdate(sql);// Insert userID to Attendees
					
					sql = "INSERT INTO affiliation (beginDate, organizationAffiliation,usernameID) VALUES (NOW(),'"
							+ affiliation + "'," + userID + ")";
					//System.out.println(sql);
					//conn.executeUpdate(sql);// Insert userID to Attendees
					
					sql = "SELECT * FROM eventattendance e WHERE e.userID = "+userID +" AND e.eventID = "+eventID+"";
					ResultSet rs1 = conn.getResultSet(sql);
					
					if(rs1.next()){
						
					}else{

						sql = "INSERT INTO eventattendance (eventID,userID) VALUES ('"
								+ eventID + "','" + userID + "')";
						//System.out.println(sql);
						//conn.executeUpdate(sql);// Insert userID to Attendees
						// Insert affiliation
						
					}

				} else {
					

					sql = "INSERT INTO affiliation (beginDate, organizationAffiliation,usernameID) VALUES (NOW(),'"
							+ affiliation + "'," + userID + ")";
					//System.out.println(sql);
					//conn.executeUpdate(sql);// Insert userID to Attendees

					
					sql = "SELECT * FROM eventattendance e WHERE e.userID = "+userID +" AND eventID = "+eventID+"";
					ResultSet rs2 = conn.getResultSet(sql);
					if(rs2.next()){
						
					}else{
					
						sql = "INSERT INTO eventattendance (eventID,userID) VALUES ('"
								+ eventID + "','" + userID + "')";
						//System.out.println(sql);
						//conn.executeUpdate(sql);// Insert userID to Attendees
						

					}

				}

			}
			System.out.println(index);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
