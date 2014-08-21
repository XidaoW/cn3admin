package edu.pitt.sis.paws.dbsync;


import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class generateContentForHT13 {
	public static void main(String[] args) throws IOException {

		MySQLUtils sqlUtil = new MySQLUtils();
		String url = "http://strelka.exp.sis.pitt.edu/xidaowen/FinalPapers.html";
		Document doc = Jsoup.connect(url).get();
		Elements papers = doc.select("paper");
		
		System.out.println("Total Number of papers " + papers.size());
		ConnectHalleyDB conn = new ConnectHalleyDB();

		String authors1 = "";			
		String titleString1 = "";
		String abstractString1 = "";
		int m = 0;
		int n = 0;

		for (Element paper : papers) {
			//Elements authors = paper.select("authors");
			Elements title = paper.select("title");
			Elements paperID = paper.select("paperID");
			Elements _abstract = paper.select("abstract");
			Elements keywords = paper.select("keywords");
			Elements type = paper.select("type");
			
			Elements authors = paper.select("authors");
			
			String typeString = type.text();

			String titleString = title.text();
			titleString = titleString.trim().replaceAll("\\r\\n+?\\n+?\\r+", " ").replaceAll("&nbsp;", " ");
			titleString = titleString.replace("\n", "").replace("\r", "");
			
			String paperIDString = paperID.text();
			paperIDString = paperIDString.trim().replaceAll("\\r\\n+?\\n+?\\r+", " ").replaceAll("&nbsp;", " ");
			String _abstractString = _abstract.text();
			_abstractString = _abstractString.trim().replaceAll("\\r\\n+?\\n+?\\r+", " ").replaceAll("&nbsp;", " ");
			String keywordsString = keywords.text();
			keywordsString = keywordsString.trim().replaceAll("\\r\\n+?\\n+?\\r+", " ").replaceAll("&nbsp;", " ");


			
			titleString1 = titleString.trim();
			

			abstractString1 = _abstractString.trim();			
			
			System.out.println("Title1: " +  titleString1);
			
			System.out.println("paperID: " + paperIDString);			
			System.out.println("title: " + titleString1);
			System.out.println("abstract: " + abstractString1);
			System.out.println("keywords: " + keywordsString);
			
			String paperTopic = "";
			
        	int conferenceID = 127;

        	String sql = "INSERT INTO content (conferenceID,title,paperTopic,abstract, keywords,contentType) " +
							"VALUES (?,?,?,?,?,?)";	        
			try {
				

				PreparedStatement pstmt = conn.conn.prepareStatement(sql);
				pstmt.setInt(1,conferenceID);
				pstmt.setString(2,titleString1);
				pstmt.setString(3,paperTopic);
				pstmt.setString(4,abstractString1);
				pstmt.setString(5,keywordsString);
				pstmt.setString(6,typeString);
				
				pstmt.executeUpdate();
				pstmt.close();
				
				sql = "SELECT LAST_INSERT_ID()";
				ResultSet rs = conn.getResultSet(sql);
				if(rs.next()){
					String contentID = rs.getString(1);
					System.out.println("contentID: " + contentID);
					
					int authorOrder = 1;
					int k = 0;
					Elements authorInfos = authors.select("authorInfo");
					System.out.println(authorInfos.size());
					for (Element authorInfo : authorInfos) {
						Elements author_name = authorInfo.getElementsByTag("author");
						Elements author_affil = authorInfo.getElementsByTag("affiliation");

						String author_name_string = author_name.text();
						author_name_string = author_name_string.trim().replaceAll("\\r\\n+?\\n+?\\r+", " ").replaceAll("&nbsp;", " ");
						System.out.println(author_name_string);
						String author_affil_string = author_affil.text();
						author_affil_string = author_affil_string.trim().replaceAll("\\r\\n+?\\n+?\\r+", " ").replaceAll("&nbsp;", " ");
						String[] author_affil_string_array = author_affil_string.split(",");
						author_affil_string = author_affil_string_array[0];
						String authorID = "-1";
						String newname = "";
						newname = author_name_string;
						
						sql = "SELECT authorID FROM author WHERE LOWER(name)LIKE LOWER('" + newname.trim().replaceAll("'","''") + "%')";
						rs.close();
						rs = conn.getResultSet(sql);
						if(rs.next()){
							authorID = rs.getString(1);
							System.out.println("authorID: " + authorID + " name: " + newname);
						}else{
							sql = "INSERT INTO author (name,address, universityAffiliation) VALUES ('" + newname.trim().replaceAll("'","''") + "','N/A','"+author_affil_string +"')";
							conn.executeUpdate(sql);
							System.out.println(sql);
						
							sql = "SELECT LAST_INSERT_ID()";
							rs.close();
							rs = conn.getResultSet(sql);
							if(rs.next()){
								authorID = rs.getString(1);
								System.out.println("authorID: " + authorID + " name: " + newname);
							}
						}
						k = authorOrder;
						
						sql = "INSERT INTO authorpresenter (contentID,authorID,authorNo) VALUES (" + contentID + "," + authorID + "," + k + ")";
						conn.executeUpdate(sql);
						authorOrder++;
						
						
					}
					
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
			return s.substring(0, width - 1) + ".";
		else
			return s;

	}
	
}
