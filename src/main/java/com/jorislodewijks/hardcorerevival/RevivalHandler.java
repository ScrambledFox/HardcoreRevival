package com.jorislodewijks.hardcorerevival;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.jorislodewijks.hardcorerevival.HardcoreRevival.ResurrectionType;

import net.md_5.bungee.api.ChatColor;

public class RevivalHandler implements Listener {
	public final HardcoreRevival plugin;

	public RevivalHandler(HardcoreRevival plugin) {
		this.plugin = plugin;
	}

	public boolean revivePlayer(Player player, Location revivalLocation, ResurrectionType ressurectionType) {
		if (player != null) {
			player.setGameMode(GameMode.SURVIVAL);
			player.teleport(revivalLocation);
			player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			player.setFoodLevel(20);
			
			switch(ressurectionType) {
			case CULT:
				player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 168000, 1, true));
				player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 200, 5));
				player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 5, 1));
				
				player.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 500);
				player.getWorld().spawnParticle(Particle.DRAGON_BREATH, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 200);
				
				player.getWorld().playSound(revivalLocation, Sound.AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRA_RARE, 1.0f, 1.0f);
				player.getWorld().playSound(revivalLocation, Sound.ENTITY_ZOMBIE_INFECT, 1.5f, 0.7f);
				
				player.sendMessage(ChatColor.MAGIC + "What is this?");
				player.sendMessage(ChatColor.RED + "You have been resurrected in a new body!");
				player.sendMessage(ChatColor.RED + "You don't feel so good.");
				break;
			case RELIGIOUS:
				player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 168000, 1, true));
				player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 1));
				
				player.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 500);
				player.getWorld().spawnParticle(Particle.SPELL_INSTANT, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 1000);
				
				player.getWorld().playSound(revivalLocation, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
				
				player.sendMessage(ChatColor.MAGIC + "What is this?");
				player.sendMessage(ChatColor.GREEN + "You have been blessed by the gods!");
				player.sendMessage(ChatColor.GREEN + "You feel greater than ever.");
				break;
			}

			
			return true;
		}

		return false;
	}

	public boolean reviveNearestDeadPlayer(Location searchLocation, Location revivalLocation, ResurrectionType resurrectionType) {
		// Search for dead player nearest to searchLocation.
		List<Player> players = Bukkit.getWorlds().get(0).getPlayers();
		Player closestDeadPlayer = null;
		double closestDistanceSqr = Double.MAX_VALUE;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getGameMode() == GameMode.SPECTATOR) {
				if (searchLocation.distanceSquared(players.get(i).getLocation()) < closestDistanceSqr) {
					closestDistanceSqr = searchLocation.distanceSquared(players.get(i).getLocation());
					closestDeadPlayer = players.get(i);
				}
			}
		}

		return revivePlayer(closestDeadPlayer, revivalLocation, resurrectionType);
	}

}