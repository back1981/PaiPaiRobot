package com.yj.robot.screencapture;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.Subscribe;
import com.yj.demo.tools.ImageParser;
import com.yj.robot.bid.BidContext;
import com.yj.robot.price.PriceEnum;
import com.yj.robot.price.PriceEvent;
import com.yj.robot.task.EventBusHolder;

@Component
public class ScreenCaptureEventListener {
	private static final Logger logger = LoggerFactory.getLogger(ScreenCaptureEventListener.class);
	@Autowired
	EventBusHolder eventBusHolder;

	@Autowired
	ImageParser imageParser;

	@Autowired
	BidContext bidCtx;

	public ScreenCaptureEventListener() {
	}

	@Subscribe
	public void listen(ScreenCaptureEvent event) {
		logger.info(event.toString());
		try {
			if (event.getScreenSizeType() == ScreenCaptureEnum.CURRENT_LOWEST_DEAL_PRICE) {
				int price = imageParser.parseNumber(event.getCapturedScreenImagePath());
				eventBusHolder.getPriceEventBus()
						.post(new PriceEvent(PriceEnum.CUR_LOWEST_DEAL_PRICE, price, event.getDate()));
			} else if (event.getScreenSizeType() == ScreenCaptureEnum.MY_BID_PRICE) {
				int price = imageParser.parseNumber(event.getCapturedScreenImagePath());
				logger.info("我的价格：{}", price);
				bidCtx.setMyBidPrice(price);
			}
		} catch (Exception e) {
			logger.error(event.toString(), e);
		}
	}

	@PostConstruct
	public void init() {
		eventBusHolder.getScreenCaptureEventBus().register(this);
	}

}
