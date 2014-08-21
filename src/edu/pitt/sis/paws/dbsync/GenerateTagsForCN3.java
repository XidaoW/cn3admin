package edu.pitt.sis.paws.dbsync;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GenerateTagsForCN3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int conferenceID = 131;
		ConnectHalleyDB conn = new ConnectHalleyDB();
		String sql = "SELECT contentID,keywords FROM content WHERE conferenceID =  " + conferenceID +
				" AND contentID >= 9435" +
				//" AND LENGTH(keywords) > 0" +
				//" AND contentType='Workshop Paper'" +
				"";//" AND keywords IS NOT NULL AND LENGTH(TRIM(keywords)) > 0";// AND contentID >= 5589";
		try {
			ResultSet rs = conn.getResultSet(sql);
			while(rs.next()){
				String contentID = rs.getString("contentID");
//				sql = "DELETE FROM tagging WHERE contentID = " + contentID + " AND userID = 15";
//				conn.executeUpdate(sql);
				String keywords = rs.getString("keywords");
				if(keywords==null)continue;
				if(keywords.trim().length()==0)continue;
				String[] j = keywords.trim().split(",");
				//String[] k = j.split(";");
				if(j!=null){

					for(int ii=0;ii<j.length;ii++){
						String[] k = j[ii].trim().split(";");
						
						if(k!=null){
							for(int i=0;i<k.length;i++){
								System.out.println("contentID: " + contentID + 
										" keyword: " + k[i].trim().toLowerCase().replaceAll("\\s+|\\n+", " "));
								sql = "SELECT tagID FROM tag WHERE tag = ?";
								PreparedStatement pstmt = conn.conn.prepareStatement(sql);
								pstmt.setString(1, k[i].trim().toLowerCase().replaceAll("\\s+|\\n+", " "));
								ResultSet rsTag = pstmt.executeQuery();
								long tagID = -1;
								if(rsTag.next()){
									tagID = rsTag.getLong("tagID");
								}
								if(tagID == -1){
									sql = "INSERT INTO tag (tag) VALUES (?)";
									pstmt.close();
									pstmt = conn.conn.prepareStatement(sql);
									pstmt.setString(1, k[i].trim().toLowerCase());
									pstmt.executeUpdate();
									
									sql = "SELECT LAST_INSERT_ID()";
									rsTag = conn.getResultSet(sql);
									if(rsTag.next()){
										tagID = rsTag.getLong(1);
									}
								}
								if(tagID > -1){
									sql = "INSERT INTO tagging (tagID,tagTime,contentID,userID) VALUES (" + tagID + ",NOW()," + contentID + ",15)";
									conn.executeUpdate(sql);
								}
							}
						}
					}
					
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
