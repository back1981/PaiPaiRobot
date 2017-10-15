package com.yj.robot.pricecapture;

import java.io.File;

import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

import com.yj.demo.tools.ImageParser;
import com.yj.demo.tools.ImageParserJniImpl;
import com.yj.demo.tools.ImageUtil;
import com.yj.demo.tools.ScreenCapture;

public class PriceCaptureUtils {
	private static final String capImgDir = "C:\\studio\\ws\\paichepai_snapshot";
	
	static ScreenCapture screenCapture = new ScreenCapture();
	static ImageParser imageParser = new ImageParserJniImpl();
	
	public static int capturePrice() throws Exception {
		StopWatch stopWatch = new LoggingStopWatch();
		PriceCaptureConfig config = PriceCaptureConfig.getInstance();
		stopWatch.lap("loadConfig");
		String capImgfilePath = getCapImgfilePath();
		screenCapture.captureScreen(capImgfilePath, config.getUpLeftX() - 2, config.getUpLeftY(), config.getHeight(), config.getWidth() + 3);
		stopWatch.lap("captureScreen");
		ImageUtil.changeImge(new File(capImgfilePath));
		stopWatch.lap("changeImgeToWhiteAndBlack");
		ImageUtil.enlargeImg(capImgfilePath);
		stopWatch.lap("enlargeImg");
		int number = imageParser.parseNumber(capImgfilePath,  capImgfilePath + ".out");
		stopWatch.lap("parseNumber");
		stopWatch.stop();
		return number;
	}
	
	private static String getCapImgfilePath() {
		return capImgDir + File.separator + "cap-" + System.currentTimeMillis() + ".png"; 
	}
	
}
