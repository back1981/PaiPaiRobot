package com.yj.robot.pricecapture;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

/**
 * 取最低可成交价
 * 
 * @author yangjie
 *
 */
public class PriceCaptureTask extends TimerTask {
	private static final Logger logger = LoggerFactory.getLogger(PriceCaptureTask.class);
	EventBus eventBus = null;
	PriceCaptureEventType type = null;

	public PriceCaptureTask(EventBus eventBus, PriceCaptureEventType type) {
		this.eventBus = eventBus;
		this.type = type;
	}

	@Override
	public void run() {
		try {
			long start = System.currentTimeMillis();
			int price = PriceCaptureUtils.capturePrice();
			SimpleDateFormat fullSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateTime = fullSdf.format(new Date());
			logger.info("price at {} is {}, timeCost={}", dateTime, price, (System.currentTimeMillis() - start));
			if (eventBus != null) {
				eventBus.post(new PriceCaptureEvent(dateTime, price, type));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
