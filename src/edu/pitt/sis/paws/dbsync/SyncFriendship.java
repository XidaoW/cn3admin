package edu.pitt.sis.paws.dbsync;

import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class SyncFriendship {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sql = "SELECT requestingUser,respondingUser,requestStatus " +
				"FROM friendship";
		ConnectHalleyDB connH = new ConnectHalleyDB();
		ConnectWurzburgDB connW = new ConnectWurzburgDB();
		HashSet<String> friendshipHalley = new HashSet<String>();
		HashSet<String> friendshipWurzburg = new HashSet<String>();

		HashSet<String> friendshipHalley1 = new HashSet<String>();
		HashSet<String> friendshipWurzburg1 = new HashSet<String>();

		try {
			System.out.println("Getting bookmarkings from Halley");
			ResultSet rs = connH.getResultSet(sql);
			while(rs.next()){
				long requestingUser = rs.getLong("requestingUser");
				long respondingUser = rs.getLong("respondingUser");
				int requestStatus = rs.getInt("requestStatus");
				friendshipHalley.add(requestingUser + " " + respondingUser + " " + requestStatus);
				friendshipHalley1.add(requestingUser + " " + respondingUser + " " + requestStatus);
			}
			System.out.println("Getting bookmarkings from Wurzburg");
			rs = connW.getResultSet(sql);
			while(rs.next()){
				long requestingUser = rs.getLong("requestingUser");
				long respondingUser = rs.getLong("respondingUser");
				int requestStatus = rs.getInt("requestStatus");
				friendshipWurzburg.add(requestingUser + " " + respondingUser + " " + requestStatus);
				friendshipWurzburg1.add(requestingUser + " " + respondingUser + " " + requestStatus);
			}
			System.out.println("Finding Halley friendships not in Wurzburg");
			for(Iterator<String> it=friendshipHalley.iterator();it.hasNext();){
				String u = it.next();
				if(friendshipWurzburg.contains(u)){
					friendshipHalley1.remove(u);
				}
			}
			System.out.println("There " + (friendshipHalley1.size() < 2?"is ":"are ") + friendshipHalley1.size() + (friendshipHalley1.size()!=0?" as belows:":""));
			if(friendshipHalley1.size() > 0){
				for(Iterator<String> it=friendshipHalley1.iterator();it.hasNext();){
					String u = it.next();
					System.out.println(u);
					String[] f = u.split("\\s+");
					long requestingUser = Long.parseLong(f[0]);
					long respondingUser = Long.parseLong(f[1]);
					int requestStatus = Integer.parseInt(f[2]);
					sql = "SELECT requestStatus FROM friendship WHERE requestingUser = ? AND respondingUser = ?";
					PreparedStatement pstmt = connW.conn.prepareStatement(sql);
					pstmt.setLong(1, requestingUser);
					pstmt.setLong(2, respondingUser);
					rs = pstmt.executeQuery();
					if(rs.next()){
						int wrequestStatus = rs.getInt("requestStatus");
						if(requestStatus > wrequestStatus){
							sql = "UDPATE friendship SET requestStatus = ? WHERE requestingUser = ? AND respondingUser = ?";
							if(requestStatus == 3){
								sql = "UDPATE friendship SET requestStatus = ?,acceptanceDate = NOW() WHERE requestingUser = ? AND respondingUser = ?";
							}
							pstmt.close();
							pstmt = connW.conn.prepareStatement(sql);
							pstmt.setInt(1, requestStatus);
							pstmt.setLong(2, requestingUser);
							pstmt.setLong(3, respondingUser);
							pstmt.executeUpdate();
							System.out.println("Updated Wurzburg friendship, requestingUser: " + requestingUser + 
									" and respondingUser: " + respondingUser + " with requestStatus: " + requestStatus);
						}
					}else{
						sql = "INSERT INTO friendship (requestingUser,respondingUser,requestDate,requestStatus) VALUES " +
								"(?,?,NOW(),?)";
						if(requestStatus == 3){
							sql = "INSERT INTO friendship (requestingUser,respondingUser,requestDate,acceptanceDate,requestStatus) VALUES " +
									"(?,?,NOW(),NOW(),?)";
						}
						pstmt.close();
						pstmt = connW.conn.prepareStatement(sql);
						pstmt.setInt(1, requestStatus);
						pstmt.setLong(2, requestingUser);
						pstmt.setLong(3, respondingUser);
						pstmt.executeUpdate();
						System.out.println("Created Wurzburg friendship, requestingUser: " + requestingUser + 
								" and respondingUser: " + respondingUser + " with requestStatus: " + requestStatus);
					}
				}
			}
			System.out.println("Finding Wurzburg friendships not in Halley");
			for(Iterator<String> it=friendshipWurzburg.iterator();it.hasNext();){
				String u = it.next();
				if(friendshipHalley.contains(u)){
					friendshipWurzburg1.remove(u);
				}
			}
			System.out.println("There " + (friendshipWurzburg1.size()<2?"is ":"are ") + friendshipWurzburg1.size() + (friendshipWurzburg1.size()!=0?" as belows:":""));
			if(friendshipWurzburg1.size() > 0){
				for(Iterator<String> it=friendshipWurzburg1.iterator();it.hasNext();){
					String u = it.next();
					System.out.println(u);
					String[] f = u.split("\\s+");
					long requestingUser = Long.parseLong(f[0]);
					long respondingUser = Long.parseLong(f[1]);
					int requestStatus = Integer.parseInt(f[2]);
					sql = "SELECT requestStatus FROM friendship WHERE requestingUser = ? AND respondingUser = ?";
					PreparedStatement pstmt = connH.conn.prepareStatement(sql);
					pstmt.setLong(1, requestingUser);
					pstmt.setLong(2, respondingUser);
					rs = pstmt.executeQuery();
					if(rs.next()){
						int wrequestStatus = rs.getInt("requestStatus");
						if(requestStatus > wrequestStatus){
							sql = "UDPATE friendship SET requestStatus = ? WHERE requestingUser = ? AND respondingUser = ?";
							if(requestStatus == 3){
								sql = "UDPATE friendship SET requestStatus = ?,acceptanceDate = NOW() WHERE requestingUser = ? AND respondingUser = ?";
							}
							pstmt.close();
							pstmt = connH.conn.prepareStatement(sql);
							pstmt.setInt(1, requestStatus);
							pstmt.setLong(2, requestingUser);
							pstmt.setLong(3, respondingUser);
							pstmt.executeUpdate();
							System.out.println("Updated Wurzburg friendship, requestingUser: " + requestingUser + 
									" and respondingUser: " + respondingUser + " with requestStatus: " + requestStatus);
						}
					}else{
						sql = "INSERT INTO friendship (requestingUser,respondingUser,requestDate,requestStatus) VALUES " +
								"(?,?,NOW(),?)";
						if(requestStatus == 3){
							sql = "INSERT INTO friendship (requestingUser,respondingUser,requestDate,acceptanceDate,requestStatus) VALUES " +
									"(?,?,NOW(),NOW(),?)";
						}
						pstmt.close();
						pstmt = connH.conn.prepareStatement(sql);
						pstmt.setInt(1, requestStatus);
						pstmt.setLong(2, requestingUser);
						pstmt.setLong(3, respondingUser);
						pstmt.executeUpdate();
						System.out.println("Created Halley friendship, requestingUser: " + requestingUser + 
								" and respondingUser: " + respondingUser + " with requestStatus: " + requestStatus);
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
