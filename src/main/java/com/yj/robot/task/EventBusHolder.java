package com.yj.robot.task;

import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;

@Component
public class EventBusHolder {
//	public static final EventBusHolder instance = new EventBusHolder();
//	
	private EventBus screenCaptureEventBus = new EventBus("screenCaptureEventBus");
	private EventBus priceEventBus = new EventBus("priceEventBus");
//	
//	public static EventBusHolder getInstance() {
//		return instance;
//	}

	public EventBus getScreenCaptureEventBus() {
		return screenCaptureEventBus;
	}

	public void setScreenCaptureEventBus(EventBus screenCaptureEventBus) {
		this.screenCaptureEventBus = screenCaptureEventBus;
	}

	public EventBus getPriceEventBus() {
		return priceEventBus;
	}

	public void setPriceEventBus(EventBus priceEventBus) {
		this.priceEventBus = priceEventBus;
	}
	
	
	
}
