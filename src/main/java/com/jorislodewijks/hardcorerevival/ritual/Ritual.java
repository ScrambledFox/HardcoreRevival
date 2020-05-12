package com.jorislodewijks.hardcorerevival.ritual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.jorislodewijks.hardcorerevival.HardcoreRevival.ResurrectionType;

public class Ritual {

	public static enum RitualType {
		NORMAL, BASIC_SOURCE_BOOK, REVIVAL, PRAY, VILLAGER_CURING
	};

	private String name;
	private String description;
	private ResurrectionType resurrectionType;
	private RitualType ritualType;
	private String source;
	private HashMap<Material, Integer> ingredients; // What ingredients are necessary for this ritual?
	private HashMap<EntityType, Integer> entities;
	private List<ItemStack> results;
	
	private int time;

	private int karmaThreshold;
	private int karmaCost;

	private int minimumCasters;
	private int currentPlayers;

	public Ritual(String name, ResurrectionType resurrectionType, RitualType ritualType, String ritualSource,
			HashMap<Material, Integer> ingredients, HashMap<EntityType, Integer> entities, List<ItemStack> results,
			int time) {
		this.name = name;
		this.resurrectionType = resurrectionType;
		this.ritualType = ritualType;
		this.source = ritualSource;
		this.ingredients = ingredients;
		this.entities = entities;
		this.results = results;
		this.time = time;
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

	public String getSourceName() {
		return this.source;
	}

	public HashMap<Material, Integer> getIngredients() {
		return this.ingredients;
	}

	public List<EntityType> getEntityTypes() {
		List<EntityType> entities = new ArrayList<EntityType>();

		if (this.entities == null)
			return null;

		if (this.entities == null)
			return null;

		for (Map.Entry<EntityType, Integer> cursor : this.entities.entrySet()) {
			if (!entities.contains(cursor.getKey()))
				entities.add(cursor.getKey());
		}

		return entities;
	}

	public boolean meetsIngredients(HashMap<Material, Integer> items) {
		if (this.ingredients == null)
			return true;

		if (this.ingredients.isEmpty())
			return true;

		for (Map.Entry<Material, Integer> cursor : this.ingredients.entrySet()) {
			if (items.containsKey(cursor.getKey())) {
				if (items.get(cursor.getKey()) < cursor.getValue()) {
					return false;
				}
			} else {
				return false;
			}
		}

		return true;
	}

	public boolean meetsEntities(List<Entity> entitiesInput) {
		List<Entity> entities = new ArrayList<Entity>();
		entities.addAll(entitiesInput);
		
		if (this.entities == null)
			return true;

		if (this.entities.isEmpty())
			return true;

		if (entities.isEmpty())
			return false;

		for (Map.Entry<EntityType, Integer> cursor : this.entities.entrySet()) {
			for (int i = 0; i < cursor.getValue(); i++) {
				if (!entities.stream().anyMatch(e -> e.getType() == cursor.getKey())) {
					return false;
				} else {
					entities.removeIf(e -> e.getType() == cursor.getKey());
				}
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