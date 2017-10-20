package com.yj.test;

import java.util.Timer;
import java.util.TimerTask;

import org.junit.Test;

public class TimerTest {
	@Test
	public void test() {
		Timer timer = new Timer();
//		timer.schedule(new MyTimerTask(), 0, 1000);
		timer.scheduleAtFixedRate(new MyTimerTask(), 0, 1000);
		while(true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public class MyTimerTask extends TimerTask{

		@Override
		public void run() {
			while(true) {
				System.out.println(Thread.currentThread().getName());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
}
