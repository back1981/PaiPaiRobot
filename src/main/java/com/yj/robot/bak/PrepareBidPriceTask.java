package com.yj.robot.bak;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.Date;

import com.yj.robot.gui.UITimerTask;

public class PrepareBidPriceTask extends UITimerTask {
	@Override
	public void run() {
		System.out.println("bid price cal at " + new Date());
		Robot robot = null;
		try {
			robot = new Robot();
			// 加价
			robot.mouseMove(BidConfig.getInstance().getAddPriceBtnX(), BidConfig.getInstance().getAddPriceBtnY());
//			int clickTimes = bidStep / 100;
			int clickTimes = 1;
			for (int i = 0; i < clickTimes; i++) {
				clickMouse(robot);
			}

			// 出价
			robot.mouseMove(BidConfig.getInstance().getBidBtnX(), BidConfig.getInstance().getBidBtnY());
			clickMouse(robot);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
