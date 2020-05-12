package com.jorislodewijks.hardcorerevival;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jorislodewijks.hardcorerevival.altar.AltarHandler;
import com.jorislodewijks.hardcorerevival.karma.KarmaHandler;
import com.jorislodewijks.hardcorerevival.players.PlayerHandler;
import com.jorislodewijks.hardcorerevival.ritual.RitualHandler;
import com.jorislodewijks.hardcorerevival.ritual.source.SourceHandler;

public final class HardcoreRevival extends JavaPlugin {
	public static HardcoreRevival instance = null;

	public DataHandler dataHandler;
	public SourceHandler sourceHandler;

	public static enum ResurrectionType {
		ANY, CULT, RELIGIOUS
	}

	@Override
	public void onEnable() {
		HardcoreRevival.instance = this;
		dataHandler = new DataHandler();
		sourceHandler = new SourceHandler();

		this.registerEventHandlers();
		this.registerCommands();
		this.registerRituals();
		this.getPluginSaveData();
		AltarHandler.registerOldAltars(dataHandler.getAltarData());

		getLogger().info("HardcoreRevival has been enabled!");
	}

	@Override
	public void onDisable() {
		getLogger().info("Saving Data...");
		dataHandler.saveData();

		getLogger().info("HardcoreRevival has been disabled!");
	}

	private void registerEventHandlers() {
		getLogger().info("Registering Event Handlers...");
		getServer().getPluginManager().registerEvents(new KarmaHandler(), this);
		getServer().getPluginManager().registerEvents(new AltarHandler(), this);
		getServer().getPluginManager().registerEvents(new RitualHandler(), this);
		getServer().getPluginManager().registerEvents(new PlayerHandler(), this);
	}

	private void registerCommands() {
		getLogger().info("Registering commands...");
		this.getCommand("setkarma").setExecutor(new MyCommandExecutor());
		this.getCommand("getkarma").setExecutor(new MyCommandExecutor());
		this.getCommand("revive").setExecutor(new MyCommandExecutor());
		this.getCommand("revivenearest").setExecutor(new MyCommandExecutor());
		this.getCommand("listaltars").setExecutor(new MyCommandExecutor());
		this.getCommand("listrituals").setExecutor(new MyCommandExecutor());
		this.getCommand("listsourcebooks").setExecutor(new MyCommandExecutor());
		this.getCommand("givesourcebook").setExecutor(new MyCommandExecutor());
		this.getCommand("gensourcebook").setExecutor(new MyCommandExecutor());
	}

	private void registerRituals() {
		getLogger().info("Getting rituals from config...");
		// RitualHandler.setRituals(dataHandler.getRituals());
	}

	private void getPluginSaveData() {
		getLogger().info("Getting Data...");
		List<Player> players = Bukkit.getServer().getWorlds().get(0).getPlayers();
		for (Player player : players) {
			PlayerSaveData data = dataHandler.getPlayerData(player);
			if (data != null)
				new KarmaHandler().setPlayerKarma(player, data.karma);
		}
	}

}