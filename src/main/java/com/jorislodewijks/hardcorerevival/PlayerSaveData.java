package com.jorislodewijks.hardcorerevival;

import java.io.Serializable;

public class PlayerSaveData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8032957560320757765L;
	public String player;
	public int karma;
	
	public PlayerSaveData(String player, int karma) {
		this.player = player;
		this.karma = karma;
	}
	
	
}