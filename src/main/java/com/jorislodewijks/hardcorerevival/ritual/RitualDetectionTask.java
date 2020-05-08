package com.jorislodewijks.hardcorerevival.ritual;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.jorislodewijks.hardcorerevival.HardcoreRevival;
import com.jorislodewijks.hardcorerevival.altar.Altar;

public class RitualDetectionTask extends BukkitRunnable {

	private Altar altar;

	public RitualDetectionTask(Altar altar) {
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
		}

	}

}