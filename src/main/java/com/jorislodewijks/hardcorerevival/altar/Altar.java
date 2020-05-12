package com.jorislodewijks.hardcorerevival.altar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;

import com.jorislodewijks.hardcorerevival.HardcoreRevival;
import com.jorislodewijks.hardcorerevival.HardcoreRevival.ResurrectionType;
import com.jorislodewijks.hardcorerevival.players.PlayerHandler;
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
			if (ThreadLocalRandom.current().nextInt(0, 100) < 50) {
				this.deactivePowerSource();
			}
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
			int times = ThreadLocalRandom.current().nextInt(1, 8 + 1);
			for (int i = 0; i < times; i++) {
				int x = 0, z = 0;
				while (x == 0 && z == 0) {
					x = ThreadLocalRandom.current().nextInt(-1, 1 + 1);
					z = ThreadLocalRandom.current().nextInt(-1, 1 + 1);
				}

				Block block = this.getImportantBlock().getRelative(x, 0, z);

				block.setType(Material.OBSIDIAN);
				block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 1.0f);
				block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation(), 50,
						Material.OBSIDIAN.createBlockData());

			}
			break;
		default:
			break;
		}
	}

	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<Player>();
		for (Entity e : this.getMobEntities()) {
			if (e instanceof Player) {
				players.add((Player) e);
			}
		}

		return players;
	}

	public Player getNearestPlayer(GameMode gameMode) {
		List<Player> players = this.getPlayers();
		return PlayerHandler.getNearestPlayer(players, gameMode, this.getImportantBlock().getLocation());
	}

	public List<Entity> getMobEntitiesWithTypes(List<EntityType> entityTypes) {
		if (entityTypes == null)
			return null;

		List<Entity> entities = new ArrayList<Entity>();

		List<Entity> entitiesAtAltar = this.getMobEntities();
		List<EntityType> entityTypesAtAltar = this.getMobEntityTypes();

		for (EntityType type : entityTypes) {
			if (entityTypesAtAltar.contains(type)) {
				int index = entityTypesAtAltar.indexOf(type);
				entities.add(entitiesAtAltar.get(index));

				entitiesAtAltar.remove(index);
				entityTypesAtAltar.remove(index);
			}
		}

		return entities;
	}

	public List<Entity> getMobEntitiesWithType(EntityType type) {
		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> entitiesAtAltar = this.getMobEntities();

		entitiesAtAltar.forEach((e) -> {
			if (e.getType() == type)
				entities.add(e);
		});

		return entities;
	}

	public Entity getFirstMobEntityWithType(EntityType type) {
		List<Entity> entitiesAtAltar = this.getMobEntities();
		List<EntityType> entityTypesAtAltar = this.getMobEntityTypes();

		if (entityTypesAtAltar.contains(type))
			return entitiesAtAltar.get(entityTypesAtAltar.indexOf(type));
		else
			return null;
	}

	public List<EntityType> getMobEntityTypes() {
		List<EntityType> entityTypes = new ArrayList<EntityType>();
		this.getMobEntities().forEach((e) -> entityTypes.add(e.getType()));
		return entityTypes;
	}

	public List<Entity> getMobEntities() {
		List<Entity> entities = new ArrayList<Entity>();
		for (Entity e : this.getEntities(5f)) {
			if (e instanceof Mob || e instanceof Player) {
				entities.add(e);
			}
		}
		return entities;
	}
	
	public Location getItemDropLocation() {
		switch(this.altarType) {
		case CULT:
			return this.getImportantBlock().getLocation().add(0.5, 0.5, 0.5);
			
		case ANY:
		case RELIGIOUS:
		default:
			return this.getImportantBlock().getLocation().add(0.5, 1.5, 0.5);
		
		}
	}

	public HashMap<Material, Integer> getInventory() {
		HashMap<Material, Integer> items = new HashMap<Material, Integer>();
		for (Entity e : this.getEntities(0.5f)) {
			if (e instanceof Item) {
				if (items.containsKey(((Item) e).getItemStack().getType())) {
					items.put(((Item) e).getItemStack().getType(),
							items.get(((Item) e).getItemStack().getType()) + ((Item) e).getItemStack().getAmount());
				} else {
					items.put(((Item) e).getItemStack().getType(), ((Item) e).getItemStack().getAmount());
				}
			}
		}
		return items;
	}

	public List<Entity> getInventoryAsEntities() {
		List<Entity> items = new ArrayList<Entity>();
		for (Entity e : this.getEntities(0.5f)) {
			if (e instanceof Item) {
				items.add(e);
			}
		}
		return items;
	}

	private Collection<Entity> getEntities(float range) {
		Location itemsLocation = this.getInventoryLocation();

		return this.getImportantBlock().getWorld()
				.getNearbyEntities(new BoundingBox(itemsLocation.getX() - range, itemsLocation.getY() - range,
						itemsLocation.getZ() - range, itemsLocation.getX() + range, itemsLocation.getY() + range,
						itemsLocation.getZ() + range));
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
			} else {
				return null;
			}

			return Altar.getReligiousAltarBlocks(block);
		} else if (block.getType() == Material.WHITE_STAINED_GLASS) {
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					if (block.getRelative(x, 0, z).getType() == Material.EMERALD_BLOCK) {
						return Altar.getReligiousAltarBlocks(block.getRelative(x, 0, z));
					}
				}
			}
		}

		return blocks;
	}

	public static boolean checkBlocksForCultAltarValidity(List<Block> blocks) {
		boolean hasCauldron = false;

		if (blocks == null)
			return false;

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

		if (blocks == null)
			return false;

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