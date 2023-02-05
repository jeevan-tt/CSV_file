package org;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.util.StringUtils;

public class ReadCSVAndUpdateReceiptTable {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
		// Connect to the database
		String url = "jdbc:mysql://localhost:3306/dev";
		String username = "root";
		String password = "tiger";
		Connection con = DriverManager.getConnection(url, username, password);
		Statement stmt = con.createStatement();

		// Read the receipts.csv file
		String csvFile = "receipts.csv";

		String line = "";
		String cvsSplitBy = ",";
		List<Receipt> notPresentInReceiptTable = new ArrayList<>();
		Receipt receiptdto = new Receipt();
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(csvFile));
		int lineNumber = 0;
		int wid = 0;
		int unit = 0;
		String quantity = null;
		int marketPrice = 0;
		String marketValueStr = null;
		String unitValueStr = null;
		List<Receipt> blankContentReceiptTable = new ArrayList<>();
		List<Receipt> datTypeMisMatchReceiptTable = new ArrayList<>();

		String[] blankDetails = null;
		try {
			while ((line = br.readLine()) != null) {
				lineNumber++;
				if (lineNumber == 1) {
					// Skip the header line
					continue;
				}
				String[] receiptData = line.split(cvsSplitBy);
				if (receiptData.length == 4) {
					try {
						wid = Integer.parseInt(receiptData[0]);
						quantity = receiptData[1];
						unit = Integer.parseInt(receiptData[2]);
						if (receiptData[2] != null) {
							unitValueStr = receiptData[2];
						}
						if (receiptData[3] != null) {
							marketValueStr = receiptData[3];
						}
						marketPrice = Integer.parseInt(receiptData[3]);
					} catch (NumberFormatException e) {

						receiptdto = new Receipt();
						receiptdto.setWid(wid);
						receiptdto.setQuantity(quantity);
						if (!StringUtils.isNullOrEmpty(marketValueStr)) {
							receiptdto.setMarketValueStr(marketValueStr);
						}
						if (!StringUtils.isNullOrEmpty(unitValueStr)) {
							receiptdto.setUnitValueStr(unitValueStr);
						}
						receiptdto.setMarketPrice(marketPrice);
						if (null != receiptdto) {
							datTypeMisMatchReceiptTable.add(receiptdto);
						}
						continue;
					}

					// Check if the wid is present in the receipt table
					ResultSet rs = stmt.executeQuery("SELECT wid, quantity, unit FROM receipt WHERE wid = " + wid
							+ " AND quantity=" + "'" + quantity + "'" + " AND unit=" + unit);
					System.out.println("SELECT wid, quantity, unit FROM receipt WHERE wid = " + wid + " AND quantity="
							+ "'" + quantity + "'" + " AND unit=" + unit);
					if (rs.next()) {
						// Update the market_price for the wid in the receipt table
						stmt.executeUpdate("UPDATE receipt SET market_price = " + marketPrice + " WHERE wid = " + wid
								+ " AND quantity=" + "'" + quantity + "'" + " AND unit=" + unit);
						System.out.println("UPDATE receipt SET market_price = " + marketPrice + " WHERE wid = " + wid
								+ " AND quantity=" + "'" + quantity + "'" + " AND unit=" + unit);
					} else {
						// Add the wid to the list of wids not present in the receipt table
						receiptdto = new Receipt();
						receiptdto.setWid(wid);
						receiptdto.setUnit(unit);
						receiptdto.setQuantity(quantity);
						notPresentInReceiptTable.add(receiptdto);

					}
				} else {

					blankDetails = receiptData;
					if (null != blankDetails) {
						receiptdto = new Receipt();
						receiptdto.setBlankDetails(blankDetails);
						blankContentReceiptTable.add(receiptdto);
						continue;
					}
				}
			}

			// Generate a text file with the wids not present in the receipt table
			try (FileWriter writer = new FileWriter("wids_not_present_in_receipt_table.txt")) {
				if (!notPresentInReceiptTable.isEmpty()) {

					writer.append(
							"We found these many wid, quantity, unit have mismatch kindly upload file which has downloaded and chenge only Recipt value");
					writer.append("\n");
					writer.append(String.valueOf("wid		quantity		 unit"));
					for (Receipt misMatch : notPresentInReceiptTable) {
						if (StringUtils.isNullOrEmpty(misMatch.getMarketValueStr())) {

							writer.append("\n");

							writer.append(String.valueOf(misMatch.getWid()));
							writer.append("\t\t");
							writer.append(String.valueOf(misMatch.getUnit()));
							writer.append("\t\t\t");
							writer.append(String.valueOf(misMatch.getQuantity()));
							writer.append("\t\t");
							writer.append("\n");
						}

					}
					System.out.println("Error report is generated -- data misMathch");

				}
				if (!datTypeMisMatchReceiptTable.isEmpty()) {

					writer.append(
							"---RECIPT SHOULD HAVE VALUE FROM 1- 99999999999999 and only numeric is allowed, never be a blank----"
									+ "\n");
					writer.append(String.valueOf("wid		quantity		 unit		 market_Price\n"));
					for (Receipt misMatch : datTypeMisMatchReceiptTable) {
						if (!StringUtils.isNullOrEmpty(misMatch.getMarketValueStr())) {

							writer.append(String.valueOf(misMatch.getWid()));
							writer.append("\t\t");
							writer.append(String.valueOf(misMatch.getQuantity()));
							writer.append("\t\t");

							if (!(StringUtils.isNullOrEmpty(misMatch.getUnitValueStr()))) {
								writer.append(String.valueOf(misMatch.getUnitValueStr()));
								writer.append("\t\t");
								writer.append("\t\t");

							}
							
							if (!(StringUtils.isNullOrEmpty(misMatch.getMarketValueStr()))) {
								writer.append(String.valueOf(misMatch.getMarketValueStr()));
								writer.append("\t\t");
							} else {
								writer.append("\t\t");
								
							}

							/* writer.append(String.valueOf(misMatch.getMarketPrice())); */

						}
						writer.append("\n");

					}
					System.out.println("Error report is generated - blank and dataType type  ");

				}
				if (!blankContentReceiptTable.isEmpty()) {

					writer.append("---We found the blank values, kindly fill the appropriate values----" + "\n");
					writer.append(String.valueOf("wid		quantity		 unit		 market_price" + "\n"));
					for (Receipt misMatch : blankContentReceiptTable) {
						if (null != (misMatch.getBlankDetails())) {

							for (int i = 0; i < misMatch.blankDetails.length; i++) {
								writer.append(misMatch.blankDetails[i] + "\t\t");

							}
							writer.append("\n");
						}

					}
					System.out.println("Error report is generated blank 2");
				}

			}

		} finally {
			// Close the database connection
			con.close();
		}
	}
}
