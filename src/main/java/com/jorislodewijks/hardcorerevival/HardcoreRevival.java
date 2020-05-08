package com.jorislodewijks.hardcorerevival;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class HardcoreRevival extends JavaPlugin {
	public DataHandler dataHandler;

	public static enum ResurrectionType {
		CULT, RELIGIOUS
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new KarmaHandler(this), this);
		getServer().getPluginManager().registerEvents(new AltarHandler(this), this);

		getLogger().info("Registering commands...");
		this.getCommand("setkarma").setExecutor(new MyCommandExecutor(this));
		this.getCommand("getkarma").setExecutor(new MyCommandExecutor(this));
		this.getCommand("revive").setExecutor(new MyCommandExecutor(this));
		this.getCommand("revivenearest").setExecutor(new MyCommandExecutor(this));

		getLogger().info("Getting Data...");
		dataHandler = new DataHandler(this);
		List<Player> players = Bukkit.getServer().getWorlds().get(0).getPlayers();
		for (Player player : players) {
			PlayerSaveData data = dataHandler.getData(player);
			new KarmaHandler(this).setPlayerKarma(player, data.karma);
		}

		getLogger().info("HardcoreRevival has been enabled!");
	}

	@Override
	public void onDisable() {
		getLogger().info("Saving Data...");
		dataHandler.saveData();

		getLogger().info("HardcoreRevival has been disabled!");
	}

}