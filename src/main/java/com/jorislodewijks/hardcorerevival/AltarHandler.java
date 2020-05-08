package com.jorislodewijks.hardcorerevival;

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

import com.jorislodewijks.hardcorerevival.HardcoreRevival.ResurrectionType;

public class AltarHandler implements Listener {
	private final HardcoreRevival plugin;
	public static List<Altar> altars;

	public AltarHandler(HardcoreRevival plugin) {
		this.plugin = plugin;
		altars = new ArrayList<Altar>();
	}

	@EventHandler
	public void BlockPlaceEvent​(BlockPlaceEvent event) {
		if (Altar.CultMaterials.contains(event.getBlock().getType())) {
			if (Altar.checkBlocksForCultAltarValidity(Altar.getCultAltarBlocks(event.getBlock()))) {
				registerNewAltar(plugin, Altar.getCultAltarBlocks(event.getBlock()), ResurrectionType.CULT);
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
					registerNewAltar(plugin, Altar.getCultAltarBlocks(event.getBlock()), ResurrectionType.CULT);
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
					registerNewAltar(plugin, Altar.getCultAltarBlocks(event.getBlock()), ResurrectionType.CULT);
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

	public static Altar registerNewAltar(HardcoreRevival plugin, List<Block> blocks, ResurrectionType type) {
		Altar altar;
		altar = new Altar(plugin, type, blocks);
		altars.add(altar);
		return altar;
	}

	public static void removeAltar(Altar altar) {
		System.out.println("Removing Altar.");
		altar.stopTasks();
		altars.remove(altar);
	}

}