package com.yj.robot.price;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.Subscribe;
import com.yj.robot.bid.BidContext;
import com.yj.robot.gui.BidTask;
import com.yj.robot.task.EventBusHolder;

@Component
public class PriceEventListener {
	private static final Logger logger = LoggerFactory.getLogger(PriceEventListener.class);
	
	@Autowired
	EventBusHolder eventBusHolder;
	
	@Autowired
	BidContext bitCtx;
	
	@Subscribe
	public void listen(PriceEvent event) {
		logger.info(event.toString());
		if(event.getType() == PriceEnum.CUR_LOWEST_DEAL_PRICE) {
			if(bitCtx.getMyBidPrice() > 0)  {
				if(event.getPrice() + 300 >= bitCtx.getMyBidPrice()) {//我的价格在价格接受区间内, 必须出价
					logger.info("价格进入出价区间");
					new BidTask().run();
					eventBusHolder.getPriceEventBus().unregister(this);
				}
			}
		}
	}
	
	@PostConstruct
	public void init() {
		eventBusHolder.getPriceEventBus().register(this);
	}
}
