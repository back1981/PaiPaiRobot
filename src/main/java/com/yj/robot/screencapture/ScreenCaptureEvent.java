package com.yj.robot.screencapture;

import java.util.Date;

public class ScreenCaptureEvent {
	private ScreenCaptureEnum screenSizeType;
	private String capturedScreenImagePath;
	private Date date;

	public ScreenCaptureEvent(String filePath, ScreenCaptureEnum type) {
		this.capturedScreenImagePath = filePath;
		this.screenSizeType = type;
		this.date = new Date();
	}
	
	
	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public String getCapturedScreenImagePath() {
		return capturedScreenImagePath;
	}

	public void setCapturedScreenImagePath(String filePath) {
		this.capturedScreenImagePath = filePath;
	}
	public ScreenCaptureEnum getScreenSizeType() {
		return screenSizeType;
	}
	public void setScreenSizeType(ScreenCaptureEnum type) {
		this.screenSizeType = type;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("screenSizeType=").append(screenSizeType.name()).append(", ");
		sb.append("screenSizeType").append(capturedScreenImagePath).append(", ");
		sb.append("date=").append(date);
		return sb.toString();
	}
}
