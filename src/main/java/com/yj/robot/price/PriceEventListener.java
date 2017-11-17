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
				if(event.getPrice() + 400 >= bidCtx.getMyBidPrice()) {//��ǰ��ͳɽ���+300�ǵ�ǰ�ɽ��ܵ���߼ۣ�+400��Ϊ����һЩ��ǰ���ۣ����Եȴ�600��������
					logger.info("�۸�����������, ��ǰ��ͳɽ��� {}", event.getPrice());
					try {
						Thread.sleep(600);
					} catch (InterruptedException e) {
					}
					new BidTask(bidCtx, "�����������").run();
//					try {
//						//ȷ���ύ֮������Ҫ������Ϣ
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
