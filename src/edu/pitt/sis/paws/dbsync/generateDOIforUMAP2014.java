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
import java.util.HashMap;

public class generateDOIforUMAP2014 {
	public static void main(String[] args) throws IOException, SQLException {

		String url = "http://ceur-ws.org/Vol-1181/";
		Document doc = Jsoup.connect(url).get();
		Elements links = doc.select("a");
		System.out.println("Total Number of Posters " + links.size());
		int i = 0;
		ConnectHalleyDB conn = new ConnectHalleyDB();
		String title = null;
		String ACMlink = null;
		HashMap<String, String> titleLinks = new HashMap<String, String>();

		int index = 0;
		int inSystemIndex = 0;

		String contentIDCollection = "";
		for (Element link : links) {
			String linkHref = link.attr("href");
//			if (linkHref.startsWith("/content/pdf/10.1007")) {
			if (linkHref.startsWith("pros2014_paper_")) {
				Elements spans = link.select("span");
				for(Element span : spans){
					title = span.text();
				}
				linkHref = "http://ceur-ws.org/Vol-1181/" + link.attr("href");

				//				linkHref = linkHref.replaceAll("/chapter/", "");
//				title = link.text();
				//linkHref = "http://dl.acm.org/"+linkHref;	
				//System.out.println("<a href=\""+linkHref+"\">"+title+"</a>");
				
				index++;
				if (title != null && title != "") 
				{
					String[] titles = title.split("'");
					titles[0] = titles[0].trim();
					String sql = "SELECT contentID FROM `content` where conferenceID = 129 AND title like '%"
							+ titles[0].substring(0, titles[0].length()) + "%'";
					if(titles.length > 1){
						titles[1] = titles[1].trim();
						sql = "SELECT contentID FROM `content` where onferenceID = 129 AND title like '%"
								+ titles[0].substring(0, titles[0].length()) + "%' or title like '%"
								+ titles[1].substring(0, titles[1].length()) + "%'";
					}
					System.out.println(sql);
					
					ResultSet rs = conn.getResultSet(sql);
					if(rs == null)
					{
						System.out.println(title);
						System.out.println(linkHref);
					}else{
						
						try {
							while (rs.next()) {
								String contentID = rs.getString("contentID");
								// DateFormat date = new
								// SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
								System.out.println(contentID);
								//String UpdateSql = "UPDATE content SET DOI = ? WHERE contentID = ?";
								String UpdateSql = "INSERT INTO contentlink (linkURL,contentID, linkCaption, type, userID,time) VALUES (?,?,?,?,?,NOW());";
								PreparedStatement pstmt = conn.conn
										.prepareStatement(UpdateSql);
								pstmt.setString(1, linkHref);
								pstmt.setString(2, contentID);
								pstmt.setString(3, "PDF");
								pstmt.setString(4, "Paper");
								pstmt.setString(5, "100000963");
								
								
								inSystemIndex++;
								String insertSql = pstmt.toString();
								// System.out.println(sqls[1]+sqls[2]+ ";");
								System.out.println(insertSql);
								pstmt.executeUpdate();
								contentIDCollection = contentIDCollection + ", "+ contentID;
								pstmt.close();

							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
			            rs.close();
			            rs = null;
						
					}
					
					
					
					

					title = null;
					
				}
				linkHref = "";
				title = "";
			}

		}
		//System.out.println(contentIDCollection);
		//System.out.println(inSystemIndex);

	}

	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

	private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;

	}
}
