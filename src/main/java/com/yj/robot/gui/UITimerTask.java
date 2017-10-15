package com.yj.robot.gui;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.TimerTask;

public abstract class UITimerTask extends TimerTask{
	protected void clickMouse(Robot robot) {
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.delay(10);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}
}
