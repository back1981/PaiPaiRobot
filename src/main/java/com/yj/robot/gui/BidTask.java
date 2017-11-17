package com.yj.robot.gui;

import java.awt.AWTException;
import java.awt.Robot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yj.robot.bid.BidContext;

/**
 * 点击确定正式提交报价
 * 
 * @author yangjie
 *
 */
public class BidTask extends UITimerTask {
	private static final Logger logger = LoggerFactory.getLogger(BidTask.class);

	BidContext bidCtx;
	private String msg;
	public BidTask(BidContext bidCtx, String msg) {
		logger.debug("start bid task, {}", Thread.currentThread().getStackTrace());
		this.bidCtx = bidCtx;
		this.msg = msg;
	}

	@Override
	public void run() {
		logger.info("确定提交价格: {}", msg);
		Robot robot = null;
		try {
			robot = new Robot();
			robot.mouseMove(PaiPaiConfig.BTN_BID_CONFIRM_X, PaiPaiConfig.BTN_BID_CONFIRM_Y);
			clickMouse(robot);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		bidCtx.setBiding(false);
	}
}
