package com.jorislodewijks.hardcorerevival.ritual;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.jorislodewijks.hardcorerevival.HardcoreRevival;
import com.jorislodewijks.hardcorerevival.altar.Altar;

public class OnRitualCanceledEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	
	private Altar altar;
	private Ritual ritual;
	
	public OnRitualCanceledEvent(Altar altar, Ritual ritual ) {
		this.altar = altar;
		this.ritual = ritual;
		
		HardcoreRevival.instance.getLogger().info(this.ritual.getName() + " has been cancelled!");
		
		altar.removeActiveRitual();
	}
	
	public Altar getAltar() {
		return this.altar;
	}
	
	public Ritual getRitual() {
		return this.ritual;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}