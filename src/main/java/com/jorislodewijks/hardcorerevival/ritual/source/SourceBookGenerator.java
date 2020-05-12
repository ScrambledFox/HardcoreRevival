package com.jorislodewijks.hardcorerevival.ritual.source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;

import com.jorislodewijks.hardcorerevival.HardcoreRevival.ResurrectionType;
import com.jorislodewijks.hardcorerevival.ritual.Ritual;

public class SourceBookGenerator {

	private static SourceBookData sourceBookData;

	public SourceBookGenerator() {
		sourceBookData = new SourceBookData();
	}

	public SourceBook getSourceBookFromRituals(ResurrectionType type, List<Ritual> rituals) {
		SourceBook newSourceBook = new SourceBook();

		// Generate book front
		newSourceBook.setTitle(SourceBookGenerator.sourceBookData.getRandomBookTitle(type));
		newSourceBook.setAuthor(ChatColor.RESET + SourceBookGenerator.sourceBookData.getRandomFirstName(type) + " "
				+ SourceBookGenerator.sourceBookData.getRandomLastName(type));
		newSourceBook.setLore(SourceBookGenerator.sourceBookData.getRandomDescription(type));

		// Generate content
		List<String> testPages = new ArrayList<String>();
		testPages.add(generateTitlePage(newSourceBook.getTitle(), newSourceBook.getAuthor(), type));
		testPages.add(newSourceBook.getTitle() + "\n\n" + ChatColor.RESET + newSourceBook.getAuthor());
		rituals.forEach(r -> testPages.add(r.getName()));
		newSourceBook.setPages(testPages);

		return newSourceBook;
	}

	public SourceBook getSourceBookFromRitual(Ritual ritual) {
		return new SourceBook(null, null, null, null, null);
	}

	private String generateTitlePage(String title, String author, ResurrectionType bookType) {
		String[] titleLines = (WordUtils.wrap(title, 15) + ChatColor.BLACK + ChatColor.ITALIC + "\n\nWritten by\n"
				+ ChatColor.RESET + WordUtils.wrap(author, 15)).split("\n");

		for (int i = 0; i < titleLines.length; i++) {
			titleLines[i] = titleLines[i].trim();
			titleLines[i] = StringUtils.center(titleLines[i], 25).replaceFirst("\\s++$", "");
		}

		String page = "\n\n";
		for (String str : titleLines) {
			page += str + "\n";
		}

		return page;
	}

	public SourceBookData getSourceBookDataBank() {
		return SourceBookGenerator.sourceBookData;
	}

	// Source Book Data class, contains all random information a source book can
	// contain. This is configurable.
	public class SourceBookData {

		private List<String> cultBookTitles;
		private List<String> cultFirstNames;
		private List<String> cultLastNames;
		private List<String> cultDescriptionLinesPrimary;
		private List<String> cultDescriptionLinesSecondary;
		private List<String> cultDescriptionLinesTertiary;
		private List<ChatColor> cultColours;

		private List<String> religiousBookTitles;
		private List<String> religiousFirstNames;
		private List<String> religiousLastNames;
		private List<String> religiousDescriptionLinesPrimary;
		private List<String> religiousDescriptionLinesSecondary;
		private List<String> religiousDescriptionLinesTertiary;
		private List<ChatColor> religiousColours;

