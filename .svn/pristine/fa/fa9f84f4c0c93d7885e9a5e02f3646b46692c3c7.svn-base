package edu.pitt.sis.paws.dbsync;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class SyncBookmarking {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sql = "SELECT b.contentID,b.userID " +
				"FROM bookmarking b JOIN content c ON b.contentID = c.contentID " +
				"WHERE c.conferenceID = 54 " +
				"GROUP BY b.contentID,b.userID";
		ConnectHalleyDB connH = new ConnectHalleyDB();
		ConnectWurzburgDB connW = new ConnectWurzburgDB();
		HashSet<String> bookmarkingHalley = new HashSet<String>();
		HashSet<String> bookmarkingWurzburg = new HashSet<String>();

		HashSet<String> bookmarkingHalley1 = new HashSet<String>();
		HashSet<String> bookmarkingWurzburg1 = new HashSet<String>();

		try {
			System.out.println("Getting bookmarkings from Halley");
			ResultSet rs = connH.getResultSet(sql);
			while(rs.next()){
				String contentID = rs.getString("contentID");
				String userID = rs.getString("userID");
				bookmarkingHalley.add(contentID + " " + userID);
				bookmarkingHalley1.add(contentID + " " + userID);
			}
			System.out.println("Getting bookmarkings from Wurzburg");
			rs = connW.getResultSet(sql);
			while(rs.next()){
				String contentID = rs.getString("contentID");
				String userID = rs.getString("userID");
				bookmarkingWurzburg.add(contentID + " " + userID);
				bookmarkingWurzburg1.add(contentID + " " + userID);
			}
			System.out.println("Finding Halley bookmarkings not in Wurzburg");
			for(Iterator<String> it=bookmarkingHalley.iterator();it.hasNext();){
				String u = it.next();
				if(bookmarkingWurzburg.contains(u)){
					bookmarkingHalley1.remove(u);
				}
			}
			System.out.println("There " + (bookmarkingHalley1.size() < 2?"is ":"are ") + bookmarkingHalley1.size() + (bookmarkingHalley1.size()!=0?" as belows:":""));
			if(bookmarkingHalley1.size() > 0){
				for(Iterator<String> it=bookmarkingHalley1.iterator();it.hasNext();){
					String u = it.next();
					System.out.println(u);
					String[] s = u.split("\\s+");
					if(s != null){
						String contentID = s[0];
						String userID = s[1];

						sql = "INSERT INTO bookmarking (contentID,userID,created) VALUES (?,?,NOW())";
						PreparedStatement pstmt = connW.conn.prepareStatement(sql);
						pstmt.setLong(1, Long.parseLong(contentID));
						pstmt.setLong(2, Long.parseLong(userID));
						pstmt.executeUpdate();
						System.out.println("Created new Wurzburg bookmarking, userID: " + userID + " with contentID: " + contentID);
					}
				}
			}
			System.out.println("Finding Wurzburg bookmarkings not in Halley");
			for(Iterator<String> it=bookmarkingWurzburg.iterator();it.hasNext();){
				String u = it.next();
				if(bookmarkingHalley.contains(u)){
					bookmarkingWurzburg1.remove(u);
				}
			}
			System.out.println("There " + (bookmarkingWurzburg1.size()<2?"is ":"are ") + bookmarkingWurzburg1.size() + (bookmarkingWurzburg1.size()!=0?" as belows:":""));
			if(bookmarkingWurzburg1.size() > 0){
				for(Iterator<String> it=bookmarkingWurzburg1.iterator();it.hasNext();){
					String u = it.next();
					System.out.println(u);
					String[] s = u.split("\\s+");
					if(s != null){
						String contentID = s[0];
						String userID = s[1];

						sql = "INSERT INTO bookmarking (contentID,userID,created) VALUES (?,?,NOW())";
						PreparedStatement pstmt = connH.conn.prepareStatement(sql);
						pstmt.setLong(1, Long.parseLong(contentID));
						pstmt.setLong(2, Long.parseLong(userID));
						pstmt.executeUpdate();
						System.out.println("Created new Halley bookmarking, userID: " + userID + " with contentID: " + contentID);
					}
				}
			}
			connH.conn.close();
			connW.conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
