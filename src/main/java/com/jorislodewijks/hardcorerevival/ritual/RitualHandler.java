package com.jorislodewijks.hardcorerevival.ritual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.jorislodewijks.hardcorerevival.HardcoreRevival.ResurrectionType;
import com.jorislodewijks.hardcorerevival.altar.Altar;
import com.jorislodewijks.hardcorerevival.ritual.Ritual.RitualType;
import com.jorislodewijks.hardcorerevival.ritual.source.SourceHandler;

public class RitualHandler implements Listener {

	@SuppressWarnings("serial")
	private static List<Ritual> _rituals = new ArrayList<Ritual>(
			Arrays.asList(new Ritual("Ritual Instruction Spawning", ResurrectionType.CULT, RitualType.BASIC_SOURCE_BOOK,
					"CULT_BASIC_INSTRUCTION_BOOK", new HashMap<Material, Integer>() {
						{
							put(Material.BOOK, 1);
							put(Material.FEATHER, 1);
							put(Material.INK_SAC, 1);
						}
					}, null, null, 10), new Ritual("Bodily Sacrificial Revival", ResurrectionType.CULT,
							RitualType.REVIVAL, "CULT_BASIC_INSTRUCTION_BOOK", new HashMap<Material, Integer>() {
								{
									put(Material.TOTEM_OF_UNDYING, 1);
									put(Material.BONE, 8);
									put(Material.REDSTONE, 16);
									put(Material.GUNPOWDER, 2);
								}
							}, null, null, 60),
					new Ritual("Heavenly Revival", ResurrectionType.RELIGIOUS, RitualType.REVIVAL,
							"RELIGIOUS_BASIC_INSTRUCTION_BOOK", new HashMap<Material, Integer>() {
								{
									put(Material.TOTEM_OF_UNDYING, 1);
									put(Material.GOLDEN_APPLE, 1);
									put(Material.EMERALD, 16);
									put(Material.POPPY, 1);
								}
							}, null, null, 60),
					new Ritual("The Lords instructions", ResurrectionType.RELIGIOUS, RitualType.BASIC_SOURCE_BOOK,
							"RELIGIOUS_BASIC_INSTRUCTION_BOOK", new HashMap<Material, Integer>() {
								{
									put(Material.BOOK, 1);
									put(Material.FEATHER, 1);
									put(Material.INK_SAC, 1);
								}
							}, null, null, 10),
					new Ritual("Villager Curification", ResurrectionType.RELIGIOUS, RitualType.VILLAGER_CURING,
							"RELIGIOUS_BASIC_INSTRUCTION_BOOK", new HashMap<Material, Integer>() {
								{
									put(Material.POTION, 1);
									put(Material.FERMENTED_SPIDER_EYE, 1);
									put(Material.BLAZE_POWDER, 1);
								}
							}, new HashMap<EntityType, Integer>() {
								{
									put(EntityType.ZOMBIE_VILLAGER, 1);
								}
							}, null, 10)));

