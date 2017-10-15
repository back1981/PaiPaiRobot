package com.yj.robot.pricecapture;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PriceCaptureConfig {
	private static final PriceCaptureConfig instance = new PriceCaptureConfig();
	private int upLeftX;
	private int upLeftY;
	private int height;
	private int width;
	
	public static PriceCaptureConfig getInstance() {
		return instance;
	}
	

	public int getUpLeftX() {
		return upLeftX;
	}

	public int getUpLeftY() {
		return upLeftY;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public PriceCaptureConfig() {
		loadPosition();
	}
	
	private void loadPosition() {
		Properties props = new Properties();
		String filePath = getPropsFilePath();
		try {
			props.load(new FileInputStream(new File(filePath)));			
			upLeftX = Integer.parseInt(props.getProperty("DEAL_PRICE_SCREEN_RECT_X"));
			upLeftY = Integer.parseInt(props.getProperty("DEAL_PRICE_SCREEN_RECT_Y"));
			height = Integer.parseInt(props.getProperty("DEAL_PRICE_SCREEN_RECT_H"));
			width = Integer.parseInt(props.getProperty("DEAL_PRICE_SCREEN_RECT_W"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getPropsFilePath() {
		return System.getProperty("user.home") + File.separator + ".PaiPai.properties";
	}
}
