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
	BidContext bidCtx;
	
	@Subscribe
	public void listen(PriceEvent event) {
		logger.debug("isBiding={}, {}", bidCtx.isBiding(), event.toString());
		if(!bidCtx.isBiding()) {
			return;
		}
		
		if(event.getType() == PriceEnum.CUR_LOWEST_DEAL_PRICE) {
			if(bidCtx.getMyBidPrice() > 0)  {				
				if(event.getPrice() + 400 >= bidCtx.getMyBidPrice()) {//当前最低成交价+300是当前可接受的最高价，+400是为了做一些提前出价，所以等待600毫秒后出价
					logger.info("价格进入出价区间, 当前最低成交价 {}", event.getPrice());
					try {
						Thread.sleep(600);
					} catch (InterruptedException e) {
					}
					new BidTask(bidCtx, "进入出价区间").run();
//					try {
//						//确定提交之后不再需要接收信息
//						eventBusHolder.getPriceEventBus().unregister(this);
//					} catch(Exception e) {}
				}
			}
		}
	}
	
	@PostConstruct
	public void init() {
		eventBusHolder.getPriceEventBus().register(this);
	}
}
