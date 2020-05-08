package com.jorislodewijks.hardcorerevival.books;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;

import com.jorislodewijks.hardcorerevival.HardcoreRevival;
import com.jorislodewijks.hardcorerevival.HardcoreRevival.ResurrectionType;
import com.jorislodewijks.hardcorerevival.ritual.Ritual;
import com.jorislodewijks.hardcorerevival.ritual.RitualHandler;

public class CultBookHandler implements IBookHandler {

	public CultBookHandler() {

	}

	public ItemStack getInstructionBook() {
		ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();

		List<Ritual> rituals = RitualHandler.getRituals(ResurrectionType.CULT);

		String text = "\n\n\n" + "�4�l     Dark Rituals�0\n" + "           and\n" + "�4�l  Peculier Potions�0\n" + "\n"
				+ "\n" + "\n" + "\n" + " A instructional set of         pages by\n" + "�5�o   Salazar the Foul�0�o.�0";

		text += getContentPage(rituals);
		text += getAltarPage();

		for (Ritual ritual : rituals) {
			text += getRitualPage(ritual);
		}

		bookMeta.setGeneration(Generation.TATTERED);
		bookMeta.setTitle("Dark Rituals � Peculier Potions");
		bookMeta.setAuthor("Salazar the Foul");

		bookMeta.setPages(text.split("/p"));
		itemStack.setItemMeta(bookMeta);
		return itemStack;
	}

	private String getContentPage(List<Ritual> rituals) {
		String text = "/p";
		text += "  Table of Contents\n" + "*-----------------*\n" + "\n"
				+ "1. How to build an altar worthy of the devil.\n" + "\n"
				+ "2. Rituals and Actions: praising the lord.\n�0";

		for (int i = 0; i < rituals.size(); i++) {
			text += ("2." + (i + 1) + " " + rituals.get(i).getName() + "\n");

			if (i == 1)
				text += "/p";
		}

		return text;
	}

	private String getAltarPage() {
		String text = "/p";
		text += "Altar\n\nA dark altar is built, by heating a cauldron with a fire. "
				+ "The cauldron will have to be filled with water to work. You know the cauldron works, "
				+ "if purple power is emitting from the cauldron.";
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
