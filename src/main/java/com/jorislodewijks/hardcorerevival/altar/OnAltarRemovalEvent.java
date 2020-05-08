package com.jorislodewijks.hardcorerevival.altar;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.jorislodewijks.hardcorerevival.HardcoreRevival;

public class OnAltarRemovalEvent extends Event {
	private static final HandlerList handlers = new HandlerList();

	// Can not exist
	private Altar altar;
	private Location location;

	public OnAltarRemovalEvent(Altar altar, Location location) {
		this.altar = altar;
		this.location = location;

		HardcoreRevival.instance.getLogger()
				.info("A " + altar.getAltarType().toString() + " altar has been destroyed!");
	}

	public Altar getAltar() {
		return this.altar;
	}
	
	public Location getLocation() {
		return this.location;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
