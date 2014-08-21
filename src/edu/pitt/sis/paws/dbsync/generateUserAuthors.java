package edu.pitt.sis.paws.dbsync;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class generateUserAuthors {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConnectHalleyDB conn = new ConnectHalleyDB();
		String sql = "SELECT * from content where content.title like '%Special Workshop on Informatio%'";//"SELECT " + paperID;//
		ResultSet rs = conn.getResultSet(sql);
		try {
			//int paperID = 5594;
			int conferenceID = 98;
			while(rs.next()){
				String contentID = rs.getString("contentID");
				String _authors = "Alessandro Acquisti,Deirdre Kathleen Mulligan";//rs.getString("authors");

				/*if(contentLink!=null){
					if(contentLink.trim().length()==0){
						contentLink=null;
					}
				}*/
				
				System.out.println("contentID: " + contentID);
				int authorno = 1;
				if(_authors!=null){
					String[] authors = _authors.split(",");//_authors.split(",");
					System.out.println("Authors: " + _authors);
					for(int i=0;i<authors.length;i++){
						if(authors[i].trim().length()==0)continue;
						String authorID = null;
						String userID = null;
						authors[i] = authors[i].replace(", ",",");
			
						
						System.out.println(authors[i]);
						sql = "SELECT authorID,userID FROM author WHERE name REGEXP '" + authors[i].trim().replaceAll("'","''") + "'";
						System.out.println(sql);		
						
						ResultSet rsContent = conn.getResultSet(sql);
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


				
				//System.out.println("original authors: " + _authors);
				//String[] authors = _authors.split(";");
				//System.out.println("parsed authors: " + _authors);
				
				/*System.out.println("original keywords: " + keywords);
				keywords = keywords.replaceAll("\\n|\\r|\\r\\n", ",");
				System.out.println("parsed keywords: " + keywords);*/
				
				
		
				//paperID++;
				
			}
		  }	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
