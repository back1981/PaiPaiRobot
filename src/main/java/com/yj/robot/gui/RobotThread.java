package com.yj.robot.gui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yj.robot.task.EventBusHolder;

@Component
@Scope("prototype")
public class RobotThread extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(RobotThread.class);
	private static final SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat fullSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	BidPriceCalTask bidPriceCalTask;
	
	@Autowired
	EventBusHolder eventBusHolder;
	
	public void run() {
		String bidPriceBaseTime = RobotGUI.tBidPriceBaseTime.getText().trim();
//		int bidStep = Integer.parseInt(RobotCommon.tBidStep.getText());
		int bidStep = 100;
		String bidTime = RobotGUI.tBidTime.getText();
		logger.info("执行策略:在" + bidPriceBaseTime + "." + PaiPaiConfig.CAL_BID_PRICE_MILLIS + "秒加价" + ", 然后在" + bidTime + "." + PaiPaiConfig.BID_MILLIS +  "秒出价");

		String curDate = dateSdf.format(new Date());
		scheduleBidPriceCalTask(bidPriceBaseTime, bidStep, curDate);
		scheduleBidTask(bidTime, curDate);
	}

	private void scheduleBidTask(String bidTime, String curDate) {
		try {
//			Date bidDate = fullSdf.parse(curDate + " " + time + bidTime);
			Date bidDate = fullSdf.parse(curDate + " " + bidTime);
			bidDate = new Date(bidDate.getTime() + PaiPaiConfig.BID_MILLIS);//500 is ms
			Timer timer = new Timer();
			timer.schedule(new BidTask(), bidDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


	private void scheduleBidPriceCalTask(String bidPriceBaseTime, int bidStep, String curDate) {
//		String bidPriceCalTime = curDate + " " + time + bidPriceBaseTime;
		String bidPriceCalTime = curDate + " " + bidPriceBaseTime;
		Date bidPriceCalDate = null;
		try {
			bidPriceCalDate = fullSdf.parse(bidPriceCalTime);
			bidPriceCalDate = new Date(bidPriceCalDate.getTime() + PaiPaiConfig.CAL_BID_PRICE_MILLIS);//200 is ms
			logger.info("bidPriceCalDate:" + bidPriceCalDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Timer timer = new Timer();
		timer.schedule(bidPriceCalTask, bidPriceCalDate);
	}
}