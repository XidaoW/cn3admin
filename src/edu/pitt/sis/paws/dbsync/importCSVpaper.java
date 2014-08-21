package edu.pitt.sis.paws.dbsync;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class importCSVpaper {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConnectHalleyDB conn = new ConnectHalleyDB();
//		String sql = "SELECT * FROM cn3fromcn20.admin_content_ectel2013_keynote WHERE contentID > 3";
		String sql = "SELECT * FROM cn3Testing_new_dump.admin_content_iknow2014 WHERE ID > 89 AND ID < 110;";
		
		ResultSet rs = conn.getResultSet(sql);
		try {
			//int paperID = 5594;
			int conferenceID = 131;
			while(rs.next()){
				String title = rs.getString("title");
				String _authors = rs.getString("author_list");//rs.getString("authors");author_list
				String track = rs.getString("contentTrack").trim();//rs.getString("track");contentTrack
				String Category = rs.getString("contentType");//rs.getString("category");contentType//type
				String keywords = rs.getString("keywords");
				String _abstract = rs.getString("abstract");
				//String contentLink = null;//rs.getString("url");String _authors1 = rs.getString("authors_formatted_1_name");//rs.getString("authors");

				/*if(contentLink!=null){
					if(contentLink.trim().length()==0){
						contentLink=null;
					}
				}*/
				
				//keywords = keywords.replaceAll("\\n|\\r|\\r\\n", ",");
				//System.out.println("parsed keywords: " + keywords);
				
				sql = "INSERT INTO content " +
						"(title,abstract,conferenceID,contentType,keywords,contentTrack) " +
						"VALUES (?,?,?,?,?,?)";	        
				PreparedStatement pstmt = conn.conn.prepareStatement(sql);
				pstmt.setString(1,title);
				pstmt.setString(2,_abstract);
				pstmt.setLong(3,conferenceID);
				pstmt.setString(4,Category);
				pstmt.setString(5,keywords);
				pstmt.setString(6,track);
				//pstmt.setInt(8,paperID);
				pstmt.executeUpdate();
				pstmt.close();
		
				sql = "SELECT LAST_INSERT_ID()";//"SELECT " + paperID;//
				//paperID++;
				ResultSet rsContent = conn.getResultSet(sql);
				if(rsContent.next()){
					String contentID = rsContent.getString(1);
					System.out.println("contentID: " + contentID);
					/*
					String time = "2013-02-25 00:00:00";
					String beginTime = "2013-02-25 18:00:00";
					String endTime = "2013-02-25 20:30:00";
					String eventSessionID = "1253";
					String presentationType = "Posters";//;//"Paper"//"Note"
					sql = "INSERT INTO presentation " +
							"(presentationDate,beginTime,endTime,contentID,eventSessionID,presentationType) " +
							"VALUES (?,?,?,?,?,?)";	        
					pstmt = conn.conn.prepareStatement(sql);
					pstmt.setString(1,time);
					pstmt.setString(2,beginTime);
					pstmt.setString(3,endTime);
					pstmt.setString(4,contentID);
					pstmt.setString(5,eventSessionID);	
					pstmt.setString(6,presentationType);
					String insertSql = pstmt.toString();
		        	System.out.println(insertSql);

					pstmt.executeUpdate();
					pstmt.close();
					*/
					
					
					int authorno = 1;
					if(_authors!=null){
						String[] authors = _authors.split(",");//_authors.split(",");
						System.out.println("Authors: " + _authors);
						for(int i=0;i<authors.length;i++){
							if(authors[i].trim().length()==0)continue;
							String authorID = null;
							String userID = null;
														
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
		
					/*if(author1 != null && author1.trim().length() > 0){
						String authorID = "-1";
						sql = "SELECT authorID FROM cn3Testing.author WHERE LOWER(name)LIKE LOWER('" + author1.trim().replaceAll("'","''") + "%')";
						System.out.println(sql);		
						rsContent = conn.getResultSet(sql);
						if(rsContent.next()){
							authorID = rsContent.getString(1);
						}else{
							sql = "INSERT INTO cn3Testing.author (name,address) VALUES ('" + author1.trim().replaceAll("'","''") + "','" + organisation1.trim().replaceAll("'","''") + "')";
							System.out.println(sql);		
							conn.executeUpdate(sql);
								
							sql = "SELECT LAST_INSERT_ID()";
							rsContent = conn.getResultSet(sql);
							if(rsContent.next()){
								authorID = rsContent.getString(1);
							}
						}	
						System.out.println("authorID: " + authorID);		
	
						sql = "INSERT INTO cn3Testing.authorpresenter (contentID,authorID,authorNo) VALUES (" + contentID + "," + authorID + "," + authorno + ")";
						System.out.println(sql);		
						conn.executeUpdate(sql);
						
						authorno++;
					}

					if(author2 != null && author2.trim().length() > 0){
						String authorID = "-1";
						sql = "SELECT authorID FROM cn3Testing.author WHERE LOWER(name)LIKE LOWER('" + author2.trim().replaceAll("'","''") + "%')";
						System.out.println(sql);		
						rsContent = conn.getResultSet(sql);
						if(rsContent.next()){
							authorID = rsContent.getString(1);
						}else{
							sql = "INSERT INTO cn3Testing.author (name,address) VALUES ('" + author2.trim().replaceAll("'","''") + "','" + organisation2.trim().replaceAll("'","''") + "')";
							System.out.println(sql);		
							conn.executeUpdate(sql);
								
							sql = "SELECT LAST_INSERT_ID()";
							rsContent = conn.getResultSet(sql);
							if(rsContent.next()){
								authorID = rsContent.getString(1);
							}
						}	
						System.out.println("authorID: " + authorID);		
	
						sql = "INSERT INTO cn3Testing.authorpresenter (contentID,authorID,authorNo) VALUES (" + contentID + "," + authorID + "," + authorno + ")";
						System.out.println(sql);		
						conn.executeUpdate(sql);
						
						authorno++;
					}

					if(author3 != null && author3.trim().length() > 0){
						String authorID = "-1";
						sql = "SELECT authorID FROM cn3Testing.author WHERE LOWER(name)LIKE LOWER('" + author3.trim().replaceAll("'","''") + "%')";
						System.out.println(sql);		
						rsContent = conn.getResultSet(sql);
						if(rsContent.next()){
							authorID = rsContent.getString(1);
						}else{
							sql = "INSERT INTO cn3Testing.author (name,address) VALUES ('" + author3.trim().replaceAll("'","''") + "','" + organisation3.trim().replaceAll("'","''") + "')";
							System.out.println(sql);		
							conn.executeUpdate(sql);
								
							sql = "SELECT LAST_INSERT_ID()";
							rsContent = conn.getResultSet(sql);
							if(rsContent.next()){
								authorID = rsContent.getString(1);
							}
						}	
						System.out.println("authorID: " + authorID);		
	
						sql = "INSERT INTO cn3Testing.authorpresenter (contentID,authorID,authorNo) VALUES (" + contentID + "," + authorID + "," + authorno + ")";
						System.out.println(sql);		
						conn.executeUpdate(sql);
						
						authorno++;
					}

					if(author4 != null && author4.trim().length() > 0){
						String authorID = "-1";
						sql = "SELECT authorID FROM cn3Testing.author WHERE LOWER(name)LIKE LOWER('" + author4.trim().replaceAll("'","''") + "%')";
						System.out.println(sql);		
						rsContent = conn.getResultSet(sql);
						if(rsContent.next()){
							authorID = rsContent.getString(1);
						}else{
							sql = "INSERT INTO cn3Testing.author (name,address) VALUES ('" + author4.trim().replaceAll("'","''") + "','" + organisation4.trim().replaceAll("'","''") + "')";
							System.out.println(sql);		
							conn.executeUpdate(sql);
								
							sql = "SELECT LAST_INSERT_ID()";
							rsContent = conn.getResultSet(sql);
							if(rsContent.next()){
								authorID = rsContent.getString(1);
							}
						}	
						System.out.println("authorID: " + authorID);		
	
						sql = "INSERT INTO cn3Testing.authorpresenter (contentID,authorID,authorNo) VALUES (" + contentID + "," + authorID + "," + authorno + ")";
						System.out.println(sql);		
						conn.executeUpdate(sql);
						
						authorno++;
					}

					if(author5 != null && author5.trim().length() > 0){
						String authorID = "-1";
						sql = "SELECT authorID FROM cn3Testing.author WHERE LOWER(name)LIKE LOWER('" + author1.trim().replaceAll("'","''") + "%')";
						System.out.println(sql);		
						rsContent = conn.getResultSet(sql);
						if(rsContent.next()){
							authorID = rsContent.getString(1);
						}else{
							sql = "INSERT INTO cn3Testing.author (name,address) VALUES ('" + author5.trim().replaceAll("'","''") + "','" + organisation5.trim().replaceAll("'","''") + "')";
							System.out.println(sql);		
							conn.executeUpdate(sql);
								
							sql = "SELECT LAST_INSERT_ID()";
							rsContent = conn.getResultSet(sql);
							if(rsContent.next()){
								authorID = rsContent.getString(1);
							}
						}	
						System.out.println("authorID: " + authorID);		
	
						sql = "INSERT INTO cn3Testing.authorpresenter (contentID,authorID,authorNo) VALUES (" + contentID + "," + authorID + "," + authorno + ")";
						System.out.println(sql);		
						conn.executeUpdate(sql);
						
						authorno++;
					}
					
					if(author6 != null && author6.trim().length() > 0){
						String authorID = "-1";
						sql = "SELECT authorID FROM cn3Testing.author WHERE LOWER(name)LIKE LOWER('" + author6.trim().replaceAll("'","''") + "%')";
						System.out.println(sql);		
						rsContent = conn.getResultSet(sql);
						if(rsContent.next()){
							authorID = rsContent.getString(1);
						}else{
							sql = "INSERT INTO cn3Testing.author (name,address) VALUES ('" + author6.trim().replaceAll("'","''") + "','" + organisation6.trim().replaceAll("'","''") + "')";
							System.out.println(sql);		
							conn.executeUpdate(sql);
								
							sql = "SELECT LAST_INSERT_ID()";
							rsContent = conn.getResultSet(sql);
							if(rsContent.next()){
								authorID = rsContent.getString(1);
							}
						}	
						System.out.println("authorID: " + authorID);		
	
						sql = "INSERT INTO cn3Testing.authorpresenter (contentID,authorID,authorNo) VALUES (" + contentID + "," + authorID + "," + authorno + ")";
						System.out.println(sql);		
						conn.executeUpdate(sql);
						
						authorno++;
					}

					if(author7 != null && author7.trim().length() > 0){
						String authorID = "-1";
						sql = "SELECT authorID FROM cn3Testing.author WHERE LOWER(name)LIKE LOWER('" + author7.trim().replaceAll("'","''") + "%')";
						System.out.println(sql);		
						rsContent = conn.getResultSet(sql);
						if(rsContent.next()){
							authorID = rsContent.getString(1);
						}else{
							sql = "INSERT INTO cn3Testing.author (name,address) VALUES ('" + author7.trim().replaceAll("'","''") + "','" + organisation7.trim().replaceAll("'","''") + "')";
							System.out.println(sql);		
							conn.executeUpdate(sql);
								
							sql = "SELECT LAST_INSERT_ID()";
							rsContent = conn.getResultSet(sql);
							if(rsContent.next()){
								authorID = rsContent.getString(1);
							}
						}	
						System.out.println("authorID: " + authorID);		
	
						sql = "INSERT INTO cn3Testing.authorpresenter (contentID,authorID,authorNo) VALUES (" + contentID + "," + authorID + "," + authorno + ")";
						System.out.println(sql);		
						conn.executeUpdate(sql);
						
						authorno++;
					}

					if(author8 != null && author8.trim().length() > 0){
						String authorID = "-1";
						sql = "SELECT authorID FROM cn3Testing.author WHERE LOWER(name)LIKE LOWER('" + author8.trim().replaceAll("'","''") + "%')";
						System.out.println(sql);		
						rsContent = conn.getResultSet(sql);
						if(rsContent.next()){
							authorID = rsContent.getString(1);
						}else{
							sql = "INSERT INTO cn3Testing.author (name,address) VALUES ('" + author8.trim().replaceAll("'","''") + "','" + organisation8.trim().replaceAll("'","''") + "')";
							System.out.println(sql);		
							conn.executeUpdate(sql);
								
							sql = "SELECT LAST_INSERT_ID()";
							rsContent = conn.getResultSet(sql);
							if(rsContent.next()){
								authorID = rsContent.getString(1);
							}
						}	
						System.out.println("authorID: " + authorID);		
	
						sql = "INSERT INTO cn3Testing.authorpresenter (contentID,authorID,authorNo) VALUES (" + contentID + "," + authorID + "," + authorno + ")";
						System.out.println(sql);		
						conn.executeUpdate(sql);
						
						authorno++;
					}

					if(author9 != null && author9.trim().length() > 0){
						String authorID = "-1";
						sql = "SELECT authorID FROM cn3Testing.author WHERE LOWER(name)LIKE LOWER('" + author9.trim().replaceAll("'","''") + "%')";
						System.out.println(sql);		
						rsContent = conn.getResultSet(sql);
						if(rsContent.next()){
							authorID = rsContent.getString(1);
						}else{
							sql = "INSERT INTO cn3Testing.author (name,address) VALUES ('" + author9.trim().replaceAll("'","''") + "','" + organisation9.trim().replaceAll("'","''") + "')";
							System.out.println(sql);		
							conn.executeUpdate(sql);
								
							sql = "SELECT LAST_INSERT_ID()";
							rsContent = conn.getResultSet(sql);
							if(rsContent.next()){
								authorID = rsContent.getString(1);
							}
						}	
						System.out.println("authorID: " + authorID);		
	
						sql = "INSERT INTO cn3Testing.authorpresenter (contentID,authorID,authorNo) VALUES (" + contentID + "," + authorID + "," + authorno + ")";
						System.out.println(sql);		
						conn.executeUpdate(sql);
						
						authorno++;
					}*/

				}                

			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
