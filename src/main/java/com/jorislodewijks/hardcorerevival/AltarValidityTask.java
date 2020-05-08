package com.jorislodewijks.hardcorerevival;

import org.bukkit.scheduler.BukkitRunnable;

public class AltarValidityTask extends BukkitRunnable {
	private final HardcoreRevival plugin;
	private Altar altar;

	public AltarValidityTask(HardcoreRevival plugin, Altar altar) {
		this.plugin = plugin;
		this.altar = altar;
	}

	@Override
	public void run() {
		switch (altar.getAltarType()) {
		case CULT:
			if (!Altar.checkBlocksForCultAltarValidity(altar.getBlocks())) {
				// Not valid anymore
				AltarHandler.removeAltar(altar);
			}
			break;
		case RELIGIOUS:
			break;
		}

	}

}
