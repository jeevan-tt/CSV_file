package com;

import java.io.FileWriter;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Main {
	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		String url = "jdbc:mysql://localhost:3306/dev";
		String username = "root";
		String password = "tiger";

		// Fetch the encrypted key from the SEC_KE_TX table
		String encryptedKey = null;
		try {
			conn = DriverManager.getConnection(url, username, password);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM SEC_KE_TX");
			if (rs.next()) {
				encryptedKey = rs.getString("encrypted_key");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Use the encrypted key to fetch data from the receipt table and store in a
		// list
		ArrayList<String[]> receiptData = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(url, username, password);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM receipt");
			while (rs.next()) {
				String[] row = new String[4];
				row[0] = rs.getString("wid");
				row[1] = rs.getString("quantity");
				row[2] = rs.getString("unit");
				row[3] = rs.getString("market_price");
				receiptData.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}