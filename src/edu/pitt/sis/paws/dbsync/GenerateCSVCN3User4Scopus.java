package edu.pitt.sis.paws.dbsync;

import java.sql.*;

public class GenerateCSVCN3User4Scopus {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sql = "SELECT name,organizationAffiliation " +
						"FROM user4iconfpaper2011 " +
						"WHERE ScopusAuthorID IS NULL AND LENGTH(TRIM(organizationAffiliation)) > 0";
		ConnectHalleyDB conn = new ConnectHalleyDB();

		try {
			ResultSet rs = conn.getResultSet(sql);
			while(rs.next()){
				String name = rs.getString("name");
				String affiliation = rs.getString("organizationAffiliation");
				
				String[] n = name.split("\\s+");
				if(n != null){
					String firstname = "";
					String middlename = "";
					String lastname = "";
					if(n.length >= 3){
						firstname = n[0];
						middlename = n[1];
						lastname = n[2];
					}else if(n.length == 2){
						firstname = n[0];
						lastname = n[1];
					}else if(n.length == 1){
						firstname = n[0];
					}
					System.out.println(firstname + "," + middlename + "," + lastname + "," + affiliation);
				}
			}
			conn.conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
