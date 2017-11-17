package com.yj.robot.gui;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.google.common.io.Files;
import com.yj.demo.tools.ScreenRectangleSelecter;

@Component
public class RobotGUI implements ApplicationContextAware{
	private static JLabel lAddPrice100Position;
	private static JLabel lBidBtnPosition;
	private static JLabel lBidConfirmBtnPosition;
	private static JLabel lSelectDealPriceRectangle;
	private static JLabel lSelectMyPriceRectangle;
	public static JTextField tBidTime;
	public static JTextField tBidTimeMillis;
	public static JTextField tBidPriceBaseTime;
	public static JTextField tBidPriceBaseTimeMillis;
	private static JTextField tBidStep;

	@Autowired
	RobotThread robotThread;
	
	private JTextField createTextField() {
		return new JTextField() {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(80, 30);
			}
		};
	}

//	public static void main(String[] args) throws AWTException {
//		new RobotGUI().start();
//	}

	public void start() throws AWTException {
		loadPosition();
		Toolkit.getDefaultToolkit().addAWTEventListener(new Listener(), AWTEvent.MOUSE_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);
		JFrame frm = new JFrame();
		JPanel pnl = (JPanel) frm.getContentPane();
		BoxLayout layout = new BoxLayout(pnl, BoxLayout.PAGE_AXIS);
		pnl.setLayout(layout);
		
		JPanel row1Pnl = new JPanel();
		JPanel row2Pnl = new JPanel();
		JPanel row3Pnl = new JPanel();
		JPanel row4Pnl = new JPanel();
		JPanel row5Pnl = new JPanel();
		JPanel row6Pnl = new JPanel();
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 15, 1);
		row1Pnl.setLayout(flowLayout);
		row2Pnl.setLayout(flowLayout);
		row3Pnl.setLayout(flowLayout);
		row4Pnl.setLayout(flowLayout);
		row5Pnl.setLayout(flowLayout);
		row6Pnl.setLayout(flowLayout);
		pnl.add(row1Pnl);
		pnl.add(row2Pnl);
		pnl.add(row3Pnl);
		pnl.add(row4Pnl);
		pnl.add(row5Pnl);
		pnl.add(row6Pnl);

		lAddPrice100Position = new JLabel("自定义加价按钮坐标:" + PaiPaiConfig.BTN_PRICE_ADD_100_X + "." + PaiPaiConfig.BTN_PRICE_ADD_100_Y);
		row1Pnl.add(lAddPrice100Position);
		JButton btnGetAddPrice100Position = new JButton("获取加价按钮坐标");
		btnGetAddPrice100Position.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PaiPaiConfig.capturePositionStatus = 0xFFFF & PaiPaiConfig.STATUS_CAPTURE_ADD_PRICE_100;
			}
		});
		row1Pnl.add(btnGetAddPrice100Position);
		
		lBidBtnPosition = new JLabel("出价按钮坐标:" + PaiPaiConfig.BTN_BID_X + "." + PaiPaiConfig.BTN_BID_Y);
		row2Pnl.add(lBidBtnPosition);
		JButton btnGetBidBtnPosition = new JButton("获取出价按钮坐标");
		btnGetBidBtnPosition.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PaiPaiConfig.capturePositionStatus = 0xFFFF & PaiPaiConfig.STATUS_CAPTURE_BID;
			}
		});
		row2Pnl.add(btnGetBidBtnPosition);
		
		lBidConfirmBtnPosition = new JLabel("确认按钮坐标:" + PaiPaiConfig.BTN_BID_CONFIRM_X + "." + PaiPaiConfig.BTN_BID_CONFIRM_Y);
		row3Pnl.add(lBidConfirmBtnPosition);
		JButton btnGetBidConfirmBtnPosition = new JButton("获取确认按钮坐标");
		btnGetBidConfirmBtnPosition.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PaiPaiConfig.capturePositionStatus = 0xFFFF & PaiPaiConfig.STATUS_CAPTURE_BID_CONFIRM;
			}
		});
		row3Pnl.add(btnGetBidConfirmBtnPosition);
		
		
		lSelectDealPriceRectangle = new JLabel("选取最低可成交价区域:" + PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_X + ":" + PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_Y + ":" + PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_W + ":" + PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_H);
		row4Pnl.add(lSelectDealPriceRectangle);
		JButton btnSelectDealPriceRectangle = new JButton("选取最低可成交价区域");
		btnSelectDealPriceRectangle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ScreenRectangleSelecter selector = new ScreenRectangleSelecter();
				selector.select();
				PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_X = selector.getRecX();
				PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_Y = selector.getRecY();
				PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_W = selector.getRecW();
				PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_H = selector.getRecH();
			}
		});
		row4Pnl.add(btnSelectDealPriceRectangle);
		
		
		lSelectMyPriceRectangle = new JLabel("选取我的出价区域:" + PaiPaiConfig.MY_PRICE_SCREEN_RECT_X + ":" + PaiPaiConfig.MY_PRICE_SCREEN_RECT_Y + ":" + PaiPaiConfig.MY_PRICE_SCREEN_RECT_W + ":" + PaiPaiConfig.MY_PRICE_SCREEN_RECT_H);
		row5Pnl.add(lSelectMyPriceRectangle);
		JButton btnSelectMyPriceRectangle = new JButton("选取我的出价区域");
		btnSelectMyPriceRectangle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ScreenRectangleSelecter selector = new ScreenRectangleSelecter();
				selector.select();
				PaiPaiConfig.MY_PRICE_SCREEN_RECT_X = selector.getRecX();
				PaiPaiConfig.MY_PRICE_SCREEN_RECT_Y = selector.getRecY();
				PaiPaiConfig.MY_PRICE_SCREEN_RECT_W = selector.getRecW();
				PaiPaiConfig.MY_PRICE_SCREEN_RECT_H = selector.getRecH();
			}
		});
		row5Pnl.add(btnSelectMyPriceRectangle);
		
		
		JLabel bidPriceLabel = new JLabel("加价时间");
		row6Pnl.add(bidPriceLabel);
		tBidPriceBaseTime = createTextField();
		row6Pnl.add(tBidPriceBaseTime);
		row6Pnl.add(new JLabel("."));
		tBidPriceBaseTime.setText("11:29:45");
		tBidPriceBaseTimeMillis = createTextField();
		tBidPriceBaseTimeMillis.setText(String.valueOf(PaiPaiConfig.CAL_BID_PRICE_MILLIS));
		row6Pnl.add(tBidPriceBaseTimeMillis);
		tBidStep = createTextField();
