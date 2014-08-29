package edu.pitt.sis.paws.dbsync;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class generatePaperForCNMobilie {
	@SuppressWarnings("null")
	public static void main(String[] args) throws IOException {

		ConnectHalleyDB conn = new ConnectHalleyDB();
		//For Keynote
		System.out.println("//FOR PAPERS****************");


		//FOR PAPERS
//		String sql1 = "SELECT p.presentationID,p.eventSessionID,c.contentID,c.title,c.abstract,c.contentType,cl.linkURL,p.presentationDate, p.beginTime,p.endTime,a.authorID,aa.`name`,e.location,GROUP_CONCAT(DISTINCT aa.`name` ORDER BY a.authorNo ASC SEPARATOR ', ') AS authors, aa.userID,af.organizationAffiliation"

		String sql1 = "SELECT p.presentationID,p.eventSessionID,c.contentID,c.title,c.abstract,c.contentType,p.presentationDate, p.beginTime,p.endTime,e.location"
					+ " FROM presentation p "
					+ " JOIN eventsession e on p.eventSessionID = e.eventSessionID "
					+ " JOIN content c on c.contentID = p.contentID and c.conferenceID = 130 and c.contentType != 'Keynote' AND p.presentationDate IS NOT NULL"
//					+ " LEFT JOIN contentlink cl ON c.contentID = cl.contentID"
//					+ " JOIN authorpresenter a on a.contentID = c.contentID "
//					+ " JOIN author aa on aa.authorID = a.authorID" 
//					+ "	LEFT JOIN affiliation af on af.usernameID = aa.userID "
					+ " GROUP BY p.presentationID"
					+ " ORDER BY contentID";
		System.out.println(sql1);

			ResultSet rs1 = conn.getResultSet(sql1);

			try {
				while (rs1.next()) {
					String weekDay;
					String monthDay;
					String dayid = null;
					String affiliation = null;
					String presentationID = rs1.getString("presentationID")
							.toString();
					String contentID = rs1.getString("contentID")
							.toString();
					String eventSessionID = rs1.getString("eventSessionID")
							.toString();
					String title = rs1.getString("title").toString();
					
					if(rs1.getString("abstract") != null){
						String _abstract = rs1.getString("abstract").trim()
								.replaceAll("(?m)^\\s*$[\n\r]{1,}", "")
								.replace("\n", "").toString();						
					}else{
						String _abstract = "";
					}

					String presentationDate = rs1.getString("presentationDate")
							.toString();
					String contentType = rs1.getString("contentType")
							.toString();
					String beginTime = rs1.getString("beginTime").toString()
							.substring(11, 16);
					String endTime = rs1.getString("endTime").toString()
							.substring(11, 16);
					//String authorID = rs1.getString("authorID").toString();


					//String name = rs1.getString("name").toString();
					//System.out.println( rs1.getString("location"));
					if( rs1.getString("location") == null){
						String location = "TBD";

					}else{
						String location = rs1.getString("location").toString();
					}

					//String authors = rs1.getString("authors").toString();
					//affiliation = rs1.getString("organizationAffiliation");
					// Process date data
					int year = Integer.parseInt(presentationDate.substring(0, 4));
					int month = Integer.parseInt(presentationDate.substring(5, 7));
					int day = Integer.parseInt(presentationDate.substring(8, 10));
					int weekNumber = new DateTime(year, month, day, 0, 0)
							.getDayOfWeek();
					int monthNumber = new DateTime(year, month, day, 0, 0)
							.getMonthOfYear();

					
					switch (weekNumber) {
					case 1:
						weekDay = "Monday";
						dayid = "1";// For iconf 2013, need to change accordingly

						break;
					case 2:
						weekDay = "Tuesday";
						dayid = "2";// For iconf 2013, need to change accordingly
						break;
					case 3:
						weekDay = "Wednesday";
						dayid = "3";
						break;
					case 4:
						weekDay = "Thursday";
						dayid = "4";
						break;
					case 5:
						weekDay = "Friday";
						break;
					case 6:
						weekDay = "Saturday";
						break;
					case 7:
						weekDay = "Sunday";
						break;
					default:
						weekDay = "Not Given";
						break;
					}
					switch (monthNumber) {
					case 1:
						monthDay = "Jan.";
						break;
					case 2:
						monthDay = "Feb.";
						break;
					case 3:
						monthDay = "Mar.";
						break;
					case 4:
						monthDay = "Apr.";
						break;
					case 5:
						monthDay = "May.";
						break;
					case 6:
						monthDay = "June";
						break;
					case 7:
						monthDay = "July";
						break;
					case 8:
						monthDay = "Aug.";
						break;
					case 9:
						monthDay = "Sept.";
						break;
					case 10:
						monthDay = "Oct.";
						break;
					case 11:
						monthDay = "Nov.";
						break;
					case 12:
						monthDay = "Dec.";
						break;
					default:
						monthDay = "Not Given";
						break;
					}

					presentationDate = weekDay + ", " + monthDay + day;


					
					
					System.out.println("p = new Paper();");
					System.out.println("p.id = \"" + contentID + "\";");
					System.out.println("p.date = \"" + presentationDate + "\";");
					System.out.println("p.day_id = \"" + dayid + "\";");
					System.out.println("p.type = \"" + contentType + "\";");
					System.out.println("p.belongToSessionID = \"" + eventSessionID + "\";");
					System.out.println("p.exactbeginTime = \"" + beginTime + "\";");
					System.out.println("p.exactendTime = \"" + endTime + "\";");
					System.out.println("p.presentationID = \"" + presentationID + "\";");
					System.out.println("pList.add(p);");
					System.out.println("");

					

				}
				System.out.println("//FOR PAPERS****************");

			

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
			return s.substring(0, width - 1) + ".";
		else
			return s;

	}
}
