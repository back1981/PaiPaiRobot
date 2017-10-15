package com.yj.robot.task;

import java.util.TimerTask;

import com.google.common.eventbus.EventBus;

public abstract class PublisherTask  extends TimerTask {
	protected EventBus eventBus = null;
	public PublisherTask(EventBus eventBus) {
		this.eventBus = eventBus;
	}
}
