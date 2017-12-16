package com.yj.robot.screencapture;

import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.yj.demo.tools.ScreenCapture;
import com.yj.robot.bid.BidContext;
import com.yj.robot.task.PublisherTask;

public class ScreenCaptureTask extends PublisherTask {
	private static final Logger logger = LoggerFactory.getLogger(ScreenCaptureTask.class);
	Rectangle rectangle;
	SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss-SSS");
	ScreenCapture screenCapture = new ScreenCapture();
	ScreenCaptureEnum type;
	BidContext bidCtx;
	public ScreenCaptureTask(BidContext bidCtx, EventBus eventBus, Rectangle rectangle, ScreenCaptureEnum type) {
		super(eventBus);
		this.type = type;
		this.rectangle = rectangle;
		this.bidCtx = bidCtx;
	}
	@Override
	public void run() {
		if(!bidCtx.isBiding()) {
			return;
		}
		logger.debug("start screen capture");
		String filePath = "C:\\studio\\ws\\paichepai_snapshot\\" + type.name() + "-" + sdf.format(new Date()) + ".png";
		try {
			if(type == ScreenCaptureEnum.FULL) {
				screenCapture.captureFullScreen(filePath);
			} else {
				screenCapture.captcaptureScreen(filePath, rectangle);
			}
			eventBus.post(new ScreenCaptureEvent(filePath, type));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