	@EventHandler
	public void OnRitualCompleteEvent(OnRitualCompletionEvent event) {

		// Check for skull item.
		SkullMeta skullMeta = null;
		if (event.getRitual().getIngredients().containsKey(Material.PLAYER_HEAD)) {
			List<Entity> items = event.getAltar().getInventoryAsEntities();

			for (Entity e : items) {
				Item i = (Item) e;
				if (((Item) i).getItemStack().getItemMeta() instanceof SkullMeta) {
					skullMeta = (((SkullMeta) ((Item) i).getItemStack().getItemMeta()));
				}
			}
		}

		// Remove Ingredients and entities.
		removeIngredients(event.getAltar(), event.getRitual().getIngredients());
		if (event.getRitual().getRitualType() != RitualType.VILLAGER_CURING)
			removeEntities(event.getAltar().getMobEntitiesWithTypes(event.getRitual().getEntityTypes()));

		// Spawn results.
		spawnResults(event.getAltar(), event.getRitual().getResults());

		// General FX on completion
		switch (event.getAltar().getAltarType()) {
		case ANY:
			break;
		case CULT:
			event.getAltar().getImportantBlock().getWorld().playSound(
					event.getAltar().getImportantBlock().getLocation(), Sound.ENTITY_PARROT_IMITATE_GHAST, 1.0f, 0.1f);
			break;
		case RELIGIOUS:
			event.getAltar().getImportantBlock().getWorld().spawnParticle(Particle.FIREWORKS_SPARK,
					event.getAltar().getImportantBlock().getX() + 0.5,
					event.getAltar().getImportantBlock().getY() + 0.5,
					event.getAltar().getImportantBlock().getZ() + 0.5, 255, 0.25, 0.25, 0.25, 1);

			event.getAltar().getImportantBlock().getWorld().playSound(
					event.getAltar().getImportantBlock().getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_BEACON_ACTIVATE,
					2.0f, 2.0f);
			break;
		default:
			break;

		}

		// Specials on ritual completion?
		switch (event.getRitual().getRitualType()) {
		case NORMAL:
			break;
		case BASIC_SOURCE_BOOK:
			event.getAltar().getImportantBlock().getWorld().dropItemNaturally(event.getAltar().getItemDropLocation(),
					SourceHandler.getSourceBookItem(event.getAltar().getAltarType(),
							event.getRitual().getSourceName()));
			break;
		case PRAY:
			break;
		case REVIVAL:
			break;
		case VILLAGER_CURING:
			ZombieVillager zVillager = (ZombieVillager) event.getAltar()
					.getFirstMobEntityWithType(EntityType.ZOMBIE_VILLAGER);

			Player player = event.getAltar().getNearestPlayer(GameMode.SURVIVAL);

			zVillager.setConversionPlayer(
					Bukkit.getOfflinePlayer(player != null ? player.getUniqueId() : event.getAltar().getCreatorUUID()));
			zVillager.setConversionTime(0);
			break;
		default:
			break;
		}

//		switch (event.getRitual().getResurrectionType()) {
//		case CULT:
//			event.getAltar().getImportantBlock().getWorld().playSound(
//					event.getAltar().getImportantBlock().getLocation(), Sound.ENTITY_PARROT_IMITATE_GHAST, 1.0f, 0.1f);
//
//			switch (event.getRitual().getRitualType()) {
//			case REVIVAL:
//				if (skullMeta != null) {
//					RevivalHandler.revivePlayer(PlayerHandler.getPlayerFromSkull(skullMeta),
//							event.getAltar().getImportantBlock().getLocation(), event.getAltar().getAltarType());
//				} else {
//					RevivalHandler.reviveNearestDeadPlayer(
//							event.getAltar().getImportantBlock().getLocation().add(0.5, 0.5, 0.5),
//							event.getAltar().getImportantBlock().getLocation().add(0.5, 0.5, 0.5),
//							event.getRitual().getResurrectionType());
//				}
//				break;
//			case BASIC_SOURCE_BOOK:
//				event.getAltar().getImportantBlock().getWorld().dropItemNaturally(
//						event.getAltar().getImportantBlock().getLocation().add(0.5, 0.5, 0.5),
//						new CultBookHandler().getSourceItem(event.getRitual().getSourceType()));
//				break;
//			case PRAY:
//				PlayerHandler.giveEffectToNearestPlayer(event.getAltar().getImportantBlock().getLocation(),
//						new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60 * 5, 1));
//				break;
//			case VILLAGER_CURING:
//				break;
//			default:
//				break;
//			}
//			break;
//		case RELIGIOUS:
//			event.getAltar().getImportantBlock().getWorld().spawnParticle(Particle.FIREWORKS_SPARK,
//					event.getAltar().getImportantBlock().getX() + 0.5,
//					event.getAltar().getImportantBlock().getY() + 0.5,
//					event.getAltar().getImportantBlock().getZ() + 0.5, 255, 0.25, 0.25, 0.25, 1);
//
//			event.getAltar().getImportantBlock().getWorld().playSound(
//					event.getAltar().getImportantBlock().getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_BEACON_ACTIVATE,
//					2.0f, 2.0f);
//
//			switch (event.getRitual().getRitualType()) {
//			case REVIVAL:
//
//				if (skullMeta != null) {
//					RevivalHandler.revivePlayer(PlayerHandler.getPlayerFromSkull(skullMeta),
//							event.getAltar().getImportantBlock().getLocation(), event.getAltar().getAltarType());
//				} else {
//					RevivalHandler.reviveNearestDeadPlayer(
//							event.getAltar().getImportantBlock().getLocation().add(0.5, 1.5, 0.5),
//							event.getAltar().getImportantBlock().getLocation().add(0.5, 1.5, 0.5),
//							event.getAltar().getAltarType());
//				}
//				break;
//			case BASIC_SOURCE_BOOK:
//				event.getAltar().getImportantBlock().getWorld().dropItemNaturally(
//						event.getAltar().getImportantBlock().getLocation().add(0.5, 0.5, 0.5),
//						new ReligiousBookHandler().getInstructionBook(event.getRitual().getSource()));
//				break;
//			case PRAY:
//				PlayerHandler.giveEffectToPlayersWithinRange(event.getAltar().getImportantBlock().getLocation(), 5,
//						new PotionEffect(PotionEffectType.REGENERATION, 20 * 60 * 5, 1));
//				break;
//			case VILLAGER_CURING:
//				ZombieVillager zVillager = (ZombieVillager) event.getAltar()
//						.getFirstMobEntityWithType(EntityType.ZOMBIE_VILLAGER);
//
//				Player player = PlayerHandler.getNearestPlayer(event.getAltar().getPlayers(), GameMode.SURVIVAL,
//						event.getAltar().getImportantBlock().getLocation());
//
//				zVillager.setConversionPlayer(Bukkit
//						.getOfflinePlayer(player != null ? player.getUniqueId() : event.getAltar().getCreatorUUID()));
//				zVillager.setConversionTime(0);
//				break;
//			default:
//				break;
//			}
//			break;
//		case ANY:
//			break;
//		default:
//			break;
//		}

		// Deplete altar power.
		event.getAltar().drainPower();
	}

