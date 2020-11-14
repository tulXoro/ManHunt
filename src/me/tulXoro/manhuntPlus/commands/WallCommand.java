package me.tulXoro.manhuntPlus.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.tulXoro.manhuntPlus.ManHunt;

public class WallCommand implements CommandExecutor {
	ManHunt plugin;
	
	public WallCommand(ManHunt plugin) {
		this.plugin = plugin;
		plugin.getCommand("walls").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length != 1) {
			sendInvalid(sender);
	        return false;
		}
		
		if(args[0].equalsIgnoreCase("enable")) {
			plugin.setWalls(true);
			sender.sendMessage(ChatColor.GREEN + "Cipher have been enabled");
		}else if(args[0].equalsIgnoreCase("disable")) {
			plugin.setWalls(true);
			sender.sendMessage(ChatColor.GREEN + "Cipher have been disabled");
		}else {
			sendInvalid(sender);
		}
		return false;
	}
	
	private void sendInvalid(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "Invalid usage. Please use:");
		sender.sendMessage(ChatColor.RED + "/cipher enable");
		sender.sendMessage(ChatColor.RED + "/cipher disable");
	}

}
