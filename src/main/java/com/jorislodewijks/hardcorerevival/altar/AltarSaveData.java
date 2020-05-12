package com.jorislodewijks.hardcorerevival.altar;

import java.io.Serializable;

public class AltarSaveData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8060120572132123543L;
	private String creator;
	private int x, y, z;

	public AltarSaveData(String creator, int x, int y, int z) {
		this.creator = creator;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String getCreatorUUID() {
		return this.creator;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getZ() {
		return this.z;
	}

}
