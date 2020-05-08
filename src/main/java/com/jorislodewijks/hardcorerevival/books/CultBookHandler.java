package com.jorislodewijks.hardcorerevival.books;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;

public class CultBookHandler implements IBookHandler {

	public CultBookHandler() {

	}

	public ItemStack getInstructionBook() {
		ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();

		List<String> pages = new ArrayList<String>(Arrays.asList("Dark Rituals and Peculier Potions",
				"A instructional set of pages", "by Salazar the Foul"));

		bookMeta.setGeneration(Generation.TATTERED);
		bookMeta.setTitle("Dark Rituals à Peculier Potions");
		bookMeta.setAuthor("Salazar the Foul");

		bookMeta.setPages(pages);

		itemStack.setItemMeta(bookMeta);
		return itemStack;
	}

}
