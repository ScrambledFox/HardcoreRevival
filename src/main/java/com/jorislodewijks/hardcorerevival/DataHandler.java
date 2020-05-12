package com.jorislodewijks.hardcorerevival;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.jorislodewijks.hardcorerevival.altar.Altar;
import com.jorislodewijks.hardcorerevival.altar.AltarSaveData;
import com.jorislodewijks.hardcorerevival.ritual.Ritual;
import com.jorislodewijks.hardcorerevival.ritual.RitualHandler;
import com.jorislodewijks.hardcorerevival.ritual.source.SourceBookGenerator;

public class DataHandler {
	private SaveData data;
	private ConfigDataObject configData;

	private final Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
	private File configFile;
	private FileConfiguration config;

	public DataHandler() {
		this.initDataFolder();
		this.initPluginData();
		this.initConfig();
	}

	private void initDataFolder() {
		File dir = HardcoreRevival.instance.getDataFolder();

		if (!dir.exists())
			if (!dir.mkdir())
				System.out.println("Could not create directory for plugin: "
						+ HardcoreRevival.instance.getDescription().getName());
	}

	private void initPluginData() {
		data = (SaveData) loadBytesObject(new File(HardcoreRevival.instance.getDataFolder(), "data.dat"));

		if (data == null)
			data = new SaveData();
	}

	private void initConfig() {
		configFile = new File(HardcoreRevival.instance.getDataFolder(), "config.json");
		if (!configFile.exists())
			HardcoreRevival.instance.saveResource(configFile.getName(), false);

		configData = this.loadConfig();
	}

	public void saveData() {
		saveBytesObject(data, new File(HardcoreRevival.instance.getDataFolder(), "data.dat"));

		ConfigDataObject cfg = new ConfigDataObject(RitualHandler.getRituals(),
				new SourceBookGenerator().getSourceBookDataBank());
		this.saveConfig(cfg);
	}

	private void saveBytesObject(Object o, File f) {
		try {
			if (!f.exists())
				f.createNewFile(); // Currently gives error on reload, if the config is deleted while it is loaded!

			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(o);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Object loadBytesObject(File f) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
			Object result = ois.readObject();
			ois.close();
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	public void saveConfig(ConfigDataObject cfg) {
		final String json = this.gsonBuilder.toJson(cfg);
		configFile.delete();
		try {
			Files.write(configFile.toPath(), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
		} catch (IOException e) {
			HardcoreRevival.instance.getLogger().severe("Stumbled upon a config saving error...");
			e.printStackTrace();
		}
	}

	public ConfigDataObject loadConfig() {
		try {
			return gsonBuilder.fromJson(new FileReader(configFile), ConfigDataObject.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			HardcoreRevival.instance.getLogger().severe("Stumbled upon a config loading error...");
			e.printStackTrace();
		}

		return null;
	}

	public void setPlayerData(Player player, int karma) {
		this.data.setSpecificPlayerData(player, karma);
	}

	public PlayerSaveData getPlayerData(Player player) {
		PlayerSaveData playerData = this.data.getSpecificPlayerData(player);
		if (playerData != null)
			return this.data.getSpecificPlayerData(player);
		else
			return null;
	}

	public void setAltarData(Altar altar) {
		this.data.setAltarData(altar);
	}

	public List<AltarSaveData> getAltarData() {
		return this.data.getAltarData();
	}

	public void setRituals(List<Ritual> rituals) {
		this.configData.setRituals(rituals);
	}

	public List<Ritual> getRituals() {
		return this.configData.getRituals();
	}

}