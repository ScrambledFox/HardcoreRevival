package com.jorislodewijks.hardcorerevival;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import com.jorislodewijks.hardcorerevival.altar.Altar;
import com.jorislodewijks.hardcorerevival.altar.AltarSaveData;

public class SaveData implements Serializable {

	private static final long serialVersionUID = 2024619285683593275L;
	private HashMap<String, PlayerSaveData> playerData;
	private List<AltarSaveData> altarData;

	public SaveData() {
		playerData = new HashMap<String, PlayerSaveData>();
		altarData = new ArrayList<AltarSaveData>();
	}

	public SaveData(HashMap<String, PlayerSaveData> playerData, List<AltarSaveData> altarData) {
		super();
		this.playerData = playerData;
		this.altarData = altarData;
	}

	public void setSpecificPlayerData(Player player, int karma) {
		if (playerData == null)
			playerData = new HashMap<String, PlayerSaveData>();
		playerData.put(player.getUniqueId().toString(), new PlayerSaveData(player.getUniqueId().toString(), karma));
	}

	public PlayerSaveData getSpecificPlayerData(Player player) {
		if (playerData != null)
			return playerData.get(player.getUniqueId().toString());
		else
			return null;
	}

	public HashMap<String, PlayerSaveData> getPlayerData() {
		return playerData;
	}

	public void setAltarData(Altar altar) {
		if (altarData == null)
			altarData = new ArrayList<AltarSaveData>();

		this.altarData.add(new AltarSaveData(altar.getCreatorUUID().toString(),
				altar.getImportantBlock().getLocation().getBlockX(),
				altar.getImportantBlock().getLocation().getBlockY(),
				altar.getImportantBlock().getLocation().getBlockZ()));
	}

	public List<AltarSaveData> getAltarData() {
		return altarData;
	}

}