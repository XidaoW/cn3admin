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

public class generateContentForECTEL13 {
	public static void main(String[] args) throws IOException {

		MySQLUtils sqlUtil = new MySQLUtils();
		String url = "http://halley.exp.sis.pitt.edu/cn3_testing/phpcn3i/accepted_papers.html";
		Document doc = Jsoup.connect(url).get();
		Elements divs = doc.select("body div");
/*
		Elements papers = doc.select("div.paper");
		Elements abstracts = doc.select("div.abstract");
*/		
		String authors1 = "";			
		String titleString1 = "";
		String abstractString1 = "";
		int m = 0;
		int n = 0;
		String[] authorArray = new String[200];
		String[] titleArray = new String[200];
		String[] abstractArray = new String[2000];
		int index = 1;
		for (Element div : divs) {
			if ((index % 2) != 0) {
				Elements authors = div.select("span:eq(0) span a");
				//System.out.println(authors);
				if(authors.isEmpty()){
					authors = div.select("span:eq(0) span");
				}
				String author_list = authors.text().replaceAll("and", ", ").trim();					
					
				//System.out.println(author_list);
				//print(author_list.substring(0, author_list.length()-1));
				author_list = author_list.replaceAll("\\r\\n+?\\r+\\n+", " ").replaceAll("&nbsp;", " ");
				//print(author_list);

				author_list = author_list.trim();
				//System.out.println("***************************");
				//System.out.println("Authors1: " +  author_list);
				
				Elements title = div.select("span:eq(1)");
				String titleString = title.text();
				titleString = titleString.trim().replaceAll("\\r\\n+?\\n+?\\r+", " ").replaceAll("&nbsp;", " ");

				String[] tt = titleString.split("\\s+");
				if(tt != null){
					for(int j=0;j<tt.length;j++){
						if(tt[j].length()>0){
							titleString1 += tt[j] + " ";
						}
					}
				}
				titleString1 = titleString1.trim();
				
				//System.out.println("Title1: " +  titleString1);
				authorArray[m] = author_list;
				titleArray[m] = titleString1;
				m++;
				authors1 = "";			
				titleString1 = "";				
				
			}else{
				String abstractString = div.text();
				abstractString = abstractString.replaceFirst("Abstract:", " ").trim().replaceAll("\\r\\n+?\\n+?\\r+", "&&newline;;").replaceAll("&nbsp;", " ").replaceAll("&quot;", "\"");
				abstractString = abstractString.trim();
				

				String[] a = abstractString.split("\\s+");
				abstractString1 = "";
				if(a != null){
					for(int j=0;j<a.length;j++){
						if(a[j].length()>0){
							abstractString1 += a[j] + " ";
						}
					}
				}
				abstractString1 = abstractString1.replaceAll("&&newline;;","<br/>").trim();
				//System.out.println("Abstract1: " + abstractString1);
				abstractArray[n] = abstractString1;
				n++;			
				abstractString1 = "";				
			}
			index++;
			
		}

		
		//System.out.println(index);
		
		if(authorArray.length > 0 && titleArray.length > 0 && abstractArray.length> 0){
			for(int i=0;i<titleArray.length;i++){
			//String topic = "N/A";
        	int conferenceID = 130;
    		ConnectHalleyDB conn = new ConnectHalleyDB();

        	String sql = "UPDATE admin_content_ht2014 SET abstract = ? WHERE title = ?";
			try {
				
				String titleSqlString = titleArray[i];
				String abstractSqlString = abstractArray[i];
				PreparedStatement pstmt = conn.conn.prepareStatement(sql);
				pstmt.setString(2,titleSqlString);
				pstmt.setString(1,abstractSqlString);
				System.out.println(pstmt);
				pstmt.executeUpdate();
				pstmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
