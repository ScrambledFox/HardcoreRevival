package com.jorislodewijks.hardcorerevival.altar;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.jorislodewijks.hardcorerevival.HardcoreRevival;

public class OnAltarCreatedEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private Altar altar;
	private Player creator;
	
	public OnAltarCreatedEvent(Altar altar, Player creator) {
		this.altar = altar;
		this.creator = creator;
		
		HardcoreRevival.instance.getLogger().info("A " + altar.getAltarType().toString() + " altar has been created!");
	}
	
	public Altar getAltar() {
		return this.altar;
	}
	
	public Player getCreator() {
		return this.creator;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}