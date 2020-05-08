package com.jorislodewijks.hardcorerevival.karma;

public class KarmaEvent {

	public int threshold;
	public String message;
	public int eventMethodId;

	public KarmaEvent(int threshold, String message, int eventMethodId) {
		this.threshold = threshold;
		this.message = message;
		this.eventMethodId = eventMethodId;
	}

}