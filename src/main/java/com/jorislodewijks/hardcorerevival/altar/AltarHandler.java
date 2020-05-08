package com.jorislodewijks.hardcorerevival.altar;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.CauldronLevelChangeEvent;

import com.jorislodewijks.hardcorerevival.HardcoreRevival;
import com.jorislodewijks.hardcorerevival.HardcoreRevival.ResurrectionType;
import com.jorislodewijks.hardcorerevival.karma.KarmaHandler;

public class AltarHandler implements Listener {
	public static List<Altar> altars;

	public AltarHandler() {
		altars = new ArrayList<Altar>();
	}

	@EventHandler
	public void BlockPlaceEvent​(BlockPlaceEvent event) {
		if (Altar.CultMaterials.contains(event.getBlock().getType())) {
			if (Altar.checkBlocksForCultAltarValidity(Altar.getCultAltarBlocks(event.getBlock()))) {
				registerNewAltar(event.getPlayer(), Altar.getCultAltarBlocks(event.getBlock()),
						ResurrectionType.CULT);
			}
		}
		if (Altar.ReligiousMaterials.contains(event.getBlock().getType())) {
			// CheckBlocksForAltarValidity(event.getBlock());
		}
	}

	@EventHandler
	public void CauldronLevelChangeEvent(CauldronLevelChangeEvent event) {
		Levelled data = (Levelled) event.getBlock().getBlockData();
		data.setLevel(event.getNewLevel());
		event.getBlock().setBlockData(data);

		if (event.getNewLevel() > 0) {
			if (Altar.checkBlocksForCultAltarValidity(Altar.getCultAltarBlocks(event.getBlock()))) {
				boolean exists = false;
				for (Altar altar : altars) {
					if (altar.getBlocks().contains(event.getBlock())) {
						exists = true;
					}
				}

				if (!exists) {
					Player p = null;
					if(event.getEntity() instanceof Player)
						p = (Player)event.getEntity();
					registerNewAltar(p, Altar.getCultAltarBlocks(event.getBlock()), ResurrectionType.CULT);
				}
			}
		}
	}

	@EventHandler
	public void BlockIgniteEvent(BlockIgniteEvent event) {
		Player p = event.getPlayer();
		if (event.getCause() == BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL) {
			if (Altar.CultMaterials.contains(event.getBlock().getType())) {
				if (Altar.checkBlocksForCultAltarValidity(Altar.getCultAltarBlocks(event.getBlock()))) {
					registerNewAltar(p, Altar.getCultAltarBlocks(event.getBlock()), ResurrectionType.CULT);
				}
			}
		}
	}

	public Altar getAltarFromBlock(Block block) {
		for (Altar altar : altars) {
			if (altar.getBlocks().contains(block)) {
				return altar;
			}
		}

		return null;
	}

	public static Altar registerNewAltar(Player creator, List<Block> blocks,
			ResurrectionType type) {
		Altar altar;
		altar = new Altar(creator.getUniqueId(), type, blocks);
		altars.add(altar);
		if (creator != null)
			new KarmaHandler().modPlayerKarma(creator, -50);
		return altar;
	}

	public static void removeAltar(Altar altar) {
		altar.stopTasks();
		altars.remove(altar);
	}

}