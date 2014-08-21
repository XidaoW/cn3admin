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

public class GeneratePresentaForCN3 {
	public static void main(String[] args) throws IOException {
		
		 Validate.isTrue(args.length == 1, "usage: supply url to fetch");
	        String url = args[0];
	        print("Fetching %s...", url);
	        url = "https://www.conftool.pro/iConference13/index.php?page=browseSessions&mode=table&form_session="
	        		+"26";
	        Document doc = Jsoup.connect(url).get();
	        Elements titles = doc.select("p.paper_title");
	        System.out.println("Total Number of Posters " + titles.size());
	        String[] paperTitles = null;
	        int i=0;
	        ConnectHalleyDB conn = new ConnectHalleyDB();
			
	        for(Element title : titles){
	        	System.out.println(i);

	        	String titleName = title.text().toString().replace('\'', '%');
	        	titleName = titleName.replace('\"', '%');

	        	i++;
	        	System.out.println(i);
	        	System.out.println(titleName);

	        	String sql = "SELECT contentID FROM `content` where title like '%"+ titleName+"%'";
	        	System.out.println(sql);
				ResultSet rs = conn.getResultSet(sql);
	        	System.out.println(rs);

				try {
					while(rs.next()){
						String contentID = rs.getString("contentID");
						//DateFormat date = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
						String time = "2013-02-15 00:00:00";
						String beginTime = "2013-02-15 10:30:00";
						String endTime = "2013-02-15 12:00:00";
						String eventSessionID = "1107";
						String presentationType = "Alternative Event";//;//"Paper"//"Note"
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
