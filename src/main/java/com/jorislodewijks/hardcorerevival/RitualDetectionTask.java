package com.jorislodewijks.hardcorerevival;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class RitualDetectionTask extends BukkitRunnable {
	private final HardcoreRevival plugin;

	private Altar altar;

	public RitualDetectionTask(HardcoreRevival plugin, Altar altar) {
		this.plugin = plugin;
		this.altar = altar;
	}

	@Override
	public void run() {
		List<Ritual> rituals = altar.getRituals(altar.getAltarType());
		List<ItemStack> items = altar.getAltarInventory();

		if (altar.hasActiveRitual()) {
			if (altar.getActiveRitual().meetsIngredients(items)) {
				// ga zo door
			} else {
				altar.cancelRitual();
			}
		} else {

			for (Ritual ritual : rituals) {
				if (ritual.meetsIngredients(items)) {
					altar.setActiveRitual(ritual);
				}
			}

			System.out.println("ITEMS: " + items.toString());
		}

	}

}