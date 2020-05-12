package com.jorislodewijks.hardcorerevival.ritual.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.jorislodewijks.hardcorerevival.HardcoreRevival.ResurrectionType;
import com.jorislodewijks.hardcorerevival.ritual.Ritual;
import com.jorislodewijks.hardcorerevival.ritual.RitualHandler;

public class SourceHandler {

	private static SourceBookGenerator generator;

	private static HashMap<Integer, SourceBook> _ritualSourcePages;
	private static HashMap<String, SourceBook> _ritualSourceBooks;

	public SourceHandler() {
		generator = new SourceBookGenerator();

		// get from data
		_ritualSourcePages = new HashMap<Integer, SourceBook>();
		_ritualSourceBooks = new HashMap<String, SourceBook>();
	}

	public static ItemStack getRitualPageItem(Ritual ritual) {
		int index = RitualHandler.getRituals().indexOf(ritual);
		if (_ritualSourcePages.containsKey(index)) {
			return _ritualSourcePages.get(index).toItemStack();
		} else {
			SourceBook newRitualPage = generator.getSourceBookFromRitual(ritual);
			_ritualSourcePages.put(index, newRitualPage);
			return newRitualPage.toItemStack();
		}
	}

	public static ItemStack getSourceBookItem(ResurrectionType type, String ritualSource) {
		if (_ritualSourceBooks.containsKey(ritualSource)) {
			return _ritualSourceBooks.get(ritualSource).toItemStack();
		} else {
			List<Ritual> ritualList = RitualHandler.getRitualsWithSource(ritualSource);
			SourceBook newRitualBook = generator.getSourceBookFromRituals(type, ritualList);
			_ritualSourceBooks.put(ritualSource, newRitualBook);
			return newRitualBook.toItemStack();
		}
	}
	
	public static boolean hasSourceFiles() {
		return (!_ritualSourcePages.isEmpty() || !_ritualSourceBooks.isEmpty());
	}

	public static List<SourceBook> getAllSourceBooks() {
		List<SourceBook> sourceBooks = new ArrayList<SourceBook>();
		_ritualSourcePages.forEach((k, v) -> sourceBooks.add(v));
		_ritualSourceBooks.forEach((k, v) -> sourceBooks.add(v));
		return sourceBooks;
	}

}