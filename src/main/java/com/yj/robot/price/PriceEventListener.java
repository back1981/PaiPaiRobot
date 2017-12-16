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
				if(event.getPrice() + 300 >= bidCtx.getMyBidPrice()) {//��ǰ��ͳɽ���+300�ǵ�ǰ�ɽ��ܵ���߼ۣ�+400��Ϊ����һЩ��ǰ���ۣ����Եȴ�600��������
					logger.info("�۸�����������, ��ǰ��ͳɽ��� {}", event.getPrice());
					new BidTask(bidCtx, "�����������").run();
				} 
//				else if(event.getPrice() + 400 >= bidCtx.getMyBidPrice()) {//��ǰ��ͳɽ���+300�ǵ�ǰ�ɽ��ܵ���߼ۣ�+400��Ϊ����һЩ��ǰ���ۣ����Եȴ�600��������
//					logger.info("�����۸�����������, ��ǰ��ͳɽ��� {}", event.getPrice());
//					try {
//						long waitTime = 800 - event.getTimeCostAtParsePrice();
//						if(waitTime > 0) {
//							logger.info("�ȴ�{}�������ύ", waitTime);
//							Thread.sleep(waitTime);
//						}
//					} catch (InterruptedException e) {
//					}
//					new BidTask(bidCtx, "���������������").run();
//				}
			}
		}
	}
	
	@PostConstruct
	public void init() {
		eventBusHolder.getPriceEventBus().register(this);
	}
}
