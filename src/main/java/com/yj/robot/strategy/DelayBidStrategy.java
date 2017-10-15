package com.yj.robot.strategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.yj.robot.pricecapture.PriceCaptureEvent;
import com.yj.robot.pricecapture.PriceCaptureEventType;
import com.yj.robot.pricecapture.PriceCaptureTask;

/**
 * If (price at compareTime - price at baseTime) < threshold then delay
 * 
 * @author yangjie
 *
 */
public class DelayBidStrategy {
	private static final Logger logger = LoggerFactory.getLogger(DelayBidStrategy.class);
	
	private static final SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String baseTime;// HH:mm:ss
	private String compareTime;// HH:mm:ss
	private int threshold;
	private EventBus eventBus = new EventBus("DelayBidStrategyEventBus");

	public DelayBidStrategy(String baseTime, String compareTime, int threshold) {
		this.baseTime = baseTime;
		this.compareTime = compareTime;
		this.threshold = threshold;
		eventBus.register(new PriceCaptureEventListener());
	}
	
	public void execute() {
		logger.info("..............");
		schedulePriceCaptureTask(baseTime, PriceCaptureEventType.DELAY_BID_STRATETY_BASE);
		schedulePriceCaptureTask(compareTime, PriceCaptureEventType.DELAY_BID_STRATETY_COMPARE);
	}

	
	
	private void schedulePriceCaptureTask(String time, PriceCaptureEventType type) {
		try {
			String curDate = dateSdf.format(new Date());
			Date startDate = dateTimeSdf.parse(curDate + " " + time);
			Timer timer = new Timer();
			timer.schedule(new PriceCaptureTask(eventBus, type), startDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class PriceCaptureEventListener {
		PriceCaptureEvent basePriceCaptureEvent;
		PriceCaptureEvent comparePriceCaptureEvent;
		
		@Subscribe
		public void listen(PriceCaptureEvent event) {
			if(event.getType() == PriceCaptureEventType.DELAY_BID_STRATETY_BASE) {
				basePriceCaptureEvent = event;
			}
			if(event.getType() == PriceCaptureEventType.DELAY_BID_STRATETY_COMPARE) {
				comparePriceCaptureEvent = event;
			}
			if(basePriceCaptureEvent != null && comparePriceCaptureEvent != null) {
				logger.info("base:{}, compapre:{}", basePriceCaptureEvent, comparePriceCaptureEvent);
				if((comparePriceCaptureEvent.getPrice() - basePriceCaptureEvent.getPrice()) < threshold) {
					DelayIndicator.shouldDelay = true;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new DelayBidStrategy("10:12:00", "10:12:10", 300).execute();
		while(true) {
			try {
				Thread.sleep(100000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
