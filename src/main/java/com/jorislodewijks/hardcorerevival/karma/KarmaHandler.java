package com.jorislodewijks.hardcorerevival.karma;

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

import com.jorislodewijks.hardcorerevival.HardcoreRevival;
import com.jorislodewijks.hardcorerevival.PlayerSaveData;

public class KarmaHandler implements Listener {

//	private static List<KarmaEvent> karmaEvents = new ArrayList<>(Arrays.asList(
//			new KarmaEvent(-100, "You are a bad human, buy you kinda like it...", -1), new KarmaEvent(100, "You are a good human.", -1)));

	public KarmaHandler() {
	}

	public void setPlayerKarma(Player player, int karma) {
		player.setMetadata("Karma", new FixedMetadataValue(HardcoreRevival.instance, karma));
		HardcoreRevival.instance.dataHandler.setPlayerData(player, karma);
	}

	public int getPlayerKarma(Player player) {
		if (player.hasMetadata("Karma")) {

			for (MetadataValue val : player.getMetadata("Karma")) {
				if (val.getOwningPlugin() == HardcoreRevival.instance)
					return val.asInt();
			}

		}

		return 0;
	}

	public int modPlayerKarma(Player player, int karma) {
		int oldKarma = getPlayerKarma(player);
		setPlayerKarma(player, oldKarma + karma);
		//handleKarmaChange(player, oldKarma, karma);
		return getPlayerKarma(player);
	}

	@EventHandler
	public void OnPlayerJoinEvent(PlayerJoinEvent event) {
		PlayerSaveData playerData = HardcoreRevival.instance.dataHandler.getPlayerData(event.getPlayer());
		if (playerData != null)
			setPlayerKarma(event.getPlayer(), playerData.karma);
		else
			setPlayerKarma(event.getPlayer(), 0);
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

//	private void handleKarmaChange(Player player, int oldKarma, int karmaChange) {
//		if (Math.signum(karmaChange) > 0.0) {
//			for(KarmaEvent karmaEvent : karmaEvents) {
//				if(oldKarma < karmaEvent.threshold && oldKarma + karmaChange >= karmaEvent.threshold) {
//					player.sendMessage(ChatColor.GREEN + karmaEvent.message);
//				} else if (oldKarma > karmaEvent.threshold && oldKarma - karmaChange <= karmaEvent.threshold) {
//					player.sendMessage(ChatColor.RED + karmaEvent.message);
//				}
//			}
//		} else {
//
//		}
//	}

}