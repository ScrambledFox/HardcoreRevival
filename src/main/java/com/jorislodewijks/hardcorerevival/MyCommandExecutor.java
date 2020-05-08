package com.jorislodewijks.hardcorerevival;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.jorislodewijks.hardcorerevival.HardcoreRevival.ResurrectionType;

import net.md_5.bungee.api.ChatColor;

public class MyCommandExecutor implements CommandExecutor {
	private final HardcoreRevival plugin;

	public MyCommandExecutor(HardcoreRevival plugin) {
		this.plugin = plugin;
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
						KarmaHandler handler = new KarmaHandler(plugin);
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
			Player player;

			if (args.length > 0) {
				player = Bukkit.getPlayer(args[0]);
				if (player == null) {
					sender.sendMessage(
							ChatColor.RED + "The player " + args[0] + " could not be found or isn't online!");
					return false;
				}
			} else {
				if (sender instanceof Player) {
					player = (Player) sender;
				} else {
					sender.sendMessage(ChatColor.RED + "You must be a player to run this command!");
					return false;
				}
			}

			int playerKarma = new KarmaHandler(plugin).getPlayerKarma(player);
			sender.sendMessage(
					(playerKarma >= 0 ? ChatColor.GREEN : ChatColor.RED) + "Your current karma is: " + playerKarma);
			return true;
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
				
				if(args.length > 1) {
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

			new RevivalHandler(plugin).revivePlayer(player, location, resurrectionType);
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
				
				if(args.length > 6) {
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

			new RevivalHandler(plugin).reviveNearestDeadPlayer(searchLocation, revivalLocation, resurrectionType);
			return true;
		}

		return false;
	}

}