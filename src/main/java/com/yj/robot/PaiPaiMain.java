package com.yj.robot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.yj.robot.gui.RobotGUI;

@SpringBootApplication
public class PaiPaiMain implements CommandLineRunner {
	@Autowired
	RobotGUI robot;

	public static void main(String[] args) {
		new SpringApplicationBuilder(PaiPaiMain.class).headless(false).web(false).run(args);
//		SpringApplication.run(PaiPaiMain.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		robot.start();
	}

}
