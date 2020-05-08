package com.jorislodewijks.hardcorerevival;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class RitualRunnerTask extends BukkitRunnable {
	private final HardcoreRevival plugin;
	private final Altar altar;
	private final Ritual ritual;

	private int currentTimer;

	public RitualRunnerTask(HardcoreRevival plugin, Altar altar, Ritual ritual) {
		this.plugin = plugin;
		this.altar = altar;
		this.ritual = ritual;

		this.currentTimer = 0;

		System.out.println("Starting Ritual!");
	}

	@Override
	public void run() {
		// Check if ritual complete.

		if (currentTimer < ritual.getRitualTime()) {
			currentTimer++;
			System.out.println("Ritual Timer: " + currentTimer);
		} else {
			// Ritual complete.

			this.complete();

			System.out.println("Ritual Complete!");
		}
	}

	private void complete() {
		// See how we can do other code execution?

		this.removeIngredients(this.ritual.getIngredients());
		altar.completeRitual();
	}

	private void removeIngredients(List<ItemStack> items) {
		for(Entity e : this.altar.getAlterItemEntities()) {
			if(e instanceof Item) {
				//if(((Item)e).getItemStack())
				
				// Remove all for now. We may do additive removal..
				e.remove();
			}
		}
	}

}
