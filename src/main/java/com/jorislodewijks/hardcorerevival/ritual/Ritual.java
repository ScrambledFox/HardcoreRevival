package com.jorislodewijks.hardcorerevival.ritual;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Ritual {

	private String name;
	private List<ItemStack> ingredients;
	private List<ItemStack> results;
	private int time;

	private boolean positiveRitual;
	private int karmaThreshold;
	private int karmaCost;

	private int eventActivationId;

	public Ritual(String name, List<ItemStack> ingredients, List<ItemStack> results, int time, boolean positiveRitual,
			int karmaThreshold, int karmaCost, int eventActivationId) {
		this.name = name;
		this.ingredients = ingredients;
		this.results = results;
		this.time = time;
		this.positiveRitual = positiveRitual;
		this.karmaThreshold = karmaThreshold;
		this.karmaCost = karmaCost;
		this.eventActivationId = eventActivationId;
	}
	
	public String getName() {
		return this.name;
	}

	public List<ItemStack> getIngredients() {
		return this.ingredients;
	}

	public boolean meetsIngredients(List<ItemStack> items) {
		for (ItemStack itemStack : this.ingredients) {
			if (!items.contains(new ItemStack(itemStack.getType()))) {
				return false;
			}
		}

		return true;
	}

	public int getRitualTime() {
		return this.time;
	}

	public List<ItemStack> getResults() {
		return this.results;
	}

}