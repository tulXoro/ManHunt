package me.tulXoro.manhuntPlus.commands;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.tulXoro.manhuntPlus.ManHunt;
import me.tulXoro.manhuntPlus.PluginModes;

public class InfiltratorCommand implements CommandExecutor{
	private ManHunt plugin;
	
	public InfiltratorCommand(ManHunt plugin) {
		this.plugin = plugin;
		plugin.getCommand("infiltration").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length != 1) {
			sendInvalid(sender);
	        return false;
		} 
		
		if(plugin.getPluginMode() != PluginModes.Traditional) {
			sender.sendMessage(ChatColor.RED + "Please restart the server to change the game's mode or add at least a hunter!");
			return false;
		}
		
		int size = plugin.getHunters().size();
		int random = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
		int i = 0;
		for(UUID playerID : plugin.getHunters())
		{
		    if (i == random) {
		    	Player player = Bukkit.getPlayer(playerID);
		    	plugin.setInfiltrator(player);
		    	player.sendMessage(ChatColor.AQUA + "You have been selected to help the speedrunner!");
		    }
		    i++;
		}
		plugin.setPluginMode(PluginModes.Infiltration);
		return false;
	}
	
	private void sendInvalid(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "Invalid usage. Please use:");
		sender.sendMessage(ChatColor.RED + "/infiltration start");
	}

}