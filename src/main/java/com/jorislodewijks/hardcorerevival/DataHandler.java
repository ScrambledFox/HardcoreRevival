package com.jorislodewijks.hardcorerevival;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class DataHandler {
	HashMap<String, Integer> data;

	@SuppressWarnings("unchecked")
	public DataHandler() {
		File dir = HardcoreRevival.instance.getDataFolder();

		if (!dir.exists())
			if (!dir.mkdir())
				System.out.println("Could not create directory for plugin: " + HardcoreRevival.instance.getDescription().getName());

		data = (HashMap<String, Integer>) load(new File(HardcoreRevival.instance.getDataFolder(), "data.dat"));

		if (data == null) {
			data = new HashMap<String, Integer>();
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

	public void setData(Player player, int karma) {
		this.data.put(player.getUniqueId().toString(), karma);
	}

	public PlayerSaveData getData(Player player) {
		int karma = this.data.get(player.getUniqueId().toString());
		return new PlayerSaveData(player.getUniqueId().toString(), karma);
	}

}