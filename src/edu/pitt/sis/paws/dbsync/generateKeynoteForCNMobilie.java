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

public class generateKeynoteForCNMobilie {
	@SuppressWarnings("null")
	public static void main(String[] args) throws IOException {

		ConnectHalleyDB conn = new ConnectHalleyDB();
		//For Keynote
		System.out.println("//FOR KEYNOTE****************");
		String sql = "SELECT p.presentationID,p.eventSessionID,c.contentID,c.title,c.abstract,p.presentationDate, p.beginTime,p.endTime,a.authorID,aa.`name`,e.location,GROUP_CONCAT(DISTINCT aa.`name` ORDER BY a.authorNo ASC SEPARATOR ', ') AS authors, aa.userID,af.organizationAffiliation"
				+ " FROM presentation p "
				+ " JOIN eventsession e on p.eventSessionID = e.eventSessionID "
				+ " JOIN content c on c.contentID = p.contentID and c.conferenceID = 130 and c.contentType = 'Keynote' "
				+ " JOIN authorpresenter a on a.contentID = c.contentID "
				+ " JOIN author aa on aa.authorID = a.authorID" 
				+ "	LEFT JOIN affiliation af on af.usernameID = aa.userID "
				+ " GROUP BY p.presentationID";

		ResultSet rs = conn.getResultSet(sql);

		try {
			while (rs.next()) {
				String weekDay;
				String monthDay;
				String dayid = null;
				String affiliation = null;
				String presentationID = rs.getString("presentationID")
						.toString();
				String contentID = rs.getString("contentID")
						.toString();
				String eventSessionID = rs.getString("eventSessionID")
						.toString();
				String title = rs.getString("title").toString();
				String _abstract = rs.getString("abstract").trim()
						.replaceAll("(?m)^\\s*$[\n\r]{1,}", "")
						.replace("\n", "").toString();
				String presentationDate = rs.getString("presentationDate")
						.toString();
				String beginTime = rs.getString("beginTime").toString()
						.substring(11, 16);
				String endTime = rs.getString("endTime").toString()
						.substring(11, 16);
				String authorID = rs.getString("authorID").toString();
				String name = rs.getString("name").toString();
				String location = rs.getString("location").toString();
				String authors = rs.getString("authors").toString();
				affiliation = rs.getString("organizationAffiliation");
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

				System.out.println("k = new Keynote();");

				System.out.println("k.ID = \"" + eventSessionID + "\";");
				System.out.println("k.title = \"" + title + "\";");
				System.out.println("k.description = \"" + _abstract + "\";");
				System.out.println("k.date = \"" + presentationDate + "\";");
				System.out.println("k.dayid = \"" + dayid + "\";");
				System.out.println("k.beginTime = \"" + beginTime + "\";");
				System.out.println("k.endTime = \"" + endTime + "\";");		
				System.out.println("k.room = \"" + location + "\";");
				System.out.println("k.speakerName = \"" + authors + "\";");
				if(affiliation == "null"){
					System.out.println("k.speakerAffiliation = \"" + affiliation + "\";");
				}else{
					System.out.println("k.speakerAffiliation = \"\";");

				}
		

				System.out.println("kList.add(k);");
				System.out.println("");

				

			}
			System.out.println("//KEYNOTE ENDS****************");
			
			
			
			

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
