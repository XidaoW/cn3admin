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

public class generatePresenFromCN3 {
	public static void main(String[] args) throws IOException {
		

	        int i=0;
	        ConnectHalleyDB conn = new ConnectHalleyDB();
	        String sql = "select contentID from content where contentType = 'Posters' and conferenceID = 98";
			ResultSet rs = conn.getResultSet(sql);

	       
				try {
					while(rs.next()){
						//String contentID = rs.getString("contentID");
						String contentID = rs.getString("contentID");
						//DateFormat date = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
						String time = "2013-02-14 00:00:00";
						String beginTime = "2013-02-14 17:30:00";
						String endTime = "2013-02-14 19:00:00";
						String eventSessionID = "1096";
						String presentationType = "Poster";	
						sql = "INSERT INTO presentation " +
								"(presentationDate,beginTime,endTime,contentID,eventSessionID,presentationType) " +
								"VALUES (?,?,?,?,?,?)";	        
						PreparedStatement pstmt = conn.conn.prepareStatement(sql);
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
