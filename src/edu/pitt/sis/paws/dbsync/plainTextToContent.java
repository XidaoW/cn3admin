package edu.pitt.sis.paws.dbsync;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.lang.StringEscapeUtils;


public class plainTextToContent {
	public static void main(String[] args) throws IOException, SQLException {
		ConnectHalleyDB conn = new ConnectHalleyDB();

		MySQLUtils mySqlUtil = new MySQLUtils();
		StringEscapeUtils stringEscapeSql = new StringEscapeUtils();
		BufferedReader bufferReader;
		InputStreamReader instream;
		String dir = "/Users/XWEN/Sites/linkedinDemo/umap2013.txt";
		instream = new FileReader(dir);
		bufferReader = new BufferedReader(instream);
		int i=0;
		try {
			String line;
			String _abstract= "";
			String authors = "";
			String title = "";
			while ((line = bufferReader.readLine()) != null ) {
				if(line == ""){
					continue;
				}else{
					if(line.startsWith("Abstract")){
						_abstract= line.trim();
						//_abstract = _abstract.replaceAll("'", "\'");
						_abstract = stringEscapeSql.escapeSql(_abstract);
						
						continue;
					}
					if(line.contains("=")){
						String[] authorTitle = line.split("=");
						authors = authorTitle[0].trim();
						title = authorTitle[1].trim();
						authors = authors.replaceAll("'", "\'");
						title = title.replaceAll("\'", "\\'");
						
						authors = stringEscapeSql.escapeSql(authors);
						title = stringEscapeSql.escapeSql(title);
						
						//System.out.println("Authors:" + authors);
						//System.out.println("Title:" +title);
						continue;
					}
				}
				
				if(_abstract != "" && authors != "" && title != ""){
					//System.out.println("INSERT INTO content_umap2013 (title,authors,abstract) VALUES ('"+title+"','"+authors+"','"+_abstract+"');");
					String sql = "INSERT INTO content_umap2013 " +
							"(title,abstract,authors) " +
							"VALUES (?,?,?)";	
					System.out.println(_abstract);
					
					PreparedStatement pstmt = conn.conn.prepareStatement(sql);
					pstmt.setString(1,title);
					pstmt.setString(2,_abstract);
					pstmt.setString(3,authors);
					pstmt.executeUpdate();
					pstmt.close();
				}
				_abstract= "";
				authors = "";
				title = "";
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bufferReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//System.out.println(i);
		
	
	}
}
