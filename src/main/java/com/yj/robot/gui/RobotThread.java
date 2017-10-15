package com.yj.robot.gui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yj.robot.task.EventBusHolder;

@Component
public class RobotThread extends Thread {
	private static final SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat fullSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	BidPriceCalTask bidPriceCalTask;
	
	@Autowired
	EventBusHolder eventBusHolder;
	
	public void run() {
		String bidPriceBaseTime = RobotCommon.tBidPriceBaseTime.getText().trim();
//		int bidStep = Integer.parseInt(RobotCommon.tBidStep.getText());
		int bidStep = 100;
		String bidTime = RobotCommon.tBidTime.getText();
		System.out.println("执行策略:在" + bidPriceBaseTime + "秒加价" + ", 然后在" + bidTime + "秒出价");

		String curDate = dateSdf.format(new Date());
		scheduleBidPriceCalTask(bidPriceBaseTime, bidStep, curDate);
		scheduleBidTask(bidTime, curDate);
	}

	private void scheduleBidTask(String bidTime, String curDate) {
		try {
//			Date bidDate = fullSdf.parse(curDate + " " + time + bidTime);
			Date bidDate = fullSdf.parse(curDate + " " + bidTime);
			bidDate = new Date(bidDate.getTime() + 200);//500 is ms
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
			bidPriceCalDate = new Date(bidPriceCalDate.getTime() + 200);//200 is ms
			System.out.println("bidPriceCalDate:" + bidPriceCalDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Timer timer = new Timer();
		timer.schedule(bidPriceCalTask, bidPriceCalDate);
	}
}