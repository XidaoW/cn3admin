package edu.pitt.sis.paws.dbsync;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringEscapeUtils;

public class readCourseRecord {
	public static void main(String[] args) throws IOException, SQLException, ParseException {
		ConnectHalleyDB conn = new ConnectHalleyDB();

		MySQLUtils mySqlUtil = new MySQLUtils();
		StringEscapeUtils stringEscapeSql = new StringEscapeUtils();
		BufferedReader bufferReader;
		InputStreamReader instream;
		String dir = "/Users/XWEN/Sites/linkedinDemo/aalto-courses-anon.csv";
		
		//String dir = "/Users/XWEN/Dropbox/PhdWork/courseAgent/Research/DataMiningg/aalto-courses-anon.csv";
		instream = new FileReader(dir);
		bufferReader = new BufferedReader(instream);
		int i=0;
		try {
			String line;
			String student_id= "";
			String course_code = "";
			String course_name = "";
			String course_name_2 = "";
			String credits = "";
			String grade = "";
			String date = "";
			String course_name_final = "";
			while ((line = bufferReader.readLine()) != null ) {
				if(line == ""){
					continue;
				}else{
					if(isNumeric(line.substring(0, 1))){
						String[] record = line.split(",");
						student_id = record[0].trim();
						course_code = record[1].trim();
						course_name = record[2].trim();
						if(record.length > 3)
							credits = record[3].trim();
						if(record.length > 4)						
							grade = record[4].trim();
						if(record.length > 5)												
							date = record[5].trim();
						
					}else{
						String[] record = line.split(",");
						course_name_2 = record[0].trim();
						if(record.length > 1)						
							credits = record[1].trim();
						if(record.length > 2)						
							grade = record[2].trim();
						if(record.length > 3)						
							date = record[3].trim();
					}	
				}
				course_name_final = course_name + " " + course_name_2;
				course_name_final = course_name_final.trim();
				
				
				if(student_id != "" && course_name_final != "" && credits != "" && grade != ""&& date != ""){
					String sql = "INSERT INTO aalto " +
							"(student_id,course_code,course_name, credits, grade, date) " +
							"VALUES (?,?,?,?,?,DATE_FORMAT(?,'%Y-%m-%d'));";	
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					//System.out.println(date);
					String[] dataArray = date.split("/");
					date = "20" + dataArray[2] + "-" + dataArray[0] + "-" + dataArray[1];
					//System.out.println(dataArray[0]);
					//System.out.println(dataArray[1]);
					//System.out.println(dataArray[2]);
					java.util.Date currentDate = sdf.parse(date);
					//System.out.println(currentDate);
					
					PreparedStatement pstmt = conn.conn.prepareStatement(sql);
					//DATE_FORMAT(NOW(),'%m-%d-%Y')
					pstmt.setString(1,student_id);
					pstmt.setString(2,course_code);
					pstmt.setString(3,course_name);
					pstmt.setString(4,credits);
					pstmt.setString(5,grade);
					pstmt.setString(6,date);					
					pstmt.executeUpdate();
					System.out.println(pstmt.toString());
					
					pstmt.close();

					student_id= "";
					course_code = "";
					course_name = "";
					course_name_2 = "";
					credits = "";
					grade = "";
					date = "";
					course_name_final = "";					
				}else{
					//System.out.println("Missing: " + student_id);
					//System.out.println("Course: " + course_code);
				}
				

				
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
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
}
