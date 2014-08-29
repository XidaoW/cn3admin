package edu.pitt.sis.paws.dbsync;

import org.joda.time.DateTime;

//import data.Poster;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class generateWorkshopForCNMobilie {
	@SuppressWarnings("null")
	public static void main(String[] args) throws IOException {

		ConnectHalleyDB conn = new ConnectHalleyDB();
		//For Keynote
		
			//FOR Sessions
			String sql1 = "SELECT e.eventSessionID,e.sessionName,e.sessionDate,e.beginTime,e.endTime,e.location FROM eventsession e " 
					+ "JOIN presentation p on e.eventSessionID = p.eventSessionID " 
					+ "JOIN content c on p.contentID = c.contentID and c.contentType LIKE '%Workshop%' and c.conferenceID = 130 "
					+ "GROUP BY eventSessionID";

			ResultSet rs1 = conn.getResultSet(sql1);
			int i = 0;
			try {
				while (rs1.next()) {
					String weekDay;
					String monthDay;
					String dayid = null;


					
					String childsessionID = rs1.getString("eventSessionID")
							.toString();

					String date = rs1.getString("sessionDate")//sessionDate//presentationDate
							.toString();
					String name = rs1.getString("sessionName")
							.toString();
					
					String beginTime = rs1.getString("beginTime").toString()
							.substring(11, 16);
					String endTime = rs1.getString("endTime").toString()
							.substring(11, 16);

					String room = rs1.getString("location");
					//String content = rs1.getString("abstract");
					//content = content.replaceAll("\"", "");
					//content = content.replaceAll("(\r\n|\n|\r)", "<br />");
					// Process date data
					int year = Integer.parseInt(date.substring(0, 4));
					int month = Integer.parseInt(date.substring(5, 7));
					int day = Integer.parseInt(date.substring(8, 10));
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

					date = weekDay + ", " + monthDay + day;
					

					i++;
					System.out.println("s = new Workshop();");
					System.out.println("s.ID = \"" + i + "\";");
					System.out.println("s.name = \"" + name + "\";");
					System.out.println("s.date = \"" + date + "\";");
					System.out.println("s.day_id = \"" + dayid + "\";");
					System.out.println("s.beginTime = \"" + beginTime + "\";");
					System.out.println("s.endTime = \"" + endTime + "\";");
					System.out.println("s.room = \"" + room + "\";");
					System.out.println("s.content = \"<p>" + "" + "</p>\";");
					System.out.println("s.childsessionID = \"" + childsessionID + "\";");

					
					
					
					System.out.println("wList.add(s);");
					System.out.println("");

					

				}
			
			

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
