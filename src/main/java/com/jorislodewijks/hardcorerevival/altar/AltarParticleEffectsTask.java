package com.jorislodewijks.hardcorerevival.altar;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import com.jorislodewijks.hardcorerevival.HardcoreRevival;

public class AltarParticleEffectsTask extends BukkitRunnable {
	Altar altar;

	public AltarParticleEffectsTask(Altar altar) {
		this.altar = altar;
	}

	@Override
	public void run() {
		int randomNum = ThreadLocalRandom.current().nextInt(0, 100 + 1);

		if (altar.hasActiveRitual()) {
			doActiveEffects(randomNum);
		} else {
			doPassiveEffects(randomNum);
		}

	}

	private void doPassiveEffects(int rnd) {
		Location location = altar.getImportantBlock().getLocation();

		switch (altar.getAltarType()) {
		case CULT:
			location.getWorld().spawnParticle(Particle.DRAGON_BREATH, location.getX() + 0.5, location.getY() + 0.5,
					location.getZ() + 0.5, 2, 0.25, 0.25, 0.25, 0.01);

			if (rnd == 0) {
				location.getWorld().playSound(this.altar.getImportantBlock().getLocation(),
						Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 1.0f);
			}
			break;
		case RELIGIOUS:
			location.getWorld().spawnParticle(Particle.CRIT_MAGIC, location.getX() + 0.5, location.getY() + 0.5,
					location.getZ() + 0.5, 1, 1, 1, 1, 0.01);

			if (rnd == 0) {
				location.getWorld().playSound(this.altar.getImportantBlock().getLocation(), Sound.BLOCK_BEACON_AMBIENT,
						1.0f, 1.0f);
			}
			break;
		}

	}

	private void doActiveEffects(int rnd) {
		Location location = altar.getImportantBlock().getLocation();

		switch (altar.getAltarType()) {
		case CULT:
			location.getWorld().spawnParticle(Particle.DRAGON_BREATH, location.getX() + 0.5, location.getY() + 0.5,
					location.getZ() + 0.5, 40, 0.25, 0.25, 0.25, 0.001);
			location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location.getX() + 0.5, location.getY() + 0.5,
					location.getZ() + 0.5, 10, 0.2, 0.2, 0.1, 0.1);

			if (rnd < 5) {
				location.getWorld().playSound(this.altar.getImportantBlock().getLocation(),
						Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 1.0f);
			}
			break;
		case RELIGIOUS:
			location.getWorld().spawnParticle(Particle.CRIT_MAGIC, location.getX() + 0.5, location.getY() + 0.5,
					location.getZ() + 0.5, 30, 3, 1, 3, 0.2);
			location.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, location.getX() + 0.5, location.getY() + 0.5,
					location.getZ() + 0.5, 50, 0.25, 0.25, 0.25, 0.05);

			if (rnd < 25) {
				location.getWorld().playSound(this.altar.getImportantBlock().getLocation(), Sound.BLOCK_BEACON_AMBIENT,
						1.0f, 2.0f);
			}
			break;
		}

	}
}