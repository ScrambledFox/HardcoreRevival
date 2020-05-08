package com.jorislodewijks.hardcorerevival.ritual;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.jorislodewijks.hardcorerevival.HardcoreRevival;
import com.jorislodewijks.hardcorerevival.altar.Altar;

public class RitualRunnerTask extends BukkitRunnable {
	private final Altar altar;
	private final Ritual ritual;

	private int currentTimer;

	public RitualRunnerTask(Altar altar, Ritual ritual) {
		this.altar = altar;
		this.ritual = ritual;

		this.currentTimer = 0;

		HardcoreRevival.instance.getLogger().info("Starting " + this.ritual.getName() + " Ritual!");
	}

	@Override
	public void run() {
		// Check if ritual complete.

		if (currentTimer < ritual.getRitualTime()) {
			currentTimer++;
		} else {
			// Ritual complete.

			this.complete();

			HardcoreRevival.instance.getLogger().info(ritual.getName() + " has been completed.");
		}
	}

	private void complete() {
		// See how we can do other code execution?

		this.removeIngredients(this.ritual.getIngredients());
		this.spawnResults(this.ritual.getResults());
		altar.completeRitual();
	}

	private void removeIngredients(List<ItemStack> items) {
		for (Entity e : this.altar.getAlterItemEntities()) {
			if (e instanceof Item) {
				// if(((Item)e).getItemStack())

				// Remove all for now. We may do additive removal..
				e.remove();
			}
		}
	}

	private void spawnResults(List<ItemStack> items) {
		for (ItemStack itemStack : items) {
			this.altar.getImportantBlock().getWorld()
					.dropItemNaturally(this.altar.getImportantBlock().getLocation().add(0.5, 0.5, 0.5), itemStack);
		}
	}

}
