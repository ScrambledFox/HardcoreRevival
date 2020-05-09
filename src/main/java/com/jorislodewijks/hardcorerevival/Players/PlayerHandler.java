package com.jorislodewijks.hardcorerevival.Players;

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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

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

	public void giveEffectToNearestPlayer(Location searchLocation, PotionEffect effect) {
		List<Player> players = Bukkit.getWorlds().get(0).getPlayers();
		Player closestSurivalPlayer = null;
		double closestDistanceSqr = Double.MAX_VALUE;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getGameMode() != GameMode.SPECTATOR) {
				if (searchLocation.distanceSquared(players.get(i).getLocation()) < closestDistanceSqr) {
					closestDistanceSqr = searchLocation.distanceSquared(players.get(i).getLocation());
					closestSurivalPlayer = players.get(i);
				}
			}
		}

		closestSurivalPlayer.addPotionEffect(effect);
	}

	public void giveEffectToPlayersWithinRange(Location searchLocation, float range, PotionEffect effect) {
		for (Player player : getPlayersWithinRange(searchLocation, range)) {
			player.addPotionEffect(effect);
		}
	}

	public void giveEffectsToPlayersWithinRange(Location searchLocation, float range, List<PotionEffect> effects) {
		for (Player player : getPlayersWithinRange(searchLocation, range)) {
			player.addPotionEffects(effects);
		}
	}

	private List<Player> getPlayersWithinRange(Location searchLocation, float range) {
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
	public ItemStack getPlayerSkull(Player player, List<String> lore) {
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

}