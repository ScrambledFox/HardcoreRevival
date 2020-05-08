package com.jorislodewijks.hardcorerevival.books;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;

import com.jorislodewijks.hardcorerevival.HardcoreRevival;
import com.jorislodewijks.hardcorerevival.HardcoreRevival.ResurrectionType;
import com.jorislodewijks.hardcorerevival.ritual.Ritual;
import com.jorislodewijks.hardcorerevival.ritual.RitualHandler;

public class ReligiousBookHandler implements IBookHandler {
	
	public ItemStack getInstructionBook() {
		ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();

		List<Ritual> rituals = RitualHandler.getRituals(ResurrectionType.RELIGIOUS);

		String text = "\n\n\n" + "§4§l     TO BE NAMED§0\n";

		text += getContentPage(rituals);
		text += getAltarPage();

		for (Ritual ritual : rituals) {
			text += getRitualPage(ritual);
		}

		bookMeta.setGeneration(Generation.TATTERED);
		bookMeta.setTitle("TO BE NAMED");
		bookMeta.setAuthor("UNKNOWN");

		bookMeta.setPages(text.split("/p"));
		itemStack.setItemMeta(bookMeta);
		return itemStack;
	}

	private String getContentPage(List<Ritual> rituals) {
		String text = "/p";
		text += "  Table of Contents\n" + "*-----------------*\n" + "\n"
				+ "1. How to build an altar worthy of the Lord.\n" + "\n"
				+ "2. Rituals and Actions: praising the lord.\n§0";

		for (int i = 0; i < rituals.size(); i++) {
			text += ("2." + (i + 1) + " " + rituals.get(i).getName() + "\n");

			if (i == 1)
				text += "/p";
		}

		return text;
	}

	private String getAltarPage() {
		String text = "/p";
		text += "Altar\n\nA godly altar is built, by circling a block of emerald with white stained glass. "
				+ "Next to this glass, an enchantment table must be placed. You know it worked, if you hear the godly sounds."; 
		return text;
	}

	private String getRitualPage(Ritual ritual) {
		String text = "/p";
		text += ritual.getName() + "\n----------------\n" + "Ingredients:\n";
		for(ItemStack itemStack : ritual.getIngredients()) {
			text += " - " + itemStack.getAmount() + "x " + HardcoreRevival.capitalize(itemStack.getType().name(), "_")  + "\n"; 
		}
		return text;
	}

}
