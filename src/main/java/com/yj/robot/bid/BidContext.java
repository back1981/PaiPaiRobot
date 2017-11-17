package com.yj.robot.bid;

import org.springframework.stereotype.Component;

@Component
public class BidContext {
	private int myBidPrice;
	private boolean isBiding;
	
	

	public boolean isBiding() {
		return isBiding;
	}

	public void setBiding(boolean isBiding) {
		this.isBiding = isBiding;
	}

	public int getMyBidPrice() {
		return myBidPrice;
	}

	public void setMyBidPrice(int myBidPrice) {
		this.myBidPrice = myBidPrice;
	}
	
	public void startBid() {
		this.myBidPrice = 0;
		this.isBiding = true;
	}
}
