package org;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

public class WriteReadCSV {
	public static void main(String[] args) throws SQLException, IOException {
		// Connect to the database
		String url = "jdbc:mysql://localhost:3306/dev";
		String username = "root";
		String password = "tiger";
		Connection con = DriverManager.getConnection(url, username, password);
		Statement stmt = con.createStatement();

		// Fetch data from the receipt table
		List<Receipt> receipts = new ArrayList<>();
		try {
			ResultSet rs = stmt.executeQuery("SELECT wid, quantity, unit, market_price FROM receipt");
			while (rs.next()) {
				int wid = rs.getInt("wid");
				String quantity = rs.getString("quantity");
				int unit = rs.getInt("unit");
				int marketPrice = rs.getInt("market_price");
				receipts.add(new Receipt(wid, quantity, unit, marketPrice));
			}
		} catch (SQLException e) {
			try (FileWriter writer = new FileWriter("error_report.txt")) {
				writer.write(e.getMessage());
			} catch (IOException ioe) {
				System.out.println("Error writing to error report: " + ioe.getMessage());
			}
			return;
		}

		// Write the list of data to a CSV file
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
			System.out.println("👍");
		} catch (IOException e) {
			try (FileWriter writer = new FileWriter("error_report.txt")) {
				writer.write(e.getMessage());
			} catch (IOException ioe) {
				System.out.println("Error writing to error report: " + ioe.getMessage());
			}
		}

		// Close the database connection
		con.close();
	}
}
