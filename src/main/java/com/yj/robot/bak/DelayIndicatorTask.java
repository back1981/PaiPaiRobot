package com.yj.robot.bak;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.yj.demo.tools.ImageParser;
import com.yj.demo.tools.ImageParserCmdImpl;
import com.yj.demo.tools.ImageUtil;
import com.yj.demo.tools.ScreenCapture;
import com.yj.robot.strategy.DelayIndicator;

/**
 * 根据价格跳动频率决定是否需要延迟提交.
 * if(实际出价时间的最低成交价 - 加价时间的最低成交价 ) < 600, 应该delay，说明没有跳价，这时提交肯定会秒回
 * @author yangjie
 *
 */
public class DelayIndicatorTask implements Runnable{
	private String capImgDir = "C:\\studio\\ws\\paichepai_snapshot";
	int upLeftX;
	int upLeftY;
	int height;
	int width;
	String startTime = "1:29:45";
	String compareTime = "1:29:55";
//	String startTime = "11:05:03";
//	String compareTime = "11:05:11";
	int threadhold = 500;
	ScreenCapture screenCapture = new ScreenCapture();
	ImageParser imageParser = new ImageParserCmdImpl();
	int startPrice = 0;

	@Override
	public void run() {
		try {
			loadPosition();
			long waitTime = getWaitTime();
			if(waitTime > 0) {
				Thread.sleep(waitTime);
			} else {//调试用
				Thread.sleep(3000);
			}
			long startTimeInMillis = getSystemTimeMillis(startTime);
			long compareTimeInMillis = getSystemTimeMillis(compareTime);
			while(true) {
				long start = System.currentTimeMillis();
				
				if(startPrice == 0) {
					int price = capturePrice();
					if(price <= 80000) {//小于警示价，解析错误，终止流程
						System.out.println("小于警示价，退出");
						return;
					}	
					System.out.println("起始价格:" + price);
					startPrice = price;
				}
				if(System.currentTimeMillis() > compareTimeInMillis ) {
					int price = capturePrice();
					System.out.println("最低成交价:" + price);
					if(price <= 80000) {//小于警示价，解析错误，终止流程
						System.out.println("小于警示价，退出");
						return;
					}
					System.out.println("差价:" + (price - startPrice));
					if(price - startPrice < threadhold) {
						DelayIndicator.shouldDelay = true;
					}
				}
				System.out.println("耗时:" + (System.currentTimeMillis() - start));
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int capturePrice() throws Exception {
		String capImgfilePath = getCapImgfilePath();
		System.out.println(capImgfilePath);
		screenCapture.captureScreen(capImgfilePath, upLeftX - 2, upLeftY, height, width + 3);
		ImageUtil.changeImge(new File(capImgfilePath));
		ImageUtil.enlargeImg(capImgfilePath);
		int number = imageParser.parseNumber(capImgfilePath,  capImgfilePath + ".out");
		return number;
	}
	
	private String getCapImgfilePath() {
		return capImgDir + File.separator + "cap-" + System.currentTimeMillis() + ".png"; 
	}
	
	private long getWaitTime() throws ParseException {
		return getSystemTimeMillis(startTime) - System.currentTimeMillis();
	}
	
	private long getSystemTimeMillis(String time) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String curDate = sdf.format(new Date());
		String targetTime = curDate + " " + time;
		SimpleDateFormat fullSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start = fullSdf.parse(targetTime);
		return start.getTime();
	}
	
	private String getPropsFilePath() {
		return System.getProperty("user.home") + File.separator + ".PaiPai.properties";
	}
	
	private void loadPosition() {
		Properties props = new Properties();
		String filePath = getPropsFilePath();
		try {
			props.load(new FileInputStream(new File(filePath)));			
			upLeftX = Integer.parseInt(props.getProperty("DEAL_PRICE_SCREEN_RECT_X"));
			upLeftY = Integer.parseInt(props.getProperty("DEAL_PRICE_SCREEN_RECT_Y"));
			height = Integer.parseInt(props.getProperty("DEAL_PRICE_SCREEN_RECT_H"));
			width = Integer.parseInt(props.getProperty("DEAL_PRICE_SCREEN_RECT_W"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
