package com.yj.robot.bak;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class BidConfig {
	private static final BidConfig instance = new BidConfig();
	private int bidBtnX = 0;
	private int bidBtnY = 0;
	private int bidConfirmBtnX = 0;
	private int bidConfirmBtnY = 0;
	private int addPriceBtnX = 0;
	private int addPriceBtnY = 0;
	

	
	public int getBidBtnX() {
		return bidBtnX;
	}

	public int getBidBtnY() {
		return bidBtnY;
	}

	public int getAddPriceBtnX() {
		return addPriceBtnX;
	}

	public int getAddPriceBtnY() {
		return addPriceBtnY;
	}

	public int getBidConfirmBtnX() {
		return bidConfirmBtnX;
	}

	public int getBidConfirmBtnY() {
		return bidConfirmBtnY;
	}

	public static BidConfig getInstance() {
		return instance;
	}

	public BidConfig() {
		loadPosition();
	}

	private void loadPosition() {
		Properties props = new Properties();
		String filePath = getPropsFilePath();
		try {
			props.load(new FileInputStream(new File(filePath)));
			bidConfirmBtnX = Integer.parseInt(props.getProperty("BTN_BID_CONFIRM_X"));
			bidConfirmBtnY = Integer.parseInt(props.getProperty("BTN_BID_CONFIRM_Y"));
			addPriceBtnX = Integer.parseInt(props.getProperty("BTN_PRICE_ADD_100_X"));
			addPriceBtnY = Integer.parseInt(props.getProperty("BTN_PRICE_ADD_100_Y"));
			bidBtnX = Integer.parseInt(props.getProperty("BTN_BID_X"));
			bidBtnY = Integer.parseInt(props.getProperty("BTN_BID_Y"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getPropsFilePath() {
		return System.getProperty("user.home") + File.separator + ".PaiPai.properties";
	}
}
