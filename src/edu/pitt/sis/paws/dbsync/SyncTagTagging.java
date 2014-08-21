package edu.pitt.sis.paws.dbsync;

import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class SyncTagTagging {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sql = "SELECT tt.userID,tt.contentID,t.tag " +
				"FROM tag t JOIN tagging tt ON t.tagID = tt.tagID";
		ConnectHalleyDB connH = new ConnectHalleyDB();
		ConnectWurzburgDB connW = new ConnectWurzburgDB();
		
		HashSet<String> tagHalley = new HashSet<String>();
		HashSet<String> tagWurzburg = new HashSet<String>();

		HashSet<String> tagHalley1 = new HashSet<String>();
		HashSet<String> tagWurzburg1 = new HashSet<String>();

		try {
			System.out.println("Getting tags from Halley");
			ResultSet rs = connH.getResultSet(sql);
			while(rs.next()){
				long userID = rs.getLong("userID");
				long contentID = rs.getLong("contentID");
				String tag = rs.getString("tag");
				tagHalley.add(userID + " " + contentID + " " + tag);
				tagHalley1.add(userID + " " + contentID + " " + tag);
			}
			System.out.println("Getting tags from Wurzburg");
			rs = connW.getResultSet(sql);
			while(rs.next()){
				long userID = rs.getLong("userID");
				long contentID = rs.getLong("contentID");
				String tag = rs.getString("tag");
				tagWurzburg.add(userID + " " + contentID + " " + tag);
				tagWurzburg1.add(userID + " " + contentID + " " + tag);
			}
			System.out.println("Finding Halley user-content-tags not in Wurzburg");
			for(Iterator<String> it=tagHalley.iterator();it.hasNext();){
				String u = it.next();
				if(tagWurzburg.contains(u)){
					tagHalley1.remove(u);
				}
			}
			System.out.println("There " + (tagHalley1.size() < 2?"is ":"are ") + tagHalley1.size() + (tagHalley1.size()!=0?" as belows:":""));
			if(tagHalley1.size() > 0){
				for(Iterator<String> it=tagHalley1.iterator();it.hasNext();){
					String u = it.next();
					System.out.println(u);
					String[] s = u.split("\\s+");
					if(s != null){
						long userID = Long.parseLong(s[0]);
						long contentID = Long.parseLong(s[1]);
						String tag = s[2];
						
						long tagID = -1;
						//Find tagID in Wurzburg
						sql = "SELECT tagID FROM tag WHERE tag = ?";
						PreparedStatement pstmt = connW.conn.prepareStatement(sql);
						pstmt.setString(1, tag);
						rs = pstmt.executeQuery();
						if(rs.next()){
							tagID = rs.getLong("tagID");
						}else{
							sql = "INSERT INTO tag (tag) VALUES (?)";
							pstmt.close();
							pstmt = connW.conn.prepareStatement(sql);
							pstmt.setString(1, tag);
							pstmt.executeUpdate();
							
							sql = "SELECT LAST_INSERT_ID()";
							rs = connW.getResultSet(sql);
							if(rs.next()){
								tagID = rs.getLong(1);
							}

							System.out.println("Created Wurzburg new tagID: " + tagID + " = " + tag);
						}
						
						sql = "INSERT INTO tagging (tagID,contentID,userID,tagTime) VALUES (?,?,?,NOW())";
						pstmt.close();
						pstmt = connW.conn.prepareStatement(sql);
						pstmt.setLong(1, tagID);
						pstmt.setLong(2, contentID);
						pstmt.setLong(3, userID);
						pstmt.executeUpdate();

						System.out.println("Created new Wurzburg tagging, userID: " + userID + 
								" and contentID: " + contentID + " with tagID: " + tagID + " = " + tag);
					}
				}
			}
			System.out.println("Finding Wurzburg user-content-tags not in Halley");
			for(Iterator<String> it=tagWurzburg.iterator();it.hasNext();){
				String u = it.next();
				if(tagHalley.contains(u)){
					tagWurzburg1.remove(u);
				}
			}
			System.out.println("There " + (tagWurzburg1.size()<2?"is ":"are ") + tagWurzburg1.size() + (tagWurzburg1.size()!=0?" as belows:":""));
			if(tagWurzburg1.size() > 0){
				for(Iterator<String> it=tagWurzburg1.iterator();it.hasNext();){
					String u = it.next();
					System.out.println(u);
					String[] s = u.split("\\s+");
					if(s != null){
						long userID = Long.parseLong(s[0]);
						long contentID = Long.parseLong(s[1]);
						String tag = s[2];
						
						long tagID = -1;
						//Find tagID in Halley
						sql = "SELECT tagID FROM tag WHERE tag = ?";
						PreparedStatement pstmt = connH.conn.prepareStatement(sql);
						pstmt.setString(1, tag);
						rs = pstmt.executeQuery();
						if(rs.next()){
							tagID = rs.getLong("tagID");
						}else{
							sql = "INSERT INTO tag (tag) VALUES (?)";
							pstmt.close();
							pstmt = connH.conn.prepareStatement(sql);
							pstmt.setString(1, tag);
							pstmt.executeUpdate();
							
							sql = "SELECT LAST_INSERT_ID()";
							rs = connH.getResultSet(sql);
							if(rs.next()){
								tagID = rs.getLong(1);
							}

							System.out.println("Created Halley new tagID: " + tagID + " = " + tag);
						}
						
						sql = "INSERT INTO tagging (tagID,contentID,userID,tagTime) VALUES (?,?,?,NOW())";
						pstmt.close();
						pstmt = connH.conn.prepareStatement(sql);
						pstmt.setLong(1, tagID);
						pstmt.setLong(2, contentID);
						pstmt.setLong(3, userID);
						pstmt.executeUpdate();

						System.out.println("Created new Halley tagging, userID: " + userID + 
								" and contentID: " + contentID + " with tagID: " + tagID + " = " + tag);
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