//		row4Pnl.add(tBidStep);
//		JLabel bidStepLabel = new JLabel("元");
//		row4Pnl.add(bidStepLabel);

		JLabel bidTimeLabel = new JLabel("出价时间");
		row6Pnl.add(bidTimeLabel);
		tBidTime = createTextField();
		row6Pnl.add(tBidTime);
		tBidTime.setText("11:29:58");
		tBidTimeMillis = createTextField();
		row6Pnl.add(new JLabel("."));
		tBidTimeMillis.setText(String.valueOf(PaiPaiConfig.BID_MILLIS));
		row6Pnl.add(tBidTimeMillis);
		
		JButton btn = new JButton("执行策略");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				savePosition();
				applicationContext.getBean(RobotThread.class).start();
			}
		});
		row6Pnl.add(btn);

		frm.setSize(new Dimension(650, 320));
		frm.setVisible(true);
	}
	
	private void savePosition() {
		StringBuilder sb = new StringBuilder();
		sb.append("BTN_PRICE_ADD_100_X=").append(PaiPaiConfig.BTN_PRICE_ADD_100_X).append("\n");
		sb.append("BTN_PRICE_ADD_100_Y=").append(PaiPaiConfig.BTN_PRICE_ADD_100_Y).append("\n");
		sb.append("BTN_BID_X=").append(PaiPaiConfig.BTN_BID_X).append("\n");
		sb.append("BTN_BID_Y=").append(PaiPaiConfig.BTN_BID_Y).append("\n");
		sb.append("BTN_BID_CONFIRM_X=").append(PaiPaiConfig.BTN_BID_CONFIRM_X).append("\n");
		sb.append("BTN_BID_CONFIRM_Y=").append(PaiPaiConfig.BTN_BID_CONFIRM_Y).append("\n");
		sb.append("DEAL_PRICE_SCREEN_RECT_X=").append(PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_X).append("\n");
		sb.append("DEAL_PRICE_SCREEN_RECT_Y=").append(PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_Y).append("\n");
		sb.append("DEAL_PRICE_SCREEN_RECT_W=").append(PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_W).append("\n");
		sb.append("DEAL_PRICE_SCREEN_RECT_H=").append(PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_H).append("\n");

		sb.append("MY_PRICE_SCREEN_RECT_X=").append(PaiPaiConfig.MY_PRICE_SCREEN_RECT_X).append("\n");
		sb.append("MY_PRICE_SCREEN_RECT_Y=").append(PaiPaiConfig.MY_PRICE_SCREEN_RECT_Y).append("\n");
		sb.append("MY_PRICE_SCREEN_RECT_W=").append(PaiPaiConfig.MY_PRICE_SCREEN_RECT_W).append("\n");
		sb.append("MY_PRICE_SCREEN_RECT_H=").append(PaiPaiConfig.MY_PRICE_SCREEN_RECT_H).append("\n");
		
		sb.append("CAL_BID_PRICE_MILLIS=").append(tBidPriceBaseTimeMillis.getText()).append("\n");
		sb.append("BID_MILLIS=").append(tBidTimeMillis.getText()).append("\n");
		String filePath = getPropsFilePath();
		try {
			Files.write(sb, new File(filePath), Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getPropsFilePath() {
		return System.getProperty("user.home") + File.separator + ".PaiPai.properties";
	}
	
	private void loadPosition() {
		Properties props = new Properties();
		String filePath = getPropsFilePath();
		try {
			props.load(new FileInputStream(new File(filePath)));
			PaiPaiConfig.BTN_PRICE_ADD_100_X = Integer.parseInt(props.getProperty("BTN_PRICE_ADD_100_X"));
			PaiPaiConfig.BTN_PRICE_ADD_100_Y = Integer.parseInt(props.getProperty("BTN_PRICE_ADD_100_Y"));
			PaiPaiConfig.BTN_BID_X = Integer.parseInt(props.getProperty("BTN_BID_X"));
			PaiPaiConfig.BTN_BID_Y = Integer.parseInt(props.getProperty("BTN_BID_Y"));
			PaiPaiConfig.BTN_BID_CONFIRM_X = Integer.parseInt(props.getProperty("BTN_BID_CONFIRM_X"));
			PaiPaiConfig.BTN_BID_CONFIRM_Y = Integer.parseInt(props.getProperty("BTN_BID_CONFIRM_Y"));
			PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_X = Integer.parseInt(props.getProperty("DEAL_PRICE_SCREEN_RECT_X"));
			PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_Y = Integer.parseInt(props.getProperty("DEAL_PRICE_SCREEN_RECT_Y"));
			PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_W = Integer.parseInt(props.getProperty("DEAL_PRICE_SCREEN_RECT_W"));
			PaiPaiConfig.DEAL_PRICE_SCREEN_RECT_H = Integer.parseInt(props.getProperty("DEAL_PRICE_SCREEN_RECT_H"));
			
			PaiPaiConfig.MY_PRICE_SCREEN_RECT_X = Integer.parseInt(props.getProperty("MY_PRICE_SCREEN_RECT_X"));
			PaiPaiConfig.MY_PRICE_SCREEN_RECT_Y = Integer.parseInt(props.getProperty("MY_PRICE_SCREEN_RECT_Y"));
			PaiPaiConfig.MY_PRICE_SCREEN_RECT_W = Integer.parseInt(props.getProperty("MY_PRICE_SCREEN_RECT_W"));
			PaiPaiConfig.MY_PRICE_SCREEN_RECT_H = Integer.parseInt(props.getProperty("MY_PRICE_SCREEN_RECT_H"));
			try {
				PaiPaiConfig.CAL_BID_PRICE_MILLIS = Integer.parseInt(props.getProperty("CAL_BID_PRICE_MILLIS"));
			} catch(Exception e) {
				PaiPaiConfig.CAL_BID_PRICE_MILLIS = 0;
			}
			try {
				PaiPaiConfig.BID_MILLIS = Integer.parseInt(props.getProperty("BID_MILLIS"));
			} catch(Exception e) {
				PaiPaiConfig.BID_MILLIS  = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}








	private static class Listener implements AWTEventListener {
		public void eventDispatched(AWTEvent event) {
			if(event.getID() == FocusEvent.FOCUS_LOST) {
				System.out.print(MouseInfo.getPointerInfo().getLocation() + " | ");
				System.out.println(event);
				if((PaiPaiConfig.capturePositionStatus & PaiPaiConfig.STATUS_CAPTURE_ADD_PRICE_100) != 0) {
					PaiPaiConfig.BTN_PRICE_ADD_100_X = (int)MouseInfo.getPointerInfo().getLocation().getX();
					PaiPaiConfig.BTN_PRICE_ADD_100_Y = (int)MouseInfo.getPointerInfo().getLocation().getY();
					lAddPrice100Position.setText("加价100按钮坐标:" + PaiPaiConfig.BTN_PRICE_ADD_100_X + "." + PaiPaiConfig.BTN_PRICE_ADD_100_Y);
					
				} else if((PaiPaiConfig.capturePositionStatus & PaiPaiConfig.STATUS_CAPTURE_BID) != 0) {
					PaiPaiConfig.BTN_BID_X = (int)MouseInfo.getPointerInfo().getLocation().getX();
					PaiPaiConfig.BTN_BID_Y = (int)MouseInfo.getPointerInfo().getLocation().getY();
					lBidBtnPosition.setText("出价按钮坐标:" + PaiPaiConfig.BTN_BID_X + "." + PaiPaiConfig.BTN_BID_Y);
				} else if((PaiPaiConfig.capturePositionStatus & PaiPaiConfig.STATUS_CAPTURE_BID_CONFIRM) != 0) {
					PaiPaiConfig.BTN_BID_CONFIRM_X = (int)MouseInfo.getPointerInfo().getLocation().getX();
					PaiPaiConfig.BTN_BID_CONFIRM_Y = (int)MouseInfo.getPointerInfo().getLocation().getY();
					lBidConfirmBtnPosition.setText("确认按钮坐标:" + PaiPaiConfig.BTN_BID_CONFIRM_X + "." + PaiPaiConfig.BTN_BID_CONFIRM_Y);
				}
				PaiPaiConfig.capturePositionStatus = 0;
			}
		}
	}






	ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
