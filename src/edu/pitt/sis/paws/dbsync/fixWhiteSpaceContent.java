package edu.pitt.sis.paws.dbsync;

import java.sql.*;

public class fixWhiteSpaceContent {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sql = "SELECT contentID,abstract " +
						"FROM content " +
						"WHERE conferenceID = 64 "/* +
						"AND " +
						"(LENGTH(TRIM(DOI)) > 0 OR LENGTH(TRIM(contentLink)) > 0)"*/;
		ConnectHalleyDB conn = new ConnectHalleyDB();

		try {
			System.out.println("Getting contents from cn3fromcn20");
			ResultSet rs = conn.getResultSet(sql);
			while(rs.next()){
				long contentID = rs.getLong("contentID");
				String _abstract = rs.getString("abstract");
				_abstract = _abstract.replaceAll("\n", "<br/>");
				
				sql = "UPDATE content SET abstract = ? WHERE contentID = ?";
				PreparedStatement pstmt = conn.conn.prepareStatement(sql);
				pstmt.setString(1, _abstract);
				pstmt.setLong(2, contentID);
				pstmt.executeUpdate();
			}
			conn.conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
