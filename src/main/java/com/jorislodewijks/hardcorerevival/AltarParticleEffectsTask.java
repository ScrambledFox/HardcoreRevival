package com.jorislodewijks.hardcorerevival;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

public class AltarParticleEffectsTask extends BukkitRunnable {
	private final HardcoreRevival plugin;

	Altar altar;

	public AltarParticleEffectsTask(HardcoreRevival plugin, Altar altar) {
		this.plugin = plugin;
		this.altar = altar;
	}

	@Override
	public void run() {
		
		
		if(altar.hasActiveRitual()) {
			doActiveEffects();
		} else {
			doPassiveEffects();
		}
		
		
	}
	
	private void doPassiveEffects() {
		Location location = altar.getImportantBlock().getLocation();
		plugin.getServer().getWorlds().get(0).spawnParticle(Particle.DRAGON_BREATH, location.getX() + 0.5, location.getY() + 0.5,
				location.getZ() + 0.5, 2, 0.25, 0.25, 0.25, 0.01);
	}
	
	private void doActiveEffects() {
		Location location = altar.getImportantBlock().getLocation();
		plugin.getServer().getWorlds().get(0).spawnParticle(Particle.DRAGON_BREATH, location.getX() + 0.5, location.getY() + 0.5,
				location.getZ() + 0.5, 200, 0.25, 0.25, 0.25, 0.01);
	}
}