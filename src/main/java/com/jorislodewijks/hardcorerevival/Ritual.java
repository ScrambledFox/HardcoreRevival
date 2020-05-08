package com.jorislodewijks.hardcorerevival;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Ritual {

	private String name;
	private List<ItemStack> ingredients;
	private int time;

	private boolean positiveRitual;
	private int karmaThreshold;
	private int karmaCost;

	private int eventActivationId;

	public Ritual(String name, List<ItemStack> ingredients, int time, boolean positiveRitual, int karmaThreshold,
			int karmaCost, int eventActivationId) {
		this.name = name;
		this.ingredients = ingredients;
		this.time = time;
		this.positiveRitual = positiveRitual;
		this.karmaThreshold = karmaThreshold;
		this.karmaCost = karmaCost;
		this.eventActivationId = eventActivationId;
	}

	public List<ItemStack> getIngredients() {
		return this.ingredients;
	}
	
	public boolean meetsIngredients(List<ItemStack> items) {
		for(ItemStack itemStack : this.ingredients) {
			if (!items.contains(new ItemStack(itemStack.getType()))) {
				return false;
			}
		}
		
		return true;
	}
	
	public int getRitualTime() {
		return time;
	}

}