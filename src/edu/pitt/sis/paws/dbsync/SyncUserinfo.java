package edu.pitt.sis.paws.dbsync;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class SyncUserinfo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sql = "SELECT userID,name,email,passcode FROM userinfo";
		ConnectHalleyDB connH = new ConnectHalleyDB();
		ConnectWurzburgDB connW = new ConnectWurzburgDB();
		HashMap<String,String> userHalley = new HashMap<String,String>();
		HashMap<String,String> userWurzburg = new HashMap<String,String>();

		HashSet<String> userHalley1 = new HashSet<String>();
		HashSet<String> userWurzburg1 = new HashSet<String>();

		try {
			System.out.println("Getting users from Halley");
			ResultSet rs = connH.getResultSet(sql);
			while(rs.next()){
				long userID = rs.getLong("userID");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String passcode = rs.getString("passcode");
				userHalley.put(userID + " " +email + " " + passcode,name);
				userHalley1.add(userID + " " + email + " " + passcode);
			}
			System.out.println("Getting users from Wurzburg");
			rs = connW.getResultSet(sql);
			while(rs.next()){
				long userID = rs.getLong("userID");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String passcode = rs.getString("passcode");
				userWurzburg.put(userID + " " + email + " " + passcode,name);
				userWurzburg1.add(userID + " " + email + " " + passcode);
			}
			System.out.println("Finding Halley users not in Wurzburg");
			for(Iterator<String> it=userHalley.keySet().iterator();it.hasNext();){
				String u = it.next();
				if(userWurzburg.keySet().contains(u)){
					userHalley1.remove(u);
				}
			}
			System.out.println("There are " + userHalley1.size() + (userHalley1.size()!=0?" as belows:":""));
			if(userHalley1.size() > 0){
				for(Iterator<String> it=userHalley1.iterator();it.hasNext();){
					String u = it.next();
					System.out.println(u);
					String[] s = u.split("\\s+");
					if(s != null){
						long userID = Long.parseLong(s[0]);
						String email = s[1];
						String passcode = s[2];

						if(email.trim().equalsIgnoreCase("null")){
							email = null;
						}
						
						//Does this user already exist in Wurzburg
						sql = "SELECT passcode FROM userinfo WHERE userID = ?";
						PreparedStatement pstmt = connW.conn.prepareStatement(sql);
						pstmt.setLong(1,userID);
						rs = pstmt.executeQuery();
						if(rs.next()){//Found => Update passcode
							String wpasscode = rs.getString("passcode");
							if(wpasscode == null ||
									wpasscode.equalsIgnoreCase("gA6ZA5ilgHrQZGQeWykiSA==") ||
									wpasscode.trim().length() == 0){
								sql = "UPDATE userinfo SET passcode = ? WHERE userID = ?";
								pstmt.close();
								pstmt = connW.conn.prepareStatement(sql);
								pstmt.setString(1, passcode);
								pstmt.setLong(2, userID);
								pstmt.executeUpdate();
								System.out.println("Updated Wurzburg userID: " + userID + ", email: " + email + " with passcode: " + passcode);
							}else{
								//Passcode is not the same, need to check the timestamp
								System.out.println("Conflict of password!!!");
								System.out.println("Halley: " + u);
								System.out.println("Wurzburg: "+ userID + " " + email + " " + wpasscode);
							}
						}else{//Not found => Insert
							sql = "INSERT INTO userinfo (userID,name,email,passcode,userroleID,guest,username) VALUES (?,?,?,?,1,0,?)";
							pstmt.close();
							pstmt = connW.conn.prepareStatement(sql);
							pstmt.setLong(1, userID);
							pstmt.setString(2, userHalley.get(u));
							pstmt.setString(3, email);
							pstmt.setString(4, passcode);
							pstmt.setString(5, userHalley.get(u));
							pstmt.executeUpdate();
							System.out.println("Created new Wurzburg userID: " + userID + ", email: " + email + " with passcode: " + passcode);
						}
					}
				}
			}
			System.out.println("Finding Wurzburg users not in Halley");
			for(Iterator<String> it=userWurzburg.keySet().iterator();it.hasNext();){
				String u = it.next();
				if(userHalley.keySet().contains(u)){
					userWurzburg1.remove(u);
				}
			}
			System.out.println("There are " + userWurzburg1.size() + (userWurzburg1.size()!=0?" as belows:":""));
			if(userWurzburg1.size() > 0){
				for(Iterator<String> it=userWurzburg1.iterator();it.hasNext();){
					String u = it.next();
					System.out.println(u);
					String[] s = u.split("\\s+");
					if(s != null){
						long userID = Long.parseLong(s[0]);
						String email = s[1];
						String passcode = s[2];
						
						if(email.trim().equalsIgnoreCase("null")){
							email = null;
						}
						
						//Does this user already exist in Halley
						sql = "SELECT passcode FROM userinfo WHERE userID = ?";
						PreparedStatement pstmt = connH.conn.prepareStatement(sql);
						pstmt.setLong(1,userID);
						rs = pstmt.executeQuery();
						if(rs.next()){//Found => Update passcode
							String hpasscode = rs.getString("passcode");
							if(hpasscode == null ||
									hpasscode.equalsIgnoreCase("gA6ZA5ilgHrQZGQeWykiSA==") ||
									hpasscode.trim().length() == 0){
								sql = "UPDATE userinfo SET passcode = ? WHERE userID = ?";
								pstmt.close();
								pstmt = connH.conn.prepareStatement(sql);
								pstmt.setString(1, passcode);
								pstmt.setLong(2, userID);
								pstmt.executeUpdate();
								System.out.println("Updated Halley userID: " + userID + ", email: " + email + " with passcode: " + passcode);
							}else{
								//Passcode is not the same, need to check the timestamp
								System.out.println("Conflict of password!!!");
								System.out.println("Wurzburg: " + u);
								System.out.println("Halley: " + userID + " " + email + " " + hpasscode);
							}
						}else{//Not found => Insert
							sql = "INSERT INTO userinfo (userID,name,email,passcode,userroleID,guest,username) VALUES (?,?,?,?,1,0,?)";
							pstmt.close();
							pstmt = connH.conn.prepareStatement(sql);
							pstmt.setLong(1, userID);
							pstmt.setString(2, userWurzburg.get(u));
							pstmt.setString(3, email);
							pstmt.setString(4, passcode);
							pstmt.setString(5, userWurzburg.get(u));
							pstmt.executeUpdate();
							System.out.println("Created new Halley userID: " + userID + ", email: " + email + " with passcode: " + passcode);
						}
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
