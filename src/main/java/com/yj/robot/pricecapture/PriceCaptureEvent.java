package com.yj.robot.pricecapture;

public class PriceCaptureEvent {
	private String dateTime;
	private int price;
	private PriceCaptureEventType type;
	
	public PriceCaptureEvent(String dateTime, int price, PriceCaptureEventType type) {
		this.dateTime = dateTime;
		this.price = price;
		this.type = type;
	}
	
	
	public PriceCaptureEventType getType() {
		return type;
	}


	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	public String toString() {
		return "dateTime=" + dateTime + ";price=" + price;
	}
}
