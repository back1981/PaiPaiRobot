package com.yj.robot.gui;

import org.springframework.stereotype.Component;

@Component
public class PaiPaiConfig {
	public static int BTN_PRICE_ADD_100_X = 0;
	public static int BTN_PRICE_ADD_100_Y = 0;

	//���Ӽۡ���ťλ������
	public static int BTN_BID_X = 0;
	public static int BTN_BID_Y = 0;

	//��ȷ���� ��ť���꣬ ���������ť����ʽ�Ѽ۸��͵�������
	public static int BTN_BID_CONFIRM_X = 0;
	public static int BTN_BID_CONFIRM_Y = 0;
	
	//��ͳɽ�����Ļ��׽����
	public static int DEAL_PRICE_SCREEN_RECT_X = 0;
	public static int DEAL_PRICE_SCREEN_RECT_Y = 0;
	public static int DEAL_PRICE_SCREEN_RECT_W = 0;
	public static int DEAL_PRICE_SCREEN_RECT_H = 0;
	
	//�ҵĳ�����Ļ��׽����
	public static int MY_PRICE_SCREEN_RECT_X = 0;
	public static int MY_PRICE_SCREEN_RECT_Y = 0;
	public static int MY_PRICE_SCREEN_RECT_W = 0;
	public static int MY_PRICE_SCREEN_RECT_H = 0;
	
	public static int capturePositionStatus = 0;
	public static int STATUS_CAPTURE_ADD_PRICE_100 = 0x1;
	public static int STATUS_CAPTURE_BID = 0x2;
	public static int STATUS_CAPTURE_BID_CONFIRM = 0x4;
}
