package com.jorislodewijks.hardcorerevival;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import net.md_5.bungee.api.ChatColor;

public class KarmaHandler implements Listener {
	private final HardcoreRevival plugin;

	private static List<KarmaEvent> karmaEvents = new ArrayList<>(Arrays.asList(
			new KarmaEvent(-100, "You are a bad human, buy you kinda like it...", -1), new KarmaEvent(100, "You are a good human.", -1)));

	public KarmaHandler(HardcoreRevival plugin) {
		this.plugin = plugin;
	}

	public void setPlayerKarma(Player player, int karma) {
		player.setMetadata("Karma", new FixedMetadataValue(plugin, karma));
		plugin.dataHandler.setData(player, karma);
	}

	public int getPlayerKarma(Player player) {
		if (player.hasMetadata("Karma")) {

			for (MetadataValue val : player.getMetadata("Karma")) {
				if (val.getOwningPlugin() == plugin)
					return val.asInt();
			}

		}

		return 0;
	}

	public int modPlayerKarma(Player player, int karma) {
		int oldKarma = getPlayerKarma(player);
		setPlayerKarma(player, oldKarma + karma);
		handleKarmaChange(player, oldKarma, karma);
		return getPlayerKarma(player);
	}

	@EventHandler
	public void PlayerJoinEvent(PlayerJoinEvent event) {
		setPlayerKarma(event.getPlayer(), plugin.dataHandler.getData(event.getPlayer()).karma);
	}

	@EventHandler
	public void EntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			if (event.getEntity() instanceof Monster) {
				modPlayerKarma((Player) event.getDamager(), 1);
			} else if (event.getEntity() instanceof Animals) {
				modPlayerKarma((Player) event.getDamager(), -1);
			} else if (event.getEntity() instanceof NPC) {
				modPlayerKarma((Player) event.getDamager(), -10);
			}
		}
	}

	private void handleKarmaChange(Player player, int oldKarma, int karmaChange) {
		if (Math.signum(karmaChange) > 0.0) {
			for(KarmaEvent karmaEvent : karmaEvents) {
				if(oldKarma < karmaEvent.threshold && oldKarma + karmaChange >= karmaEvent.threshold) {
					player.sendMessage(ChatColor.GREEN + karmaEvent.message);
				} else if (oldKarma > karmaEvent.threshold && oldKarma - karmaChange <= karmaEvent.threshold) {
					player.sendMessage(ChatColor.RED + karmaEvent.message);
				}
			}
		} else {

		}
	}

}