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

public class generateFromHTML {
	public static void main(String[] args) throws IOException, SQLException {

//		String url = "http://www.ncbi.nlm.nih.gov/pubmed?term=(Xie%2C%20Wen%5BAuthor%5D)%20AND%20University%20of%20Pittsburgh%5BAffiliation%5D";
		String url = "http://halley.exp.sis.pitt.edu/linkedInJob/pubmed.html";		
		Document doc = Jsoup.connect(url).get();
		Elements links = doc.select("a");
		System.out.println("Total Number of Posters " + links.size());
		int i = 0;
		String title = null;



		for (Element link : links) {
			String linkHref = link.attr("href");
//			if (linkHref.startsWith("/content/pdf/10.1007")) {
			if (linkHref.startsWith("/pubmed/")) {
//				Elements spans = link.select("span");
//				for(Element span : spans){
//					title = span.text();
//				}
//				linkHref = "http://ceur-ws.org/Vol-1181/" + link.attr("href");
				title = link.text();
				
				System.out.println(title);
				

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
