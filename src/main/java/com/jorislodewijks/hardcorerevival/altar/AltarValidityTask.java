package com.jorislodewijks.hardcorerevival.altar;

import org.bukkit.scheduler.BukkitRunnable;

public class AltarValidityTask extends BukkitRunnable {
	private Altar altar;

	public AltarValidityTask(Altar altar) {
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
			if (!Altar.checkBlocksForReligiousAltarValidity(altar.getBlocks())) {
				// Not valid anymore
				AltarHandler.removeAltar(altar);
			}
			break;
		}

	}

}
