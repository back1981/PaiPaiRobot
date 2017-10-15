package com.yj.robot.gui;

import java.awt.AWTException;
import java.awt.Robot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 点击确定正式提交报价
 * @author yangjie
 *
 */
public class BidTask extends UITimerTask {
	private static final Logger logger = LoggerFactory.getLogger(BidTask.class);
	@Override
	public void run() {
		logger.info("确定提交价格");
		Robot robot = null;
		try {
			robot = new Robot();
			robot.mouseMove(PaiPaiConfig.BTN_BID_CONFIRM_X, PaiPaiConfig.BTN_BID_CONFIRM_Y);
			clickMouse(robot);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}