		public SourceBookData() {
			cultBookTitles = new ArrayList<String>();
			cultFirstNames = new ArrayList<String>();
			cultLastNames = new ArrayList<String>();
			cultDescriptionLinesPrimary = new ArrayList<String>();
			cultDescriptionLinesSecondary = new ArrayList<String>();
			cultDescriptionLinesTertiary = new ArrayList<String>();
			cultColours = new ArrayList<ChatColor>();

			religiousBookTitles = new ArrayList<String>();
			religiousFirstNames = new ArrayList<String>();
			religiousLastNames = new ArrayList<String>();
			religiousDescriptionLinesPrimary = new ArrayList<String>();
			religiousDescriptionLinesSecondary = new ArrayList<String>();
			religiousDescriptionLinesTertiary = new ArrayList<String>();
			religiousColours = new ArrayList<ChatColor>();

			cultBookTitles.add("A paper on Frogs and Toads");
			cultBookTitles.add("Witches and Wizards");
			cultBookTitles.add("Handy tricks (Gone Wrong)");
			cultBookTitles.add("3 Spells You Need to Know!");

			cultFirstNames.add("Salazar");
			cultFirstNames.add("Serap");
			cultFirstNames.add("Ruud");
			cultFirstNames.add("Saracruncher");

			cultLastNames.add("the Foul");
			cultLastNames.add("the Dirty");
			cultLastNames.add("the Red");
			cultLastNames.add("the Dark");

			cultDescriptionLinesPrimary.add("A summary of spells");
			cultDescriptionLinesPrimary.add("The best things to know");
			cultDescriptionLinesPrimary.add("A list of rituals");
			cultDescriptionLinesPrimary.add("Some important keystones");

			cultDescriptionLinesSecondary.add("that every wizard");
			cultDescriptionLinesSecondary.add("that all dark magicians");
			cultDescriptionLinesSecondary.add("for witches and black cats");
			cultDescriptionLinesSecondary.add("that every wizard");

			cultDescriptionLinesTertiary.add("needs to know.");
			cultDescriptionLinesTertiary.add("has to learn.");
			cultDescriptionLinesTertiary.add("must master to be evil.");
			cultDescriptionLinesTertiary.add("must conquer to rule the world.");

			cultColours.add(ChatColor.MAGIC);
			cultColours.add(ChatColor.BLACK);
			cultColours.add(ChatColor.DARK_PURPLE);
			cultColours.add(ChatColor.LIGHT_PURPLE);
			cultColours.add(ChatColor.DARK_RED);
			cultColours.add(ChatColor.RED);
			cultColours.add(ChatColor.DARK_BLUE);
			cultColours.add(ChatColor.BLUE);

			religiousBookTitles.add("God and you, a beginners guide");
			religiousBookTitles.add("How Angels defeat Demons");
			religiousBookTitles.add("The means of Prayer");
			religiousBookTitles.add("Burying Bones and Purging Evil");

			religiousFirstNames.add("Eprix");
			religiousFirstNames.add("Krahith");
			religiousFirstNames.add("Anewix");
			religiousFirstNames.add("Vetior");

			religiousLastNames.add("the Great");
			religiousLastNames.add("the Brave");
			religiousLastNames.add("the Wise");
			religiousLastNames.add("the Gray");
			religiousLastNames.add("the White");
			religiousLastNames.add("the Bright");

			religiousDescriptionLinesPrimary.add("A summary of prayers");
			religiousDescriptionLinesPrimary.add("A list of good");
			religiousDescriptionLinesPrimary.add("The best items");
			religiousDescriptionLinesPrimary.add("God gave us these rituals");

			religiousDescriptionLinesSecondary.add("to think about");
			religiousDescriptionLinesSecondary.add("to learn and pray with");
			religiousDescriptionLinesSecondary.add("that help us all");
			religiousDescriptionLinesSecondary.add("where evil may be vanquished with");

			religiousDescriptionLinesTertiary.add("when purging the enemy.");
			religiousDescriptionLinesTertiary.add("when dealing with evil.");
			religiousDescriptionLinesTertiary.add("created by our God.");
			religiousDescriptionLinesTertiary.add(", compiled by our Holy Experts.");

			religiousColours.add(ChatColor.MAGIC);
			religiousColours.add(ChatColor.GOLD);
			religiousColours.add(ChatColor.WHITE);
			religiousColours.add(ChatColor.YELLOW);
			religiousColours.add(ChatColor.GREEN);
			religiousColours.add(ChatColor.GRAY);
			// Get data from config
		}

		public String getRandomBookTitle(ResurrectionType type) {
			switch (type) {
			case ANY:
				List<String> bookTitles = new ArrayList<String>();
				bookTitles.addAll(cultBookTitles);
				bookTitles.addAll(religiousBookTitles);
				List<ChatColor> colours = new ArrayList<ChatColor>();
				return colours.get(ThreadLocalRandom.current().nextInt(0, colours.size()))
						+ bookTitles.get(ThreadLocalRandom.current().nextInt(0, bookTitles.size()));
			case CULT:
				return cultColours.get(ThreadLocalRandom.current().nextInt(0, cultColours.size()))
						+ cultBookTitles.get(ThreadLocalRandom.current().nextInt(0, cultBookTitles.size()));
			case RELIGIOUS:
				return religiousColours.get(ThreadLocalRandom.current().nextInt(0, religiousColours.size()))
						+ religiousBookTitles.get(ThreadLocalRandom.current().nextInt(0, religiousBookTitles.size()));
			default:
				return "UNKNOWN";
			}
		}