	@EventHandler
	public void OnRitualCanceledEvent(OnRitualCanceledEvent event) {
		if (ThreadLocalRandom.current().nextInt(0, 100) < 50) {
			event.getAltar().deactivePowerSource();
		}

		switch (event.getRitual().getResurrectionType()) {
		case CULT:
			event.getAltar().getImportantBlock().getWorld().playSound(
					event.getAltar().getImportantBlock().getLocation(), Sound.ENTITY_GHAST_DEATH, 1.0f, 0.05f);

			Entity zombie = event.getAltar().getImportantBlock().getWorld().spawnEntity(
					event.getAltar().getImportantBlock().getLocation().add(0.5, 0.5, 0.5), EntityType.ZOMBIE);
			zombie.setCustomName("Forsaken Experiment");
			zombie.playEffect(EntityEffect.ENTITY_POOF);
			break;
		case RELIGIOUS:
			event.getAltar().getImportantBlock().getWorld().playSound(
					event.getAltar().getImportantBlock().getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1.0f, 1.0f);
			break;
		case ANY:
			break;
		default:
			break;
		}
	}

	private void removeIngredients(Altar altar, HashMap<Material, Integer> items) {
		// For now, delete all the items on the altar. So, not only the ingredients.

		if (items == null || items.isEmpty())
			return;

		for (Entity e : altar.getInventoryAsEntities()) {
			e.remove();
		}
	}

	private void removeEntities(List<Entity> entities) {
		if (entities == null || entities.isEmpty())
			return;

		for (Entity e : entities) {
			e.remove();
		}
	}

	private void spawnResults(Altar altar, List<ItemStack> items) {
		if (items == null)
			return;

		for (ItemStack itemStack : items) {
			altar.getImportantBlock().getWorld()
					.dropItemNaturally(altar.getImportantBlock().getLocation().add(0.5, 0.5, 0.5), itemStack);
		}
	}

	public static boolean hasRituals() {
		return !_rituals.isEmpty();
	}

	// Override preset rituals.
	public static void setRituals(List<Ritual> rituals) {
		_rituals = rituals;
	}

	public static List<Ritual> getRituals() {
		return _rituals;
	}

	public static List<Ritual> getRituals(ResurrectionType resurrectionType) {
		List<Ritual> rituals = new ArrayList<Ritual>();

		_rituals.forEach(r -> {
			if (r.getResurrectionType() == resurrectionType)
				rituals.add(r);
		});

		return rituals;
	}

	public static List<Ritual> getRitualsWithSource(String source) {
		List<Ritual> rituals = new ArrayList<Ritual>();

		_rituals.forEach(r -> {
			if (r.getSourceName() == source)
				rituals.add(r);
		});

		return rituals;
	}

}