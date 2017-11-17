package com.yj.robot.gui;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.util.Calendar;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yj.robot.screencapture.ScreenCaptureEnum;
import com.yj.robot.screencapture.ScreenCaptureTask;
import com.yj.robot.task.EventBusHolder;

/**
 * 计算竞拍价格，比如说在45s+1000之类的
 * 
 * @author yangjie
 *
 */
@Component
@Scope("prototype")
public class BidPriceCalTask extends UITimerTask {
	private static final Logger logger = LoggerFactory.getLogger(BidPriceCalTask.class);

	@Autowired
	EventBusHolder eventBusHolder;
	
	public static Timer previousMonitorTimer = null;

	public BidPriceCalTask() {
	}

	@Override
	public void run() {
		logger.info("计算出价");
		Robot robot = null;
		try {
			robot = new Robot();
			// 点击“加价”
			robot.mouseMove(PaiPaiConfig.BTN_PRICE_ADD_100_X, PaiPaiConfig.BTN_PRICE_ADD_100_Y);
			clickMouse(robot);

			// 点击“出价”
			robot.mouseMove(PaiPaiConfig.BTN_BID_X, PaiPaiConfig.BTN_BID_Y);
			clickMouse(robot);
			
			logger.info("监控启动：{}", (previousMonitorTimer != null));
			if(previousMonitorTimer == null) {
				previousMonitorTimer = scheduleDealPriceMonitorTask();
			} 
			
			try {
				Thread.sleep(1000);// capture capture may need some time, so sleep a while
			} catch (InterruptedException e) {
			}
			// 捕获屏幕，记录自己的出价
			Timer timer = new Timer();
			Rectangle rectangle = new Rectangle(PaiPaiConfig.MY_PRICE_SCREEN_RECT_X,
					PaiPaiConfig.MY_PRICE_SCREEN_RECT_Y, PaiPaiConfig.MY_PRICE_SCREEN_RECT_W,
					PaiPaiConfig.MY_PRICE_SCREEN_RECT_H);
			timer.schedule(new ScreenCaptureTask(eventBusHolder.getScreenCaptureEventBus(), rectangle,
					ScreenCaptureEnum.MY_BID_PRICE), 0);
			

		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private Timer scheduleDealPriceMonitorTask() {
		Timer timer = new Timer();
		Rectangle rectangle = new Rectangle(PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_X, PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_Y, PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_W, PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_H);
		ScreenCaptureTask screenCaptureTask = new ScreenCaptureTask(eventBusHolder.getScreenCaptureEventBus(), rectangle, ScreenCaptureEnum.CURRENT_LOWEST_DEAL_PRICE);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.SECOND, 1);
		timer.scheduleAtFixedRate(screenCaptureTask, cal.getTime(), 200);
		return timer;
	}
}