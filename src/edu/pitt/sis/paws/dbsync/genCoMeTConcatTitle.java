package edu.pitt.sis.paws.dbsync;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class genCoMeTConcatTitle {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConnectHalleyDB conn = new ConnectHalleyDB();
		String sql = "SELECT col_id,TRIM(LOWER(title)) _title FROM colloquia.colloquium WHERE concattitle IS NULL";
		ResultSet rs = conn.getResultSet(sql);
		try {
			while(rs.next()){
				long col_id = rs.getLong("col_id");
				String _title = rs.getString("_title");
				
				_title = _title.replaceAll("\\s+", "");
				
				sql = "UPDATE colloquia.colloquium SET concattitle = ? WHERE col_id = ?";
				PreparedStatement pstmt = conn.conn.prepareStatement(sql);
				pstmt.setString(1, _title);
				pstmt.setLong(2, col_id);
				pstmt.executeUpdate();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
