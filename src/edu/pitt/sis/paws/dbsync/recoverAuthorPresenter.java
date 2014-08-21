package edu.pitt.sis.paws.dbsync;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class recoverAuthorPresenter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConnectHalleyDB conn = new ConnectHalleyDB();
		String sql = "SELECT * FROM `iconf2013`";
		ResultSet rs = conn.getResultSet(sql);
		try {
			//int paperID = 5594;
			int conferenceID = 98;
			while(rs.next()){
				String keywords = rs.getString("Keywords");
				String _authors = rs.getString("authors_formatted") + ';';//rs.getString("authors");

				sql = "SELECT contentID FROM content WHERE keywords REGEXP '"+keywords.trim() + "'";	        
				System.out.println(sql);		

				ResultSet rsContent = conn.getResultSet(sql);

		
				if(rsContent.next()){
					String contentID = rsContent.getString(1);
					System.out.println("contentID: " + contentID);
					
					
					int authorno = 1;
					if(_authors!=null){
						String[] authors = _authors.split(";");//_authors.split(",");
						System.out.println("Authors: " + _authors);
						for(int i=0;i<authors.length;i++){
							if(authors[i].trim().length()==0)continue;
							String authorID = null;
							String userID = null;
							
							String[] authors_revert = authors[i].split(",");
							authors[i] = authors_revert[1].trim()+ " " +authors_revert[0].trim(); 
														
							System.out.println(authors[i]);
							sql = "SELECT authorID,userID FROM author WHERE name REGEXP '" + authors[i].trim().replaceAll("'","''") + "'";
							System.out.println(sql);		
							
							rsContent = conn.getResultSet(sql);
							if(rsContent.next()){
								authorID = rsContent.getString("authorID");
								userID = rsContent.getString("userID");
							}else{
							}	
							
							if(userID==null){
								sql = "SELECT userID FROM userinfo WHERE name REGEXP '" + authors[i].trim().replaceAll("'","''") + "'";
								System.out.println(sql);
								rsContent = conn.getResultSet(sql);
								if(rsContent.next()){
									userID = rsContent.getString("userID");
									sql = "SELECT authorID FROM author WHERE userID=" + userID;
									System.out.println(sql);
									rsContent = conn.getResultSet(sql);
									if(rsContent.next()){
										authorID = rsContent.getString("authorID");
									}
								}
							}

							System.out.println("authorID: " + authorID + " userID: " + userID);		

							if(authorID==null){
								sql = "INSERT INTO author (name,address) VALUES ('" + authors[i].trim().replaceAll("'","''") + "','N/A')";
								if(userID!=null){
									sql = "INSERT INTO author (name,address,userID) VALUES ('" + authors[i].trim().replaceAll("'","''") + "','N/A'," + userID + ")";
								}
								System.out.println(sql);		
								conn.executeUpdate(sql);
									
								sql = "SELECT LAST_INSERT_ID()";
								System.out.println(sql);		
								rsContent = conn.getResultSet(sql);
								if(rsContent.next()){
									authorID = rsContent.getString(1);
								}
							}
							
							System.out.println("authorID: " + authorID + " userID: " + userID);		
							
							sql = "INSERT INTO authorpresenter (contentID,authorID,authorNo) VALUES (" + contentID + "," + authorID + "," + authorno + ")";
							System.out.println(sql);		
							conn.executeUpdate(sql);
							authorno++;		
							
						}
					}
		
					

				}                

			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
