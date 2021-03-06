package com.yj.robot.screencapture;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

	ExecutorService executor = Executors.newFixedThreadPool(20);

	public ScreenCaptureEventListener() {
	}

	@Subscribe
	public void listen(ScreenCaptureEvent event) {
		logger.debug("isBiding={}, {}", bidCtx.isBiding(), event.toString());
		if (!bidCtx.isBiding()) {
			return;
		}

		executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					if (event.getScreenSizeType() == ScreenCaptureEnum.CURRENT_LOWEST_DEAL_PRICE) {
						long start = System.currentTimeMillis();
						int price = imageParser.parseNumber(event.getCapturedScreenImagePath());
						long timeCost = System.currentTimeMillis() - start;
						eventBusHolder.getPriceEventBus().post(
								new PriceEvent(PriceEnum.CUR_LOWEST_DEAL_PRICE, price, event.getDate(), timeCost));
					} else if (event.getScreenSizeType() == ScreenCaptureEnum.MY_BID_PRICE) {
						while (true) {
							try {
								int price = imageParser.parseNumber(event.getCapturedScreenImagePath());
								logger.info("�ҵļ۸�{}", price);
								bidCtx.setMyBidPrice(price);
								break;
							} catch (Exception e) {
								Thread.sleep(5);
							}
						}
						// Thread.sleep(3000);
					}
					// Thread.sleep(3000);
				} catch (Exception e) {
					logger.error(event.toString(), e);
				}
			}

		});

	}

	@PostConstruct
	public void init() {
		eventBusHolder.getScreenCaptureEventBus().register(this);
	}

}
