package me.tulXoro.manhuntPlus.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.tulXoro.manhuntPlus.ManHunt;
import me.tulXoro.manhuntPlus.PluginModes;

public class FreeForAllCommand implements CommandExecutor{

	private ManHunt plugin;
	
	public FreeForAllCommand(ManHunt plugin) {
		this.plugin = plugin;
		plugin.getCommand("freeforall").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length != 1) {
			sendInvalid(sender);
	        return false;
		} 
		
		if(plugin.getPluginMode() != PluginModes.Idle) {
			sender.sendMessage(ChatColor.RED + "Please restart the server to change the game's mode!");
			return false;
		}
		
		for(Player player : Bukkit.getOnlinePlayers()) {
	        plugin.getHunters().add(player.getUniqueId());
	        sender.sendMessage(ChatColor.GREEN + player.getName() + " is now a participant.");
	        player.getInventory().addItem(new ItemStack[] { plugin.getCompass() });
		}
		
		plugin.setPluginMode(PluginModes.FreeForAll);
		return false;
	}
	
	private void sendInvalid(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "Invalid usage. Please use:");
		sender.sendMessage(ChatColor.RED + "/freeforall start");
	}
	
}
