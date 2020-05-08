package com.jorislodewijks.hardcorerevival.Players;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class PlayerHandler {

	public void giveEffectToNearestPlayer(Location searchLocation, PotionEffect effect) {
		List<Player> players = Bukkit.getWorlds().get(0).getPlayers();
		Player closestSurivalPlayer = null;
		double closestDistanceSqr = Double.MAX_VALUE;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getGameMode() == GameMode.SURVIVAL) {
				if (searchLocation.distanceSquared(players.get(i).getLocation()) < closestDistanceSqr) {
					closestDistanceSqr = searchLocation.distanceSquared(players.get(i).getLocation());
					closestSurivalPlayer = players.get(i);
				}
			}
		}

		closestSurivalPlayer.addPotionEffect(effect);
	}
	
}