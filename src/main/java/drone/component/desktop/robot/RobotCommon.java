package drone.component.desktop.robot;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * Description: Author: feng.xie on 2015/11/5.
 */
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
		Toolkit.getDefaultToolkit().addAWTEventListener(new Listener(), AWTEvent.MOUSE_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);

//		final Robot rb = new Robot();
		JFrame frm = new JFrame();
		// frm.addMouseMotionListener(new MousePositionListener());
		JPanel pnl = (JPanel) frm.getContentPane();
//		pnl.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 1));
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

		lAddPrice100Position = new JLabel("�Ӽ�100��ť����:" + BTN_PRICE_ADD_100_X + "." + BTN_PRICE_ADD_100_Y);
		row1Pnl.add(lAddPrice100Position);
		JButton btnGetAddPrice100Position = new JButton("��ȡ�Ӽ�100��ť����");
		btnGetAddPrice100Position.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				capturePositionStatus = 0xFFFF & STATUS_CAPTURE_ADD_PRICE_100;
			}
		});
		row1Pnl.add(btnGetAddPrice100Position);
		
		lBidBtnPosition = new JLabel("���۰�ť����:" + BTN_BID_X + "." + BTN_BID_Y);
		row2Pnl.add(lBidBtnPosition);
		JButton btnGetBidBtnPosition = new JButton("��ȡ���۰�ť����");
		btnGetBidBtnPosition.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				capturePositionStatus = 0xFFFF & STATUS_CAPTURE_BID;
			}
		});
		row2Pnl.add(btnGetBidBtnPosition);
		
		lBidConfirmBtnPosition = new JLabel("ȷ�ϰ�ť����:" + BTN_BID_CONFIRM_X + "." + BTN_BID_CONFIRM_Y);
		row3Pnl.add(lBidConfirmBtnPosition);
		JButton btnGetBidConfirmBtnPosition = new JButton("��ȡȷ�ϰ�ť����");
		btnGetBidConfirmBtnPosition.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				capturePositionStatus = 0xFFFF & STATUS_CAPTURE_BID_CONFIRM;
			}
		});
		row3Pnl.add(btnGetBidConfirmBtnPosition);
		
		tBidPriceBaseTime = createTextField();
		row4Pnl.add(tBidPriceBaseTime);
		JLabel bidPriceLabel = new JLabel("��ĵ�ǰ�ۼ�");
		row4Pnl.add(bidPriceLabel);

		tBidStep = createTextField();
		row4Pnl.add(tBidStep);
		JLabel bidStepLabel = new JLabel("Ԫ");
		row4Pnl.add(bidStepLabel);

		JLabel bidTimeLabel = new JLabel("����ʱ��(s)");
		row4Pnl.add(bidTimeLabel);
		tBidTime = createTextField();
		row4Pnl.add(tBidTime);

		// textField = new JTextField() {
		// @Override
		// public Dimension getPreferredSize() {
		// return new Dimension(getParent().getWidth(), 40);
		// }
		// };
		// pnl.add(textField);

		JButton btn = new JButton("ִ�в���");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// int [] pixels = ((BufferedImage)srcImage).getRGB(x, y, w, h,
				// null, 0, w);
				// DataBuffer dataBuffer = new DataBufferInt(pixels, w*h);
				// WritableRaster raster = Raster.createPackedRaster
				// (dataBuffer, w, h,w, new int [] { 0xFF0000, 0xFF00, 0xFF },
				// null );
				// DirectColorModel directColorModel = new DirectColorModel(24,
				// 0xFF0000, 0xFF00, 0xFF);
				// BufferedImage image = new BufferedImage(100, 20,
				// BufferedImage.TYPE_BYTE_GRAY);
				// grayImg(rb, image);
				// robotCommon.hit(rb, true);
				new RobotThread().start();
			}
		});
		row4Pnl.add(btn);

		frm.setSize(new Dimension(550, 220));
		frm.setVisible(true);
	}

	private static void grayImg(Robot rb, BufferedImage dest) {
		BufferedImage bimage = rb.createScreenCapture(new Rectangle(1092, 586, 100, 20));
		ColorConvertOp cco = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		cco.filter(bimage, bimage);
		try {
			FileOutputStream fos = new FileOutputStream("d:/test.jpg");
			JPEGImageEncoder je = JPEGCodec.createJPEGEncoder(fos);
			JPEGEncodeParam jp = je.getDefaultJPEGEncodeParam(bimage);
			jp.setQuality(1f, false);
			je.setJPEGEncodeParam(jp);
			je.encode(bimage);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void clickLMouse(Robot r, int x, int y) {
		r.mouseMove(x, y);
		r.mousePress(InputEvent.BUTTON1_MASK);
		r.delay(10);
		r.mouseRelease(InputEvent.BUTTON1_MASK);
		r.delay(10);
	}

	public static void clickRMouse(Robot r, int x, int y) {
		r.mouseMove(x, y);
		r.mousePress(InputEvent.BUTTON3_MASK);
		r.delay(500);
		r.mouseRelease(InputEvent.BUTTON3_MASK);
		r.delay(500);
	}

	public static void pressKeys(Robot r, int[] ks, int delay) {
		for (int i = 0; i < ks.length; i++) {
			r.keyPress(ks[i]);
			r.delay(10);
			r.keyRelease(ks[i]);
			r.delay(delay);
		}
	}

	private int getKeyCode(char c) {
		return KeyEvent.VK_1 + (c - '0') - 1;
	}

	private void option2(Robot rb) {
		clickLMouse(rb, 1067, 411);
		String txt = textField.getText();
		for (int i = 0; i < txt.length(); i++) {
			rb.keyPress(getKeyCode(txt.charAt(i)));
			rb.delay(20);
		}
		clickLMouse(rb, 1176, 416);
	}

	private void option1(Robot rb) {
		for (int i = 0; i < 6; i++) {
			clickLMouse(rb, 1010, 490);
		}
	}

	private void hit(Robot rb, boolean opt1) {
		if (opt1) {
			option1(rb);
		} else {
			option2(rb);
		}
		clickLMouse(rb, 1170, 532);
		// try {
		// Thread.sleep(500);
		// clickLMouse(rb, 1154, 457);//focus text field
		// } catch (InterruptedException e1) {
		// e1.printStackTrace();
		// }
		rb.mouseMove(950, 609);
	}

	void doCopy(Robot r) throws InterruptedException {
		Thread.sleep(3000);
		r.setAutoDelay(200);
		r.keyPress(KeyEvent.VK_CONTROL);
		r.keyPress(KeyEvent.VK_C);
		r.keyRelease(KeyEvent.VK_CONTROL);
		r.keyRelease(KeyEvent.VK_C);
	}

	void doParse(Robot r) throws InterruptedException {
		r.setAutoDelay(500);
		Thread.sleep(2000);
		r.mouseMove(300, 300);
		r.mousePress(InputEvent.BUTTON1_MASK);
		r.mouseRelease(InputEvent.BUTTON1_MASK);
		r.keyPress(KeyEvent.VK_CONTROL);
		r.keyPress(KeyEvent.VK_V);
		r.keyRelease(KeyEvent.VK_CONTROL);
		r.keyRelease(KeyEvent.VK_V);
	}

	public Icon captureFullScreen(Robot r) {
		BufferedImage fullScreenImage = r.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		ImageIcon icon = new ImageIcon(fullScreenImage);
		return icon;
	}

	public Icon capturePartScreen(Robot r, int x, int y, int width, int height) {
		r.mouseMove(x, y);
		BufferedImage fullScreenImage = r.createScreenCapture(new Rectangle(width, height));
		ImageIcon icon = new ImageIcon(fullScreenImage);
		return icon;
	}

	// static class MousePositionListener implements MouseMotionListener {
	// public void mouseMoved(MouseEvent e) {
	// int x = e.getX();
	// int y = e.getY();
	// String s = "��ǰ�������:" + x + ',' + y;
	// System.out.println(s);
	// }
	//
	// public void mouseDragged(MouseEvent e) {
	// };
	// }

	public static class RobotThread extends Thread {
//		private static final String time = "20:43:";
		// private static final SimpleDateFormat sdf = new
		// SimpleDateFormat("HH:mm:ss");
		private static final SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
		private static final SimpleDateFormat fullSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		public void run() {
			String bidPriceBaseTime = RobotCommon.tBidPriceBaseTime.getText().trim();
			int bidStep = Integer.parseInt(RobotCommon.tBidStep.getText());
			String bidTime = RobotCommon.tBidTime.getText();
			System.out.println("ִ�в���:��" + bidPriceBaseTime + "��ļ۸��ϼӼ�" + bidStep + "Ԫ, Ȼ����" + bidTime + "�����");

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
				// �Ӽ�
				robot.mouseMove(BTN_PRICE_ADD_100_X, BTN_PRICE_ADD_100_Y);
				int clickTimes = bidStep / 100;
				for (int i = 0; i < clickTimes; i++) {
					clickMouse(robot);
				}

				// ����
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
					lAddPrice100Position.setText("�Ӽ�100��ť����:" + BTN_PRICE_ADD_100_X + "." + BTN_PRICE_ADD_100_Y);
					
				} else if((capturePositionStatus & STATUS_CAPTURE_BID) != 0) {
					BTN_BID_X = (int)MouseInfo.getPointerInfo().getLocation().getX();
					BTN_BID_Y = (int)MouseInfo.getPointerInfo().getLocation().getY();
					lBidBtnPosition.setText("���۰�ť����:" + BTN_BID_X + "." + BTN_BID_Y);
				} else if((capturePositionStatus & STATUS_CAPTURE_BID_CONFIRM) != 0) {
					BTN_BID_CONFIRM_X = (int)MouseInfo.getPointerInfo().getLocation().getX();
					BTN_BID_CONFIRM_Y = (int)MouseInfo.getPointerInfo().getLocation().getY();
					lBidConfirmBtnPosition.setText("ȷ�ϰ�ť����:" + BTN_BID_CONFIRM_X + "." + BTN_BID_CONFIRM_Y);
				}
				capturePositionStatus = 0;
			}
		}
	}
}
