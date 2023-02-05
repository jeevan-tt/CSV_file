package org;

class Receipt {
	int wid;
	String quantity;
	int unit;
	int marketPrice;
	String unitValueStr;
	String marketValueStr;
	String [] blankDetails;

	public Receipt() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Receipt(int wid, String quantity, int unit, int marketPrice, String unitValueStr, String marketValueStr) {
		super();
		this.wid = wid;
		this.quantity = quantity;
		this.unit = unit;
		this.marketPrice = marketPrice;
		this.unitValueStr = unitValueStr;
		this.marketValueStr = marketValueStr;
	}

	public String getUnitValueStr() {
		return unitValueStr;
	}

	public void setUnitValueStr(String unitValueStr) {
		this.unitValueStr = unitValueStr;
	}

	public String getMarketValueStr() {
		return marketValueStr;
	}

	public void setMarketValueStr(String marketValueStr) {
		this.marketValueStr = marketValueStr;
	}

	public Receipt(int wid, String quantity, int unit, int marketPrice) {
		super();
		this.wid = wid;
		this.quantity = quantity;
		this.unit = unit;
		this.marketPrice = marketPrice;
	}

	public int getWid() {
		return wid;
	}

	public void setWid(int wid) {
		this.wid = wid;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	public int getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(int marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String[] getBlankDetails() {
		return blankDetails;
	}

	public void setBlankDetails(String[] blankDetails) {
		this.blankDetails = blankDetails;
	}

}
