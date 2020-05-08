package com.jorislodewijks.hardcorerevival;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jorislodewijks.hardcorerevival.altar.AltarHandler;
import com.jorislodewijks.hardcorerevival.karma.KarmaHandler;

public final class HardcoreRevival extends JavaPlugin {
	public static HardcoreRevival instance = null;
	
	public DataHandler dataHandler;

	public static enum ResurrectionType {
		CULT, RELIGIOUS
	}

	@Override
	public void onEnable() {
		HardcoreRevival.instance = this;
		
		getServer().getPluginManager().registerEvents(new KarmaHandler(), this);
		getServer().getPluginManager().registerEvents(new AltarHandler(), this);

		getLogger().info("Registering commands...");
		this.getCommand("setkarma").setExecutor(new MyCommandExecutor());
		this.getCommand("getkarma").setExecutor(new MyCommandExecutor());
		this.getCommand("revive").setExecutor(new MyCommandExecutor());
		this.getCommand("revivenearest").setExecutor(new MyCommandExecutor());

		getLogger().info("Getting Data...");
		dataHandler = new DataHandler();
		List<Player> players = Bukkit.getServer().getWorlds().get(0).getPlayers();
		for (Player player : players) {
			PlayerSaveData data = dataHandler.getData(player);
			new KarmaHandler().setPlayerKarma(player, data.karma);
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