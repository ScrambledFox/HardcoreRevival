package com.jorislodewijks.hardcorerevival.ritual;

import org.bukkit.Bukkit;
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
			this.complete();
		}
	}

	private void complete() {
		OnRitualCompletionEvent event = new OnRitualCompletionEvent(this.altar, this.ritual);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	public float getProgress() {
		return (currentTimer / (float) ritual.getRitualTime());
	}

}
