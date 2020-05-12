package com.jorislodewijks.hardcorerevival.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;

import com.jorislodewijks.hardcorerevival.HardcoreRevival;

public class PlayerHandler implements Listener {

	@EventHandler
	public void OnPlayerDeathEvent(PlayerDeathEvent event) {
		// He went over the cliff.
		if (event.getDeathMessage().contains("fell") || event.getDeathMessage().contains("ground"))
			event.setDeathMessage(event.getEntity().getDisplayName() + " went over the cliff.");

		List<String> headLore = new ArrayList<String>();
//		headLore.add("Died on Day " + event.getEntity().getWorld().getFullTime() % 24000);
		headLore.add(event.getDeathMessage());
		event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),
				getPlayerSkull(event.getEntity(), headLore));
	}

	@EventHandler
	public void OnBlockPlace(BlockPlaceEvent event) {
		boolean isNewVersion = Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList())
				.contains("PLAYER_HEAD");
		Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");

		if (event.getBlockPlaced().getType() == type) {
			ItemStack headItem = event.getItemInHand();
			SkullMeta skullMeta = (SkullMeta) headItem.getItemMeta();
			event.getBlockPlaced().setMetadata("SkullLore",
					new FixedMetadataValue(HardcoreRevival.instance, skullMeta.getLore().get(0)));
		}
	}

	@EventHandler
	public void OnBlockDropItem(BlockDropItemEvent event) {
		boolean isNewVersion = Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList())
				.contains("PLAYER_HEAD");
		Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");

		if(event.getItems() == null || event.getItems().isEmpty())
			return;
		
		if (event.getItems().get(0).getItemStack().getType() == type) {
			Item droppedSkull = event.getItems().get(0);
			SkullMeta skullMeta = (SkullMeta) droppedSkull.getItemStack().getItemMeta();
			if (event.getBlock().hasMetadata("SkullLore")) {
//				for (MetadataValue val : event.getBlock().getMetadata("SkullLore")) {
//					if (val.getOwningPlugin() == HardcoreRevival.instance)
//						skullMeta.setLore(new ArrayList<String>(Arrays.asList(val.asString())));
//				}

				skullMeta.setLore(new ArrayList<String>(
						Arrays.asList(event.getBlock().getMetadata("SkullLore").get(0).asString())));

			} else {
				System.out.println("LOST DATA!");
			}

			droppedSkull.getItemStack().setItemMeta(skullMeta);
		}
	}

	public static void giveEffectToNearestPlayer(Location searchLocation, PotionEffect effect) {
		List<Player> players = Bukkit.getWorlds().get(0).getPlayers();

		PlayerHandler.getNearestPlayer(players, GameMode.SURVIVAL, searchLocation).addPotionEffect(effect);
	}

	public static void giveEffectToPlayersWithinRange(Location searchLocation, float range, PotionEffect effect) {
		for (Player player : getPlayersWithinRange(searchLocation, range)) {
			player.addPotionEffect(effect);
		}
	}

	public static void giveEffectsToPlayersWithinRange(Location searchLocation, float range, List<PotionEffect> effects) {
		for (Player player : getPlayersWithinRange(searchLocation, range)) {
			player.addPotionEffects(effects);
		}
	}

	private static List<Player> getPlayersWithinRange(Location searchLocation, float range) {
		List<Player> players = new ArrayList<Player>();

		Collection<Entity> entities = searchLocation.getWorld().getNearbyEntities(searchLocation, range, range, range);
		for (Entity entity : entities) {
			if (entity instanceof Player) {
				players.add((Player) entity);
			}
		}

		return players;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getPlayerSkull(Player player, List<String> lore) {
		boolean isNewVersion = Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList())
				.contains("PLAYER_HEAD");

		Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");
		ItemStack item = new ItemStack(type, 1);

		if (!isNewVersion)
			item.setDurability((short) 3);

		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(player.getName());

		meta.setDisplayName(player.getName() + "'s Head");
		meta.setLore(lore);

		item.setItemMeta(meta);

		return item;
	}

	public static Player getPlayerFromSkull(SkullMeta skullMeta) {
		return skullMeta.getOwningPlayer().getPlayer();
	}

	public static Player getNearestPlayer(List<Player> players, GameMode gameMode, Location centre) {
		Player closestPlayer = null;
		double closestDistanceSqr = Double.MAX_VALUE;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getGameMode() == gameMode) {
				if (centre.distanceSquared(players.get(i).getLocation()) < closestDistanceSqr) {
					closestDistanceSqr = centre.distanceSquared(players.get(i).getLocation());
					closestPlayer = players.get(i);
				}
			}
		}

		return closestPlayer;
	}
}