package edu.pitt.sis.paws.dbsync;

import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class SyncFollow {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sql = "SELECT followerID,followeeID " +
				"FROM follow";
		ConnectHalleyDB connH = new ConnectHalleyDB();
		ConnectWurzburgDB connW = new ConnectWurzburgDB();
		HashSet<String> followHalley = new HashSet<String>();
		HashSet<String> followWurzburg = new HashSet<String>();

		HashSet<String> followHalley1 = new HashSet<String>();
		HashSet<String> followWurzburg1 = new HashSet<String>();

		try {
			System.out.println("Getting follows from Halley");
			ResultSet rs = connH.getResultSet(sql);
			while(rs.next()){
				long followerID = rs.getLong("followerID");
				long followeeID = rs.getLong("followeeID");
				followHalley.add(followerID + " " + followeeID);
				followHalley1.add(followerID + " " + followeeID);
			}
			System.out.println("Getting follows from Wurzburg");
			rs = connW.getResultSet(sql);
			while(rs.next()){
				long followerID = rs.getLong("followerID");
				long followeeID = rs.getLong("followeeID");
				followWurzburg.add(followerID + " " + followeeID);
				followWurzburg1.add(followerID + " " + followeeID);
			}
			System.out.println("Finding Halley follows not in Wurzburg");
			for(Iterator<String> it=followHalley.iterator();it.hasNext();){
				String u = it.next();
				if(followWurzburg.contains(u)){
					followHalley1.remove(u);
				}
			}
			System.out.println("There " + (followHalley1.size() < 2?"is ":"are ") + followHalley1.size() + (followHalley1.size()!=0?" as belows:":""));
			if(followHalley1.size() > 0){
				for(Iterator<String> it=followHalley1.iterator();it.hasNext();){
					String u = it.next();
					System.out.println(u);
					String[] f = u.split("\\s+");
					long followerID = Long.parseLong(f[0]);
					long followeeID = Long.parseLong(f[1]);
					sql = "INSERT INTO follow (followerID,followeeID,`date`) VALUES " +
							"(?,?,NOW())";
					PreparedStatement pstmt = connW.conn.prepareStatement(sql);
					pstmt.setLong(1, followerID);
					pstmt.setLong(2, followeeID);
					pstmt.executeUpdate();
					System.out.println("Created Wurzburg follow, followerID: " + followerID + 
							" and followeeID: " + followeeID);
				}
			}
			System.out.println("Finding Wurzburg follows not in Halley");
			for(Iterator<String> it=followWurzburg.iterator();it.hasNext();){
				String u = it.next();
				if(followHalley.contains(u)){
					followWurzburg1.remove(u);
				}
			}
			System.out.println("There " + (followWurzburg1.size()<2?"is ":"are ") + followWurzburg1.size() + (followWurzburg1.size()!=0?" as belows:":""));
			if(followWurzburg1.size() > 0){
				for(Iterator<String> it=followWurzburg1.iterator();it.hasNext();){
					String u = it.next();
					System.out.println(u);
					String[] f = u.split("\\s+");
					long followerID = Long.parseLong(f[0]);
					long followeeID = Long.parseLong(f[1]);
					sql = "INSERT INTO follow (followerID,followeeID,`date`) VALUES " +
							"(?,?,NOW())";
					PreparedStatement pstmt = connH.conn.prepareStatement(sql);
					pstmt.setLong(1, followerID);
					pstmt.setLong(2, followeeID);
					pstmt.executeUpdate();
					System.out.println("Created Halley follow, followerID: " + followerID + 
							" and followeeID: " + followeeID);
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
