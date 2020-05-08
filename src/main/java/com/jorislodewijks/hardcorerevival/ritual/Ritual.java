package com.jorislodewijks.hardcorerevival.ritual;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.jorislodewijks.hardcorerevival.HardcoreRevival.ResurrectionType;

public class Ritual {

	public static enum RitualType {
		INSTRUCTION_BOOK, REVIVAL, PRAY
	};

	private String name;
	private RitualType ritualType;
	private ResurrectionType resurrectionType;
	private List<ItemStack> ingredients; // What ingredients are necessary for this ritual?
	private List<ItemStack> results; // Which resulting items will be spawned? CANNOT SPAWN BOOK AS IT NEEDS THE
										// RITUAL ARRAY... STATIC ERROR
	private int time;

	private int karmaThreshold;
	private int karmaCost;

	private int eventActivationId;

	public Ritual(String name, ResurrectionType resurrectionType, RitualType ritualType, List<ItemStack> ingredients,
			List<ItemStack> results, int time, int karmaThreshold, int karmaCost, int eventActivationId) {
		this.name = name;
		this.resurrectionType = resurrectionType;
		this.ritualType = ritualType;
		this.ingredients = ingredients;
		this.results = results;
		this.time = time;
		this.karmaThreshold = karmaThreshold;
		this.karmaCost = karmaCost;
		this.eventActivationId = eventActivationId;
	}

	public String getName() {
		return this.name;
	}

	public RitualType getRitualType() {
		return this.ritualType;
	}

	public ResurrectionType getResurrectionType() {
		return this.resurrectionType;
	}

	public List<ItemStack> getIngredients() {
		return this.ingredients;
	}

	public boolean meetsIngredients(List<ItemStack> items) {
		for (int i = 0; i < this.ingredients.size(); i++) {
			if (!items.contains(this.ingredients.get(i))) {
				return false;
			}
			
			ItemStack selectedItem = items.get(items.indexOf(this.ingredients.get(i)));
			if (selectedItem.getAmount() < this.ingredients.get(i).getAmount()) {
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