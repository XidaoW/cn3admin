package edu.pitt.sis.paws.dbsync;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class regrexParenthesesTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConnectHalleyDB conn = new ConnectHalleyDB();
		String sql = "SELECT * FROM admin_content_iknow_bk1 WHERE conferenceID = 109";
//		String sql = "SELECT * FROM cn3Testing_new_dump.umap_lifelong_2013";
		
		ResultSet rs = conn.getResultSet(sql);
		try {
			//int paperID = 5594;
			int conferenceID = 124;
			while(rs.next()){
				String id = rs.getString("id");
				String title = rs.getString("title");
				String _authors = rs.getString("author_list");//rs.getString("authors");author_list
				String track = rs.getString("contentTrack");//rs.getString("track");contentTrack
				String Category = rs.getString("contentType");//rs.getString("category");contentType
				//String keywords = rs.getString("keywords");
				String _abstract = rs.getString("abstract");
				//String contentLink = null;//rs.getString("url");String _authors1 = rs.getString("authors_formatted_1_name");//rs.getString("authors");

				/*if(contentLink!=null){
					if(contentLink.trim().length()==0){
						contentLink=null;
					}
				}*/
				
				//keywords = keywords.replaceAll("\\n|\\r|\\r\\n", ",");
				//System.out.println("parsed keywords: " + keywords);
				
				_authors = _authors.replaceAll("\\(.*\\)", "").trim();
				System.out.println(_authors);	
				sql = "UPDATE admin_content SET author_list = ? WHERE id = ?";
				PreparedStatement pstmt = conn.conn.prepareStatement(sql);
				pstmt.setString(1,_authors);
				pstmt.setString(2,id);

				String insertSql = pstmt.toString();
	        	System.out.println(insertSql);

				pstmt.executeUpdate();
				pstmt.close();

			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
