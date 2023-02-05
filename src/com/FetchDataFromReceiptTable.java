package com;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class FetchDataFromReceiptTable {

    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        // Connect to the database
        String url = "jdbc:mysql://localhost:3306/dev";
        String username = "root";
        String password = "tiger";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList<String[]> data = new ArrayList<String[]>();
        try {
            conn = DriverManager.getConnection(url, username, password);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM receipt");
            while (rs.next()) {
                String[] row = new String[4];
                row[0] = String.valueOf(rs.getInt("wid"));
                row[1] = rs.getString("quantity");
                row[2] = String.valueOf(rs.getInt("unit"));
                row[3] = String.valueOf(rs.getDouble("market_price"));
                data.add(row);
            }
            System.out.println("  =");
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

        // Write the data to a CSV file
        try {
            FileWriter writer = new FileWriter("receipt_data.csv");
            writer.append("wid");
            writer.append(",");
            writer.append("quantity");
            writer.append(",");
            writer.append("unit");
            writer.append(",");
            writer.append("market_price");
            writer.append("\n");
            for (String[] row : data) {
                writer.append(row[0]);
                writer.append(",");
                writer.append(row[1]);
                writer.append(",");
                writer.append(row[2]);
                writer.append(",");
                writer.append(row[3]);
                writer.append("\n");
            }
           

            writer.flush();
            writer.close();
            String key = "secret-key";
            String inputFile = "employee-details.csv";
            String outputFile = "employee-details-encrypted.csv";

            EncryptFile.encrypt(key, inputFile, outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
