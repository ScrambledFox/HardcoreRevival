package com.jorislodewijks.hardcorerevival.books;

import org.bukkit.inventory.ItemStack;

import com.jorislodewijks.hardcorerevival.ritual.Ritual.RitualSource;

public interface IBookHandler {
	
	public ItemStack getInstructionBook(RitualSource ritualSource);
	
}