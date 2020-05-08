package com.jorislodewijks.hardcorerevival.altar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;

import com.jorislodewijks.hardcorerevival.HardcoreRevival;
import com.jorislodewijks.hardcorerevival.HardcoreRevival.ResurrectionType;
import com.jorislodewijks.hardcorerevival.RevivalHandler;
import com.jorislodewijks.hardcorerevival.books.CultBookHandler;
import com.jorislodewijks.hardcorerevival.ritual.Ritual;
import com.jorislodewijks.hardcorerevival.ritual.RitualDetectionTask;
import com.jorislodewijks.hardcorerevival.ritual.RitualRunnerTask;

public class Altar {
	public static List<Material> CultMaterials = new ArrayList<Material>(
			Arrays.asList(Material.CAULDRON, Material.FIRE, Material.CAMPFIRE));
	public static List<Material> ReligiousMaterials = new ArrayList<Material>(
			Arrays.asList(Material.DIAMOND_BLOCK, Material.EMERALD_BLOCK));

	private static List<Ritual> CultRituals = new ArrayList<Ritual>(Arrays.asList(
			new Ritual("Ritual Instruction Spawning",
					new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Material.BOOK, 1),
							new ItemStack(Material.FEATHER, 1), new ItemStack(Material.INK_SAC, 1))),
					new ArrayList<ItemStack>(Arrays.asList(new CultBookHandler().getInstructionBook())), 10, false,
					-100, -50, -1),
			new Ritual("Bodily Sacrificial Revival",
					new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Material.TOTEM_OF_UNDYING, 1))), null, 10,
					false, -100, -50, -1)));

	private static List<Ritual> ReligiousRituals = new ArrayList<Ritual>(
			Arrays.asList(new Ritual("Bodily Sacrificial Revival",
					new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Material.TOTEM_OF_UNDYING, 1))), null, 10,
					false, -100, -50, -1)));

	private ResurrectionType altarType;
	private List<Block> blocks;
	private UUID creatorUUID;

	private Ritual activeRitual;

	private BukkitTask particleTask;
	private BukkitTask validityTask;
	private BukkitTask ritualDetectionTask;
	private BukkitTask ritualRunnerTask;

	public Altar(UUID creatorUUID, ResurrectionType altarType, List<Block> blocks) {
		this.creatorUUID = creatorUUID;
		this.altarType = altarType;
		this.blocks = blocks;

		particleTask = new AltarParticleEffectsTask(this).runTaskTimer(HardcoreRevival.instance, 0, 2);
		validityTask = new AltarValidityTask(this).runTaskTimer(HardcoreRevival.instance, 20, 20);
		ritualDetectionTask = new RitualDetectionTask(this).runTaskTimer(HardcoreRevival.instance, 0, 20/* * 10 */);
	}

	public void stopTasks() {
		if (particleTask != null)
			particleTask.cancel();
		if (validityTask != null)
			validityTask.cancel();
		if (ritualDetectionTask != null)
			ritualDetectionTask.cancel();
		if (ritualRunnerTask != null)
			ritualRunnerTask.cancel();
	}

	public Block getImportantBlock() {
		switch (altarType) {
		case CULT:
			for (Block block : blocks) {
				if (block.getType() == CultMaterials.get(0)) {
					return block;
				}
			}
			break;
		case RELIGIOUS:
			for (Block block : blocks) {
				if (block.getType() == ReligiousMaterials.get(0)) {
					return block;
				}
			}
			break;
		}

		return null;
	}

	public List<Block> getBlocks() {
		return this.blocks;
	}

	public ResurrectionType getAltarType() {
		return this.altarType;
	}

	public List<Ritual> getRituals(ResurrectionType type) {
		switch (type) {
		case CULT:
			return CultRituals;
		case RELIGIOUS:
			return ReligiousRituals;
		}

		return null;
	}

	public Ritual getActiveRitual() {
		return this.activeRitual;
	}

	public void setActiveRitual(Ritual ritual) {
		if (this.hasActiveRitual())
			this.cancelRitual();

		this.activeRitual = ritual;
		ritualRunnerTask = new RitualRunnerTask(this, ritual).runTaskTimer(HardcoreRevival.instance, 0, 20);
	}

	public boolean hasActiveRitual() {
		return this.activeRitual != null ? true : false;
	}

	public void cancelRitual() {
		ritualRunnerTask.cancel();
		this.activeRitual = null;
	}

	public void completeRitual() {
		ritualRunnerTask.cancel();
		this.activeRitual = null;

		new RevivalHandler().reviveNearestDeadPlayer(this.getImportantBlock().getLocation().add(0.5, 0.5, 0.5),
				this.getImportantBlock().getLocation().add(0.5, 0.5, 0.5), this.altarType);
	}

	public List<ItemStack> getAltarInventory() {
		List<ItemStack> items = new ArrayList<ItemStack>();
		for (Entity e : this.getAlterItemEntities()) {
			items.add(((Item) e).getItemStack());
		}
		return items;
	}

	public List<Entity> getAlterItemEntities() {
		List<Entity> items = new ArrayList<Entity>();
		Location itemsLocation = this.getInventoryLocation();

		for (Entity entity : this.getImportantBlock().getWorld()
				.getNearbyEntities(new BoundingBox(itemsLocation.getX() - 0.5, itemsLocation.getY() - 0.5,
						itemsLocation.getZ() - 0.5, itemsLocation.getX() + 0.5, itemsLocation.getY() + 0.5,
						itemsLocation.getZ() + 0.5))) {
			if (entity instanceof Item) {
				items.add(entity);
			}
		}

		return items;
	}

	private Location getInventoryLocation() {
		switch (this.altarType) {
		case CULT:
			return this.getImportantBlock().getLocation().add(0.5, 0.5, 0.5);
		case RELIGIOUS:
			return this.getImportantBlock().getRelative(BlockFace.UP).getLocation().add(0.5, 0.5, 0.5);
		}

		return null;
	}

	public static List<Block> getCultAltarBlocks(Block block) {
		List<Block> blocks = new ArrayList<Block>();
		if (block.getType() == Material.CAULDRON) {
			blocks.add(block);
			blocks.add(block.getRelative(BlockFace.DOWN));
		}
		if (block.getType() == Material.FIRE || block.getType() == Material.CAMPFIRE) {
			blocks.add(block);
			blocks.add(block.getRelative(BlockFace.UP));
		}

		return blocks;
	}

	public static List<Block> getReligiousAltarBlocks(Block block) {
		return null;
	}

	public static boolean checkBlocksForCultAltarValidity(List<Block> blocks) {
		boolean hasCauldron = false;
		for (Block block : blocks) {
			if (block.getType() == Material.CAULDRON) {
				hasCauldron = true;

				Levelled cauldronData = (Levelled) block.getBlockData();
				if (cauldronData.getLevel() > 0) {

					Block fireSourceBlock = block.getRelative(BlockFace.DOWN);
					if (fireSourceBlock.getType() == Material.FIRE || fireSourceBlock.getType() == Material.CAMPFIRE) {
						if (fireSourceBlock.getType() == Material.CAMPFIRE) {

							Lightable campfireData = (Lightable) fireSourceBlock.getBlockData();
							if (!campfireData.isLit()) {
								return false;
							}

						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}

		if (!hasCauldron) {
			return false;
		}

		return true;
	}

	public static boolean checkBlocksForReligiousAltarValidity(List<Block> blocks) {
		return false;
	}

}