package drone.component.desktop.robot;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.google.common.io.Files;

public class RobotCommon {
	private static JLabel lAddPrice100Position;
	private static JLabel lBidBtnPosition;
	private static JLabel lBidConfirmBtnPosition;
	private static JTextField textField;
	private static JTextField tBidTime;
	private static JTextField tBidPriceBaseTime;
	private static JTextField tBidStep;
	private static int BTN_PRICE_ADD_100_X = 0;
	private static int BTN_PRICE_ADD_100_Y = 0;

	private static int BTN_BID_X = 0;
	private static int BTN_BID_Y = 0;

	private static int BTN_BID_CONFIRM_X = 0;
	private static int BTN_BID_CONFIRM_Y = 0;
	
	private static int capturePositionStatus = 0;
	private static int STATUS_CAPTURE_ADD_PRICE_100 = 0x1;
	private static int STATUS_CAPTURE_BID = 0x2;
	private static int STATUS_CAPTURE_BID_CONFIRM = 0x4;
	
	private JTextField createTextField() {
		return new JTextField() {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(80, 30);
			}
		};
	}

	public static void main(String[] args) throws AWTException {
		new RobotCommon().start();
	}

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
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 15, 1);
		row1Pnl.setLayout(flowLayout);
		row2Pnl.setLayout(flowLayout);
		row3Pnl.setLayout(flowLayout);
		pnl.add(row1Pnl);
		pnl.add(row2Pnl);
		pnl.add(row3Pnl);
		pnl.add(row4Pnl);

		lAddPrice100Position = new JLabel("加价100按钮坐标:" + BTN_PRICE_ADD_100_X + "." + BTN_PRICE_ADD_100_Y);
		row1Pnl.add(lAddPrice100Position);
		JButton btnGetAddPrice100Position = new JButton("获取加价100按钮坐标");
		btnGetAddPrice100Position.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				capturePositionStatus = 0xFFFF & STATUS_CAPTURE_ADD_PRICE_100;
			}
		});
		row1Pnl.add(btnGetAddPrice100Position);
		
		lBidBtnPosition = new JLabel("出价按钮坐标:" + BTN_BID_X + "." + BTN_BID_Y);
		row2Pnl.add(lBidBtnPosition);
		JButton btnGetBidBtnPosition = new JButton("获取出价按钮坐标");
		btnGetBidBtnPosition.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				capturePositionStatus = 0xFFFF & STATUS_CAPTURE_BID;
			}
		});
		row2Pnl.add(btnGetBidBtnPosition);
		
		lBidConfirmBtnPosition = new JLabel("确认按钮坐标:" + BTN_BID_CONFIRM_X + "." + BTN_BID_CONFIRM_Y);
		row3Pnl.add(lBidConfirmBtnPosition);
		JButton btnGetBidConfirmBtnPosition = new JButton("获取确认按钮坐标");
		btnGetBidConfirmBtnPosition.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				capturePositionStatus = 0xFFFF & STATUS_CAPTURE_BID_CONFIRM;
			}
		});
		row3Pnl.add(btnGetBidConfirmBtnPosition);
		
		tBidPriceBaseTime = createTextField();
		row4Pnl.add(tBidPriceBaseTime);
		JLabel bidPriceLabel = new JLabel("秒的当前价加");
		row4Pnl.add(bidPriceLabel);

		tBidStep = createTextField();
		row4Pnl.add(tBidStep);
		JLabel bidStepLabel = new JLabel("元");
		row4Pnl.add(bidStepLabel);

		JLabel bidTimeLabel = new JLabel("出价时间(s)");
		row4Pnl.add(bidTimeLabel);
		tBidTime = createTextField();
		row4Pnl.add(tBidTime);

		JButton btn = new JButton("执行策略");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				savePosition();
				new RobotThread().start();
			}
		});
		row4Pnl.add(btn);

		frm.setSize(new Dimension(550, 220));
		frm.setVisible(true);
	}
	
	private void savePosition() {
		StringBuilder sb = new StringBuilder();
		sb.append("BTN_PRICE_ADD_100_X=").append(BTN_PRICE_ADD_100_X).append("\n");
		sb.append("BTN_PRICE_ADD_100_Y=").append(BTN_PRICE_ADD_100_Y).append("\n");
		sb.append("BTN_BID_X=").append(BTN_BID_X).append("\n");
		sb.append("BTN_BID_Y=").append(BTN_BID_Y).append("\n");
		sb.append("BTN_BID_CONFIRM_X=").append(BTN_BID_CONFIRM_X).append("\n");
		sb.append("BTN_BID_CONFIRM_Y=").append(BTN_BID_CONFIRM_Y).append("\n");
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
			BTN_PRICE_ADD_100_X = Integer.parseInt(props.getProperty("BTN_PRICE_ADD_100_X"));
			BTN_PRICE_ADD_100_Y = Integer.parseInt(props.getProperty("BTN_PRICE_ADD_100_Y"));
			BTN_BID_X = Integer.parseInt(props.getProperty("BTN_BID_X"));
			BTN_BID_Y = Integer.parseInt(props.getProperty("BTN_BID_Y"));
			BTN_BID_CONFIRM_X = Integer.parseInt(props.getProperty("BTN_BID_CONFIRM_X"));
			BTN_BID_CONFIRM_Y = Integer.parseInt(props.getProperty("BTN_BID_CONFIRM_Y"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class RobotThread extends Thread {
		private static final SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
		private static final SimpleDateFormat fullSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		public void run() {
			String bidPriceBaseTime = RobotCommon.tBidPriceBaseTime.getText().trim();
			int bidStep = Integer.parseInt(RobotCommon.tBidStep.getText());
			String bidTime = RobotCommon.tBidTime.getText();
			System.out.println("执行策略:在" + bidPriceBaseTime + "秒的价格上加价" + bidStep + "元, 然后在" + bidTime + "秒出价");

			String curDate = dateSdf.format(new Date());

			scheduleBidPriceCalTask(bidPriceBaseTime, bidStep, curDate);

			scheduleBidTask(bidTime, curDate);
		}

		private void scheduleBidTask(String bidTime, String curDate) {
			try {
//				Date bidDate = fullSdf.parse(curDate + " " + time + bidTime);
				Date bidDate = fullSdf.parse(curDate + " " + bidTime);
				Timer timer = new Timer();
				timer.schedule(new BidTask(), bidDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void scheduleBidPriceCalTask(String bidPriceBaseTime, int bidStep, String curDate) {
//			String bidPriceCalTime = curDate + " " + time + bidPriceBaseTime;
			String bidPriceCalTime = curDate + " " + bidPriceBaseTime;
			Date bidPriceCalDate = null;
			try {
				bidPriceCalDate = fullSdf.parse(bidPriceCalTime);
				System.out.println("bidPriceCalDate:" + bidPriceCalDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Timer timer = new Timer();
			timer.schedule(new BidPriceCalTask(bidStep), bidPriceCalDate);
		}
	}

	private static void clickMouse(Robot robot) {
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.delay(10);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}

	public static class BidPriceCalTask extends TimerTask {
		private int bidStep = 0;

		public BidPriceCalTask(int bidStep) {
			this.bidStep = bidStep;
		}

		@Override
		public void run() {
			System.out.println("bid price cal at " + new Date());
			Robot robot = null;
			try {
				robot = new Robot();
				// 加价
				robot.mouseMove(BTN_PRICE_ADD_100_X, BTN_PRICE_ADD_100_Y);
				int clickTimes = bidStep / 100;
				for (int i = 0; i < clickTimes; i++) {
					clickMouse(robot);
				}

				// 出价
				robot.mouseMove(BTN_BID_X, BTN_BID_Y);
				clickMouse(robot);
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static class BidTask extends TimerTask {
		@Override
		public void run() {
			System.out.println("bid price at " + new Date());
			Robot robot = null;
			try {
				robot = new Robot();
				robot.mouseMove(BTN_BID_CONFIRM_X, BTN_BID_CONFIRM_Y);
				clickMouse(robot);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}

	private static class Listener implements AWTEventListener {
		public void eventDispatched(AWTEvent event) {
			if(event.getID() == FocusEvent.FOCUS_LOST) {
				System.out.print(MouseInfo.getPointerInfo().getLocation() + " | ");
				System.out.println(event);
				if((capturePositionStatus & STATUS_CAPTURE_ADD_PRICE_100) != 0) {
					BTN_PRICE_ADD_100_X = (int)MouseInfo.getPointerInfo().getLocation().getX();
					BTN_PRICE_ADD_100_Y = (int)MouseInfo.getPointerInfo().getLocation().getY();
					lAddPrice100Position.setText("加价100按钮坐标:" + BTN_PRICE_ADD_100_X + "." + BTN_PRICE_ADD_100_Y);
					
				} else if((capturePositionStatus & STATUS_CAPTURE_BID) != 0) {
					BTN_BID_X = (int)MouseInfo.getPointerInfo().getLocation().getX();
					BTN_BID_Y = (int)MouseInfo.getPointerInfo().getLocation().getY();
					lBidBtnPosition.setText("出价按钮坐标:" + BTN_BID_X + "." + BTN_BID_Y);
				} else if((capturePositionStatus & STATUS_CAPTURE_BID_CONFIRM) != 0) {
					BTN_BID_CONFIRM_X = (int)MouseInfo.getPointerInfo().getLocation().getX();
					BTN_BID_CONFIRM_Y = (int)MouseInfo.getPointerInfo().getLocation().getY();
					lBidConfirmBtnPosition.setText("确认按钮坐标:" + BTN_BID_CONFIRM_X + "." + BTN_BID_CONFIRM_Y);
				}
				capturePositionStatus = 0;
			}
		}
	}
}