		public String getRandomFirstName(ResurrectionType type) {
			switch (type) {
			case ANY:
				List<String> firstNames = new ArrayList<String>();
				firstNames.addAll(cultFirstNames);
				firstNames.addAll(religiousFirstNames);
				return firstNames.get(ThreadLocalRandom.current().nextInt(0, firstNames.size()));
			case CULT:
				return cultFirstNames.get(ThreadLocalRandom.current().nextInt(0, cultFirstNames.size()));
			case RELIGIOUS:
				return religiousFirstNames.get(ThreadLocalRandom.current().nextInt(0, religiousFirstNames.size()));
			default:
				return "UNKNOWN";
			}
		}

		public String getRandomLastName(ResurrectionType type) {
			switch (type) {
			case ANY:
				List<String> lastNames = new ArrayList<String>();
				lastNames.addAll(cultLastNames);
				lastNames.addAll(religiousLastNames);
				return lastNames.get(ThreadLocalRandom.current().nextInt(0, lastNames.size()));
			case CULT:
				return cultLastNames.get(ThreadLocalRandom.current().nextInt(0, cultLastNames.size()));
			case RELIGIOUS:
				return religiousLastNames.get(ThreadLocalRandom.current().nextInt(0, religiousLastNames.size()));
			default:
				return "UNKNOWN";
			}
		}

		public List<String> getRandomDescription(ResurrectionType type) {
			switch (type) {
			case ANY:
				List<String> primaries = new ArrayList<String>();
				List<String> secondaries = new ArrayList<String>();
				List<String> tertiaries = new ArrayList<String>();
				primaries.addAll(cultDescriptionLinesPrimary);
				primaries.addAll(religiousDescriptionLinesPrimary);
				secondaries.addAll(cultDescriptionLinesSecondary);
				secondaries.addAll(religiousDescriptionLinesSecondary);
				tertiaries.addAll(cultDescriptionLinesTertiary);
				tertiaries.addAll(religiousDescriptionLinesTertiary);

				List<String> description = new ArrayList<String>();
				description.add(primaries.get(ThreadLocalRandom.current().nextInt(0, primaries.size())));
				description.add(secondaries.get(ThreadLocalRandom.current().nextInt(0, secondaries.size())));
				description.add(tertiaries.get(ThreadLocalRandom.current().nextInt(0, tertiaries.size())));
				return description;
			case CULT:
				List<String> cultDescription = new ArrayList<String>();
				cultDescription.add(cultDescriptionLinesPrimary
						.get(ThreadLocalRandom.current().nextInt(0, cultDescriptionLinesPrimary.size())));
				cultDescription.add(cultDescriptionLinesSecondary
						.get(ThreadLocalRandom.current().nextInt(0, cultDescriptionLinesSecondary.size())));
				cultDescription.add(cultDescriptionLinesTertiary
						.get(ThreadLocalRandom.current().nextInt(0, cultDescriptionLinesTertiary.size())));
				return cultDescription;
			case RELIGIOUS:
				List<String> religiousDescription = new ArrayList<String>();
				religiousDescription.add(religiousDescriptionLinesPrimary
						.get(ThreadLocalRandom.current().nextInt(0, religiousDescriptionLinesPrimary.size())));
				religiousDescription.add(religiousDescriptionLinesSecondary
						.get(ThreadLocalRandom.current().nextInt(0, religiousDescriptionLinesSecondary.size())));
				religiousDescription.add(religiousDescriptionLinesTertiary
						.get(ThreadLocalRandom.current().nextInt(0, religiousDescriptionLinesTertiary.size())));
				return religiousDescription;
			default:
				return new ArrayList<String>(Arrays.asList(ChatColor.MAGIC + "Nothing to see here."));
			}
		}

		public ChatColor getRandomColour(ResurrectionType type) {
			switch (type) {
			case ANY:
				List<ChatColor> colours = new ArrayList<ChatColor>();
				colours.addAll(cultColours);
				colours.addAll(religiousColours);
				return colours.get(ThreadLocalRandom.current().nextInt(0, colours.size()));
			case CULT:
				return cultColours.get(ThreadLocalRandom.current().nextInt(0, cultColours.size()));
			case RELIGIOUS:
				return religiousColours.get(ThreadLocalRandom.current().nextInt(0, religiousColours.size()));
			default:
				return ChatColor.WHITE;
			}
		}

	}

}