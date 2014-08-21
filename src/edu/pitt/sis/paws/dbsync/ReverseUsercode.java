package edu.pitt.sis.paws.dbsync;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import edu.pitt.sis.paws.dbsync.StringEncrypter.EncryptionException;

public class ReverseUsercode {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sql = "SELECT userID,passcode FROM userinfo";
		ConnectHalleyDB conn = new ConnectHalleyDB();

		String encryptionScheme = StringEncrypter.DESEDE_ENCRYPTION_SCHEME;		
		StringEncrypter encrypter;
		try {
			encrypter = new StringEncrypter(encryptionScheme, BasicFunctions.encKey);
			ResultSet rs = conn.getResultSet(sql);
			while(rs.next()){
				long userID = rs.getLong("userID");
				String encryptedPassword = rs.getString("passcode");
				if(encryptedPassword.trim().length() > 0){
					String decryptedPassword = encrypter.decrypt(encryptedPassword);
					
					sql = "INSERT INTO reversedpassCode (userID,origpassCode,rawpassCode) VALUES (?,?,?)";
					PreparedStatement pstmt = conn.conn.prepareStatement(sql);
					pstmt.setLong(1, userID);
					pstmt.setString(2, encryptedPassword);
					pstmt.setString(3, decryptedPassword);
					pstmt.executeUpdate();
				}else{
					sql = "INSERT INTO reversedpassCode (userID,origpassCode) VALUES (?,?)";
					PreparedStatement pstmt = conn.conn.prepareStatement(sql);
					pstmt.setLong(1, userID);
					pstmt.setString(2, encryptedPassword);
					pstmt.executeUpdate();
				}
			}
			conn.conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncryptionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
