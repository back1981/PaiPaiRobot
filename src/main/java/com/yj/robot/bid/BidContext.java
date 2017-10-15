package com.yj.robot.bid;

import org.springframework.stereotype.Component;

@Component
public class BidContext {
	private int myBidPrice;

	public int getMyBidPrice() {
		return myBidPrice;
	}

	public void setMyBidPrice(int myBidPrice) {
		this.myBidPrice = myBidPrice;
	}
	
}
