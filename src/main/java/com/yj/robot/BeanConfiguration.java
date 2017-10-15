package com.yj.robot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yj.demo.tools.ImageParser;
import com.yj.demo.tools.ImageParserBaiduOcrImpl;

@Configuration
public class BeanConfiguration {
	@Bean
	public ImageParser getImageParser() {
//		return new ImageParserJniImpl();
		return new ImageParserBaiduOcrImpl();
	}
}
