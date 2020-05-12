package com.jorislodewijks.hardcorerevival;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.jorislodewijks.hardcorerevival.HardcoreRevival.ResurrectionType;
import com.jorislodewijks.hardcorerevival.altar.Altar;
import com.jorislodewijks.hardcorerevival.altar.AltarHandler;
import com.jorislodewijks.hardcorerevival.karma.KarmaHandler;
import com.jorislodewijks.hardcorerevival.ritual.RitualHandler;
import com.jorislodewijks.hardcorerevival.ritual.source.SourceBook;
import com.jorislodewijks.hardcorerevival.ritual.source.SourceHandler;

import net.md_5.bungee.api.ChatColor;

public class MyCommandExecutor implements CommandExecutor {

	public MyCommandExecutor() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("setkarma")) {
			if (args.length < 2) {
				sender.sendMessage(ChatColor.RED + "You must pass the correct arguments!");
			} else {
				Player player = Bukkit.getPlayer(args[0]);
				if (player == null) {
					sender.sendMessage(ChatColor.RED + "Player " + args[0] + " was not found!");
				} else {
					try {
						int i = Integer.parseInt(args[1]);
						KarmaHandler handler = new KarmaHandler();
						handler.setPlayerKarma(player, i);
						sender.sendMessage(ChatColor.GREEN + "Set " + player.getDisplayName() + "'s karma to " + i);
						player.sendMessage(ChatColor.LIGHT_PURPLE + "Your karma has been magically set to: " + i);
						return true;
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED + "The second argument must be parsable to an int!");
					}
				}
			}
		}

		if (cmd.getName().equalsIgnoreCase("getkarma")) {
			sender.sendMessage(ChatColor.RED + "Karma is currently disabled, as it is not used for anything.");
			return true;

//			if (args.length > 0) {
//				player = Bukkit.getPlayer(args[0]);
//				if (player == null) {
//					sender.sendMessage(
//							ChatColor.RED + "The player " + args[0] + " could not be found or isn't online!");
//					return false;
//				}
//			} else {
//				if (sender instanceof Player) {
//					player = (Player) sender;
//				} else {
//					sender.sendMessage(ChatColor.RED + "You must be a player to run this command!");
//					return false;
//				}
//			}
//
//			int playerKarma = new KarmaHandler().getPlayerKarma(player);
//			sender.sendMessage(
//					(playerKarma >= 0 ? ChatColor.GREEN : ChatColor.RED) + "Your current karma is: " + playerKarma);
//			return true;
		}

		if (cmd.getName().equalsIgnoreCase("revive")) {
			Player player;
			Location location;
			ResurrectionType resurrectionType;

			if (args.length > 0) {
				player = Bukkit.getPlayer(args[0]);
				if (player == null) {
					sender.sendMessage("The player " + args[0] + " could not be found or isn't online!");
					return false;
				}
				location = player.getLocation(); // revived player's own location for now.'

				if (args.length > 1) {
					resurrectionType = ResurrectionType.valueOf(args[1]);
				} else {
					resurrectionType = ResurrectionType.RELIGIOUS;
				}

			} else {
				if (sender instanceof Player) {
					player = (Player) sender;
					location = player.getLocation(); // sender's location for now
					resurrectionType = ResurrectionType.RELIGIOUS;
				} else {
					sender.sendMessage("You must be a player to run this command!");
					return false;
				}
			}

			RevivalHandler.revivePlayer(player, location, resurrectionType);
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("revivenearest")) {
			Location searchLocation = null;
			Location revivalLocation = null;
			ResurrectionType resurrectionType = ResurrectionType.RELIGIOUS;

			if (args.length > 0) {
				try {
					int x, y, z;
					x = Integer.parseInt(args[0]);
					y = Integer.parseInt(args[1]);
					z = Integer.parseInt(args[2]);
					searchLocation = new Location(Bukkit.getWorlds().get(0), x, y, z);
				} catch (NumberFormatException e) {
					sender.sendMessage("Search Location formatting not correct!");
				}

				if (args.length > 3) {
					try {
						int x, y, z;
						x = Integer.parseInt(args[3]);
						y = Integer.parseInt(args[4]);
						z = Integer.parseInt(args[5]);
						revivalLocation = new Location(Bukkit.getWorlds().get(0), x, y, z);
					} catch (NumberFormatException e) {
						sender.sendMessage("Revival Location formatting not correct!");
					}

				} else {

					if (sender instanceof Entity) {
						revivalLocation = ((Entity) sender).getLocation();
					} else {
						revivalLocation = Bukkit.getWorlds().get(0).getSpawnLocation();
					}

				}

				if (args.length > 6) {
					resurrectionType = ResurrectionType.valueOf(args[6]);
				}

			} else {

				if (sender instanceof Entity) {
					searchLocation = ((Entity) sender).getLocation();
					revivalLocation = ((Entity) sender).getLocation();
					resurrectionType = ResurrectionType.RELIGIOUS;
				} else {
					sender.sendMessage(
							"Couldn't retrieve locations from sender. Are you calling it without arguments from the console?");
				}

			}

			RevivalHandler.reviveNearestDeadPlayer(searchLocation, revivalLocation, resurrectionType);
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("listaltars")) {
			sender.sendMessage(ChatColor.DARK_PURPLE + "All current altars:");
			if (!AltarHandler.hasAltars()) {
				sender.sendMessage(ChatColor.RED + "NONE");
			} else {
				for (Altar altar : AltarHandler.getAllAltars()) {
					String message = altar.getAltarType().toString();
					message += ": " + altar.getImportantBlock().getLocation().toVector().toString();

					if (sender instanceof Player) {
						DecimalFormat df = new DecimalFormat("0.00");
						message += ": " + df.format(
								((Player) sender).getLocation().distance(altar.getImportantBlock().getLocation()))
								+ "m";
					}

					sender.sendMessage(ChatColor.LIGHT_PURPLE + message);
				}
			}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("listrituals")) {
			sender.sendMessage(ChatColor.DARK_PURPLE + "All current configured rituals:");
			if (!RitualHandler.hasRituals())
				sender.sendMessage(ChatColor.RED + "NONE");

			RitualHandler.getRituals().forEach(r -> sender.sendMessage(ChatColor.LIGHT_PURPLE + r.getName()));
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("listsourcebooks")) {
			sender.sendMessage(ChatColor.DARK_PURPLE + "All generated source books:");
			if (!SourceHandler.hasSourceFiles()) {
				sender.sendMessage(ChatColor.RED + "NONE");
			} else {
				int i = 0;
				for (SourceBook book : SourceHandler.getAllSourceBooks()) {
					String message = "[" + ++i + "] " + book.getTitle();
					message += " by " + book.getAuthor();
					sender.sendMessage(ChatColor.LIGHT_PURPLE + ChatColor.stripColor(message));
				}
			}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("givesourcebook")) {
			if (sender instanceof Player) {
				if (args.length > 0) {
					try {
						int index = Integer.parseInt(args[0]) - 1;
						ItemStack book = SourceHandler.getAllSourceBooks().get(index).toItemStack();
						Item drop = ((Player) sender).getWorld().dropItem(((Player) sender).getEyeLocation(), book);
						drop.setPickupDelay(0);
						return true;
					} catch (Exception e) {
						sender.sendMessage(ChatColor.RED + "Couldn't get the selected source book!");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "You must choose the index of which source book you want!");
				}

			} else {
				sender.sendMessage(ChatColor.RED + "You must be a player to run this command!");
			}

		}

		if (cmd.getName().equalsIgnoreCase("gensourcebook")) {
			if (sender instanceof Player) {
				ItemStack book = SourceHandler.getSourceBookItem(ResurrectionType.RELIGIOUS,
						"CULT_BASIC_INSTRUCTION_BOOK");
				Item drop = ((Player) sender).getWorld().dropItem(((Player) sender).getEyeLocation(), book);
				drop.setPickupDelay(0);
				return true;
			}
		}

		return false;
	}

}