package com.jorislodewijks.hardcorerevival;

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
import com.jorislodewijks.hardcorerevival.karma.KarmaHandler;
import com.jorislodewijks.hardcorerevival.players.PlayerHandler;

import net.md_5.bungee.api.ChatColor;

public class RevivalHandler implements Listener {

	public RevivalHandler() {
	}

	public static boolean revivePlayer(Player player, Location revivalLocation, ResurrectionType resurrectionType) {
		if (player != null) {
			player.setGameMode(GameMode.SURVIVAL);
			player.teleport(revivalLocation);
			player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			player.setFoodLevel(20);

			switch (resurrectionType) {
			case CULT:
				player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 20 * 60 * 20 * 7, 1, true));
				player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 200, 5));
				player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 10, 10));

				player.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, player.getLocation().getX(),
						player.getLocation().getY(), player.getLocation().getZ(), 500);
				player.getWorld().spawnParticle(Particle.DRAGON_BREATH, player.getLocation().getX(),
						player.getLocation().getY(), player.getLocation().getZ(), 200);

				player.getWorld().playSound(revivalLocation, Sound.AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRA_RARE, 1.0f,
						1.0f);
				player.getWorld().playSound(revivalLocation, Sound.ENTITY_ZOMBIE_INFECT, 1.5f, 0.7f);

				player.sendMessage(ChatColor.MAGIC + "What is this?");
				player.sendMessage(ChatColor.RED + "You have been resurrected in a new body!");
				player.sendMessage(ChatColor.RED + "You don't feel so good.");

				new KarmaHandler().setPlayerKarma(player, -500);
				break;
			case RELIGIOUS:
				player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 20 * 60 * 20 * 7, 1, true));
				player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 1));

				player.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, player.getLocation().getX(),
						player.getLocation().getY(), player.getLocation().getZ(), 500);
				player.getWorld().spawnParticle(Particle.SPELL_INSTANT, player.getLocation().getX(),
						player.getLocation().getY(), player.getLocation().getZ(), 1000);

				player.getWorld().playSound(revivalLocation, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);

				player.sendMessage(ChatColor.MAGIC + "What is this?");
				player.sendMessage(ChatColor.GREEN + "You have been blessed by the gods!");
				player.sendMessage(ChatColor.GREEN + "You feel greater than ever.");
				new KarmaHandler().setPlayerKarma(player, 500);
				break;
			}

			return true;
		}

		return false;
	}

	public static boolean reviveNearestDeadPlayer(Location searchLocation, Location revivalLocation,
			ResurrectionType resurrectionType) {
		// Search for dead player nearest to searchLocation.
		return revivePlayer(PlayerHandler.getNearestPlayer(searchLocation.getWorld().getPlayers(), GameMode.SPECTATOR,
				searchLocation), revivalLocation, resurrectionType);
	}

}