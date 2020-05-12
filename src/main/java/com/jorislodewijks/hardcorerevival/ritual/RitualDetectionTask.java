package com.jorislodewijks.hardcorerevival.ritual;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import com.jorislodewijks.hardcorerevival.altar.Altar;

public class RitualDetectionTask extends BukkitRunnable {

	private Altar altar;

	public RitualDetectionTask(Altar altar) {
		this.altar = altar;
	}

	@Override
	public void run() {
		List<Ritual> rituals = RitualHandler.getRituals(altar.getAltarType());
		HashMap<Material, Integer> items = altar.getInventory();
		List<Entity> entities = altar.getMobEntities();

		if (altar.hasActiveRitual()) {
			if (altar.getActiveRitual().meetsIngredients(items) && altar.getActiveRitual().meetsEntities(entities)) {
				// No problems found, continue checking...
			} else {
				OnRitualCanceledEvent event = new OnRitualCanceledEvent(altar, altar.getActiveRitual());
				Bukkit.getServer().getPluginManager().callEvent(event);
			}
		} else {

			for (Ritual ritual : rituals) {
				if (ritual.meetsIngredients(items) && ritual.meetsEntities(entities)) {
					altar.setActiveRitual(ritual);
					break;
				}
			}
		}

	}

}