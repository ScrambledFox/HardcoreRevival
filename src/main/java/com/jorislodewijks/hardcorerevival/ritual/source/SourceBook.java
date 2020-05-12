package com.jorislodewijks.hardcorerevival.ritual.source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;

import com.jorislodewijks.hardcorerevival.HardcoreRevival.ResurrectionType;

import net.md_5.bungee.api.ChatColor;
import util.PluginUtils;

public class SourceBook {

	private String title;
	private String author;
	private List<String> lore;
	private List<String> pages;

	public SourceBook() {
	}

	public SourceBook(ResurrectionType bookType, String title, String author, List<String> lore, List<String> pages) {
		super();
		this.title = title;
		this.author = author;
		this.lore = lore;
		this.pages = pages;
	}

	public boolean setTitle(String title) {
		if (title.length() <= 32) {
			this.title = title;
			return true;
		} else {
			this.title = ChatColor.MAGIC + "Too Long";
			return false;
		}

	}

	public String getTitle() {
		return this.title;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	public List<String> getLore() {
		return this.lore;
	}

	public boolean setPages(List<String> pages) {
		if (pages.size() <= 100) {
			this.pages = pages;
			return true;
		} else {
			this.pages = new ArrayList<String>(
					Arrays.asList("The book seems to be encoded in some sort of magic language...\n\n" + ChatColor.MAGIC
							+ PluginUtils.getAlphaNumericString(100)));
			return false;
		}
	}

	public ItemStack toItemStack() {
		ItemStack item = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta book = (BookMeta) item.getItemMeta();

		book.setTitle(this.title);
		book.setAuthor(this.author);
		book.setLore(this.lore);
		book.setPages(this.pages);
		
		book.setGeneration(Generation.TATTERED);

		item.setItemMeta(book);
		return item;
	}

}