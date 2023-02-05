package com;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class WriteCSVWithEncryption {
    public static void main(String[] args) throws SQLException, IOException, Exception {
        // Connect to the database
        String url = "jdbc:mysql://localhost:3306/dev";
        String username = "root";
        String password = "tiger";
        Connection con = DriverManager.getConnection(url, username, password);
        Statement stmt = con.createStatement();
        
        // Fetch data from the receipt table
        ResultSet rs = stmt.executeQuery("SELECT wid, quantity, unit, market_price FROM receipt");
        List<Receipt> receipts = new ArrayList<>();
        while (rs.next()) {
            int wid = rs.getInt("wid");
            String quantity = rs.getString("quantity");
            int unit = rs.getInt("unit");
            int marketPrice = rs.getInt("market_price");
            receipts.add(new Receipt(wid, quantity, unit, marketPrice));
        }
        
        // Fetch the encrypted key from the SEC_KE_TX table
        ResultSet keyResult = stmt.executeQuery("SELECT encrypted_key FROM SEC_KE_TX");
        byte[] encryptedKey = null;
        if (keyResult.next()) {
            encryptedKey = keyResult.getBytes("encrypted_key");
        }
        
        // Decrypt the encrypted key
		/*
		 * SecretKeySpec secretKey = new SecretKeySpec(encryptedKey, "AES"); Cipher
		 * cipher = Cipher.getInstance("AES"); cipher.init(Cipher.DECRYPT_MODE,
		 * secretKey);
		 */
		/* byte[] decryptedKey = cipher.doFinal(encryptedKey); */
        
        // Write the list of data to a CSV file while encrypting the file
        try (FileWriter writer = new FileWriter("receipts.csv")) {
            writer.append("wid");
            writer.append(",");
            writer.append("quantity");
            writer.append(",");
            writer.append("unit");
            writer.append(",");
            writer.append("market_price");
            writer.append("\n");
            for (Receipt receipt : receipts) {
                writer.append(String.valueOf(receipt.wid));
                writer.append(",");
                writer.append(receipt.quantity);
                writer.append(",");
                writer.append(String.valueOf(receipt.unit));
                writer.append(",");
                writer.append(String.valueOf(receipt.marketPrice));
                writer.append("\n");
            }
        }
        
        // Close the database connection
        con.close();
    }
}

class Receipt {
    int wid;
    String quantity;
    int unit;
    int marketPrice;
	public Receipt(int wid, String quantity, int unit, int marketPrice) {
		super();
		this.wid = wid;
		this.quantity = quantity;
		this.unit = unit;
		this.marketPrice = marketPrice;
	}


}