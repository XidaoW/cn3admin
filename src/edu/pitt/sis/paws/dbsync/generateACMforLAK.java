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
import java.util.Iterator;
import java.util.Map.Entry;

public class generateACMforLAK {
	public static void main(String[] args) throws IOException {

		String url = "http://localhost/~XWEN/phpcn3i_ot/lak_proceeding.php";
		Document doc = Jsoup.connect(url).get();
		Elements links = doc.select("a");
		System.out.println("Total Number of Posters " + links.size());
		int i = 0;
		ConnectHalleyDB conn = new ConnectHalleyDB();
		String title = null;
		String doi = null;
		String contentID = null;
		String ACMlink = null;
		HashMap<String, String> titleLinks = new HashMap<String, String>();
		HashMap<String, String> missingTitleLinks = new HashMap<String, String>();

		int index = 0;
		int inSystemIndex = 0;

		String contentIDCollection = "";
		for (Element link : links) {
			String linkHref = link.attr("href");
			
			if (linkHref.startsWith("http://dl.acm.org/citation.cfm?id=")) {
				title = link.text();
				linkHref = "http://dl.acm.org/"+linkHref;	
				//System.out.println("<a href=\""+linkHref+"\">"+title+"</a>");
				index++;
				if (title != null) 
				{
					String[] titles = title.split("'");
					titles[0] = titles[0].trim();
					String sql = "SELECT contentID FROM `content` where title like '%"
							+ titles[0].substring(0, titles[0].length()) + "%'";
					if(titles.length > 1){
						titles[1] = titles[1].trim();
						sql = "SELECT contentID FROM `content` where title like '%"
								+ titles[0].substring(0, titles[0].length()) + "%' or title like '%"
								+ titles[1].substring(0, titles[1].length()) + "%'";
					}
					ResultSet rs = conn.getResultSet(sql);
					
					try {
						
						if(!rs.next())
						{
							String href = titleLinks.get(title);
							if(href == null){

							//System.out.println(title);
							continue;
							}
						}else{
							contentID = rs.getString("contentID");

							//System.out.println(contentID);
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					
					

					
				}
				
			}
			if (linkHref.startsWith("http://dx.doi.org")) {
				doi = link.attr("href");
					//System.out.println(doi);
			}
			if(doi != null && title != null && contentID == null)
			{
				missingTitleLinks.put(title, doi);
				doi = null;
				title = null;
				continue;
			}
			
			if(doi != null && contentID != null){
				titleLinks.put(contentID, doi);
				title = null;
				doi = null;
				contentID = null;
				continue;
			}
		}
		
		Iterator entries = titleLinks.entrySet().iterator();
		while (entries.hasNext()) { // Add to result set
			Entry thisEntry = (Entry) entries.next();
			Object key = thisEntry.getKey();
			Object value = thisEntry.getValue();
			String content = (String) key;
			String doiLink = (String) value;
			
			String sql = "UPDATE content SET DOI = ? WHERE contentID = ?";
			PreparedStatement pstmt;
			try {
				pstmt = conn.conn
				.prepareStatement(sql);
				pstmt.setString(1, doiLink);
				pstmt.setString(2, content);
				String insertSql = pstmt.toString();
				System.out.println(insertSql);
				pstmt.executeUpdate();

				pstmt.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
		Iterator entries1 = missingTitleLinks.entrySet().iterator();
		while (entries.hasNext()) { // Add to result set
			Entry thisEntry = (Entry) entries1.next();
			Object key = thisEntry.getKey();
			Object value = thisEntry.getValue();
			String contentTitle = (String) key;
			String doiLink1 = (String) value;
			
			System.out.println(contentTitle);
		}
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
