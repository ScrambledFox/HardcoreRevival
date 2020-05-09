package com.jorislodewijks.hardcorerevival;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.bukkit.entity.Player;

import com.jorislodewijks.hardcorerevival.altar.Altar;
import com.jorislodewijks.hardcorerevival.altar.AltarSaveData;

public class DataHandler {
	SaveData data;

	@SuppressWarnings("unchecked")
	public DataHandler() {
		File dir = HardcoreRevival.instance.getDataFolder();

		if (!dir.exists())
			if (!dir.mkdir())
				System.out.println("Could not create directory for plugin: "
						+ HardcoreRevival.instance.getDescription().getName());

		data = (SaveData) load(new File(HardcoreRevival.instance.getDataFolder(), "data.dat"));

		if (data == null) {
			data = new SaveData();
		}
	}

	public void saveData() {
		save(data, new File(HardcoreRevival.instance.getDataFolder(), "data.dat"));
	}

	private void save(Object o, File f) {
		try {
			if (!f.exists())
				f.createNewFile();

			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(o);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Object load(File f) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
			Object result = ois.readObject();
			ois.close();
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	public void setPlayerData(Player player, int karma) {
		this.data.setSpecificPlayerData(player, karma);
	}

	public PlayerSaveData getPlayerData(Player player) {
		return this.data.getSpecificPlayerData(player);
	}
	
	public void setAltarData(Altar altar) {
		this.data.setAltarData(altar);
	}

	public List<AltarSaveData> getAltarData() {
		return this.data.getAltarData();
	}

}