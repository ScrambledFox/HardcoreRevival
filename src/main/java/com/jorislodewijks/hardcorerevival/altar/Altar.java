package com.jorislodewijks.hardcorerevival.altar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;

import com.jorislodewijks.hardcorerevival.HardcoreRevival;
import com.jorislodewijks.hardcorerevival.HardcoreRevival.ResurrectionType;
import com.jorislodewijks.hardcorerevival.ritual.Ritual;
import com.jorislodewijks.hardcorerevival.ritual.RitualDetectionTask;
import com.jorislodewijks.hardcorerevival.ritual.RitualRunnerTask;

public class Altar {
	public static List<Material> CultMaterials = new ArrayList<Material>(
			Arrays.asList(Material.CAULDRON, Material.FIRE, Material.CAMPFIRE));
	public static List<Material> ReligiousMaterials = new ArrayList<Material>(Arrays.asList(Material.EMERALD_BLOCK,
			Material.ENCHANTING_TABLE, Material.WHITE_STAINED_GLASS, Material.LECTERN));

	private ResurrectionType altarType;
	private List<Block> blocks;
	private UUID creatorUUID;

	private Location importantBlockLocation;

	private Ritual activeRitual;

	private BukkitTask particleTask;
	private BukkitTask validityTask;
	private BukkitTask ritualDetectionTask;
	private BukkitTask ritualRunnerTask;

	public Altar(Player creator, ResurrectionType altarType, List<Block> blocks) {
		this.creatorUUID = creator != null ? creator.getUniqueId() : UUID.randomUUID();
		this.altarType = altarType;
		this.blocks = blocks;

		importantBlockLocation = this.getImportantBlock().getLocation();

		OnAltarCreatedEvent event = new OnAltarCreatedEvent(this, creator);
		Bukkit.getServer().getPluginManager().callEvent(event);

		particleTask = new AltarParticleEffectsTask(this).runTaskTimer(HardcoreRevival.instance, 0, 2);
		validityTask = new AltarValidityTask(this).runTaskTimer(HardcoreRevival.instance, 20, 20);
		ritualDetectionTask = new RitualDetectionTask(this).runTaskTimer(HardcoreRevival.instance, 0, 20/* * 10 */);
	}

	public void remove() {
		OnAltarRemovalEvent event = new OnAltarRemovalEvent(this, importantBlockLocation);
		Bukkit.getServer().getPluginManager().callEvent(event);

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

	public UUID getCreatorUUID() {
		return this.creatorUUID;
	}

	public List<Block> getBlocks() {
		return this.blocks;
	}

	public ResurrectionType getAltarType() {
		return this.altarType;
	}

	public Ritual getActiveRitual() {
		return this.activeRitual;
	}

	public void setActiveRitual(Ritual ritual) {
		if (this.hasActiveRitual())
			this.removeActiveRitual();

		this.activeRitual = ritual;
		ritualRunnerTask = new RitualRunnerTask(this, ritual).runTaskTimer(HardcoreRevival.instance, 0, 20);
	}

	public boolean hasActiveRitual() {
		return this.activeRitual != null ? true : false;
	}

	public void removeActiveRitual() {
		ritualRunnerTask.cancel();
		this.activeRitual = null;
	}

	public void drainPower() {
		switch (this.altarType) {
		case CULT:
			Levelled cauldronData = (Levelled) this.getImportantBlock().getBlockData();
			cauldronData.setLevel(cauldronData.getLevel() - 1);
			this.getImportantBlock().setBlockData(cauldronData);

			if (cauldronData.getLevel() == 0) {
				this.deactivePowerSource();
			}
			break;
		case RELIGIOUS:
			break;
		default:
			break;
		}
	}

	public void deactivePowerSource() {
		switch (this.altarType) {
		case CULT:
			Block fireSourceBlock = this.getImportantBlock().getRelative(BlockFace.DOWN);
			if (fireSourceBlock.getType() == Material.CAMPFIRE) {
				Lightable fireData = (Lightable) fireSourceBlock.getBlockData();
				fireData.setLit(false);
				fireSourceBlock.setBlockData(fireData);
			} else if (fireSourceBlock.getType() == Material.FIRE) {
				fireSourceBlock.setType(Material.AIR);
			}
			break;
		case RELIGIOUS:
			break;
		default:
			break;
		}
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
		List<Block> blocks = new ArrayList<Block>();
		if (block.getType() == Material.ENCHANTING_TABLE || block.getType() == Material.LECTERN) {
			blocks.add(block);

			Block middleBlock = null;
			if (block.getRelative(BlockFace.SOUTH, 2).getType() == Material.EMERALD_BLOCK) {
				middleBlock = block.getRelative(BlockFace.SOUTH, 2);
			} else if (block.getRelative(BlockFace.EAST, 2).getType() == Material.EMERALD_BLOCK) {
				middleBlock = block.getRelative(BlockFace.EAST, 2);
			} else if (block.getRelative(BlockFace.NORTH, 2).getType() == Material.EMERALD_BLOCK) {
				middleBlock = block.getRelative(BlockFace.NORTH, 2);
			} else if (block.getRelative(BlockFace.WEST, 2).getType() == Material.EMERALD_BLOCK) {
				middleBlock = block.getRelative(BlockFace.WEST, 2);
			}

			if (middleBlock != null) {

				for (int x = 0; x < 3; x++) {
					for (int z = 0; z < 3; z++) {
						blocks.add(middleBlock.getRelative(x - 1, 0, z - 1));
					}
				}
			}
		} else if (block.getType() == Material.EMERALD_BLOCK) {
			if (block.getRelative(BlockFace.SOUTH, 2).getType() == Material.ENCHANTING_TABLE
					|| block.getRelative(BlockFace.SOUTH, 2).getType() == Material.LECTERN) {
				block = block.getRelative(BlockFace.SOUTH, 2);
			} else if (block.getRelative(BlockFace.EAST, 2).getType() == Material.ENCHANTING_TABLE
					|| block.getRelative(BlockFace.EAST, 2).getType() == Material.LECTERN) {
				block = block.getRelative(BlockFace.EAST, 2);
			} else if (block.getRelative(BlockFace.NORTH, 2).getType() == Material.ENCHANTING_TABLE
					|| block.getRelative(BlockFace.NORTH, 2).getType() == Material.LECTERN) {
				block = block.getRelative(BlockFace.NORTH, 2);
			} else if (block.getRelative(BlockFace.WEST, 2).getType() == Material.ENCHANTING_TABLE
					|| block.getRelative(BlockFace.WEST, 2).getType() == Material.LECTERN) {
				block = block.getRelative(BlockFace.WEST, 2);
			}
			
			return Altar.getReligiousAltarBlocks(block);
		}

		return blocks;
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
		Block middleBlock = null;
		Block table = null;

		for (Block block : blocks) {
			if (block.getType() == Material.EMERALD_BLOCK)
				middleBlock = block;
			if (block.getType() == Material.ENCHANTING_TABLE || block.getType() == Material.LECTERN)
				table = block;
		}

		if (middleBlock == null)
			return false;

		if (table == null)
			return false;

		for (int x = 0; x < 3; x++) {
			for (int z = 0; z < 3; z++) {
				if (middleBlock.getRelative(x - 1, 0, z - 1).getType() != Material.WHITE_STAINED_GLASS) {
					if (x == 1 && z == 1)
						continue;
					return false;
				}
			}
		}

		return true;
	}

}