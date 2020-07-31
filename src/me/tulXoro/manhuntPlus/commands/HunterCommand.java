package me.tulXoro.manhuntPlus.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.tulXoro.manhuntPlus.ManHunt;

public class HunterCommand implements CommandExecutor{
	
	ManHunt plugin;
	ItemStack compass = new ItemStack(Material.COMPASS);
	
	
	public HunterCommand(ManHunt plugin) {
		this.plugin = plugin;
		plugin.getCommand("hunter").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length != 2) {
			sendInvalid(sender);
	        return false;
		} 
		
	    Player player = Bukkit.getPlayer(args[1]);
	    if(player == null) {
	    	sender.sendMessage(ChatColor.RED + "Player not found.");
	        return false;
	    } 
	    if(args[0].equalsIgnoreCase("add")) {
	        plugin.getHunters().add(player.getUniqueId());
	        sender.sendMessage(ChatColor.GREEN + player.getName() + " is now a hunter.");
	        player.getInventory().addItem(new ItemStack[] { plugin.getCompass() });
	        
	    }else if(args[0].equalsIgnoreCase("remove")) {
	    	plugin.getHunters().remove(player.getUniqueId());
	        sender.sendMessage(ChatColor.GREEN + player.getName() + " is no longer a hunter.");
	        player.getInventory().remove(plugin.getCompass());
	    }else{
	        sendInvalid(sender);
	    }
		return false;
	}
	
	private void sendInvalid(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "Invalid usage. Please use:");
		sender.sendMessage(ChatColor.RED + "/hunter add <name>");
		sender.sendMessage(ChatColor.RED + "/hunter remove <name>");
	}

}
