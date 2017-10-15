package com.yj.robot.price;

import java.util.Date;

public class PriceEvent {
	private PriceEnum type;
	private Date date;
	private int price;
	
	public PriceEvent(PriceEnum type, int price, Date date) {
		this.type = type;
		this.price = price;
		this.date = date;
	}
	public PriceEnum getType() {
		return type;
	}
	public void setType(PriceEnum type) {
		this.type = type;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("type=").append(type.name()).append(", ");
		sb.append("price=").append(price).append(", ");
		sb.append("date=").append(date);
		return sb.toString();
	}
}
