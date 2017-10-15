package com.yj.robot.bak;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yj.robot.gui.BidTask;

public class BidStrategy {
	private static final Logger logger = LoggerFactory.getLogger(BidStrategy.class);
	private static final SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// 计算出价价格的时间, HH:mm:ss
	String prepareBidPriceTime;// RobotCommon.tBidPriceBaseTime.getText().trim();
	// 出价时间, HH:mm:ss
	String bidTime; // RobotCommon.tBidTime.getText();

	public void execute() {
		logger.info("执行策略:在" + prepareBidPriceTime + "秒加价" + ", 然后在" + bidTime + "秒出价");

		String curDate = dateSdf.format(new Date());
		schedulePrepareBidPriceTask(prepareBidPriceTime, curDate);

		scheduleBidTask(bidTime, curDate);
		new Thread(new DelayIndicatorTask()).start();
		;
	}

	private void scheduleBidTask(String bidTime, String curDate) {
		try {
			Date bidDate = dateTimeSdf.parse(curDate + " " + bidTime);
			bidDate = new Date(bidDate.getTime() + 200);// 500 is ms
			Timer timer = new Timer();
			timer.schedule(new BidTask(), bidDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void schedulePrepareBidPriceTask(String bidPriceBaseTime, String curDate) {
		String bidPriceCalTime = curDate + " " + bidPriceBaseTime;
		Date bidPriceCalDate = null;
		try {
			bidPriceCalDate = dateTimeSdf.parse(bidPriceCalTime);
			bidPriceCalDate = new Date(bidPriceCalDate.getTime() + 200);// 200 is ms
			System.out.println("bidPriceCalDate:" + bidPriceCalDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Timer timer = new Timer();
		timer.schedule(new PrepareBidPriceTask(), bidPriceCalDate);
	}
}
