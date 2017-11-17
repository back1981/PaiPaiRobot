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

import com.yj.robot.bid.BidContext;
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
	
	@Autowired
	BidContext bidCtx;
	
	public void run() {
		String bidPriceBaseTime = RobotGUI.tBidPriceBaseTime.getText().trim();
//		int bidStep = Integer.parseInt(RobotCommon.tBidStep.getText());
		int bidStep = 100;
		String bidTime = RobotGUI.tBidTime.getText();
		logger.info("ִ�в���:��" + bidPriceBaseTime + "." + PaiPaiConfig.CAL_BID_PRICE_MILLIS + "��Ӽ�" + ", Ȼ����" + bidTime + "." + PaiPaiConfig.BID_MILLIS +  "�����");
		bidCtx.startBid();
		String curDate = dateSdf.format(new Date());
		scheduleBidPriceCalTask(bidPriceBaseTime, bidStep, curDate);
		scheduleBidTask(bidTime, curDate);
	}

	//�ύ�۸�Ķ�ʱ����
	private void scheduleBidTask(String bidTime, String curDate) {
		try {
//			Date bidDate = fullSdf.parse(curDate + " " + time + bidTime);
			Date bidDate = fullSdf.parse(curDate + " " + bidTime);
			bidDate = new Date(bidDate.getTime() + PaiPaiConfig.BID_MILLIS);//500 is ms
			Timer timer = new Timer();
			timer.schedule(new BidTask(bidCtx, "�ﵽԤ���ύʱ��"), bidDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


	//�Ӽ۵Ķ�ʱ����
	private void scheduleBidPriceCalTask(String bidPriceBaseTime, int bidStep, String curDate) {
//		String bidPriceCalTime = curDate + " " + time + bidPriceBaseTime;
		String bidPriceCalTime = curDate + " " + bidPriceBaseTime;
		Date bidPriceCalDate = null;
		try {
			bidPriceCalDate = fullSdf.parse(bidPriceCalTime);
			bidPriceCalDate = new Date(bidPriceCalDate.getTime() + PaiPaiConfig.CAL_BID_PRICE_MILLIS);//200 is ms
			logger.info("bidPriceCalDate:" + bidPriceCalDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Timer timer = new Timer();
		if(BidPriceCalTask.previousMonitorTimer != null) {
			logger.info("ȡ��ԭ���timer");
			BidPriceCalTask.previousMonitorTimer.cancel();
			BidPriceCalTask.previousMonitorTimer = null;
		} 
		timer.schedule(bidPriceCalTask, bidPriceCalDate);
	}
}