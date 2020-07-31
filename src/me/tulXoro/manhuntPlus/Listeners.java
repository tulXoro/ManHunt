package me.tulXoro.manhuntPlus;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Listeners implements Listener {
	public HashMap<String, Long> cooldowns = new HashMap<String, Long>();
	
	private ManHunt plugin;
	
	public Listeners (ManHunt plugin) {
		this.plugin = plugin;
	}
	
	  @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
	    if(plugin.getHunters().contains(player.getUniqueId()) && event.hasItem() && event.getItem().getType() == Material.COMPASS && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
	    	Player nearest = null;
	    	
	    	double distance = Double.MAX_VALUE;
	    
	    	for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
	    		if(onlinePlayer.equals(player) || !onlinePlayer.getWorld().equals(player.getWorld()) || plugin.getHunters().contains(onlinePlayer.getUniqueId()))
	    			continue; 
	    		double distanceSquared = onlinePlayer.getLocation().distanceSquared(player.getLocation());
	    		if(distanceSquared < distance) {
	    			distance = distanceSquared;
	    			nearest = onlinePlayer;
	    		} 
	    	} 
		    if(nearest == null) {
		    	player.sendMessage(ChatColor.RED + "No players to track!");
		        return;
		    } 
		    plugin.getCompassMeta().setLodestone(nearest.getLocation());
		    player.sendMessage(ChatColor.GREEN + "Compass is now pointing to " + nearest.getName() + ".");
	    }
	  }
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		if (plugin.getHunters().contains(event.getEntity().getUniqueId()))
		event.getDrops().removeIf(next -> (next.getType() == Material.COMPASS)); 
	}
	  
	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		if(plugin.getHunters().contains(event.getPlayer().getUniqueId()) && event.getItemDrop().getItemStack().getType() == Material.COMPASS) {
			Player player = event.getPlayer();
	    	if(plugin.getWalls()) {
	    		Player nearest = null;
	    		
	    		double distance = Double.MAX_VALUE;
	    		
		    	for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
		    		if(onlinePlayer.equals(player) || !onlinePlayer.getWorld().equals(player.getWorld()) || plugin.getHunters().contains(onlinePlayer.getUniqueId()))
		    			continue; 
		    		double distanceSquared = onlinePlayer.getLocation().distanceSquared(player.getLocation());
		    		if(distanceSquared < distance) {
		    			distance = distanceSquared;
		    			nearest = onlinePlayer;
		    		} 
		    	} 
		    	
		        int cooldownTime = 60; // Get number of seconds from wherever you want
		        if(cooldowns.containsKey(player.getName())) {
		            long secondsLeft = ((cooldowns.get(player.getName())/1000)+cooldownTime) - (System.currentTimeMillis()/1000);
		            if(secondsLeft>0) {
		                // Still cooling down
		                player.sendMessage(ChatColor.RED + "You can't reveal players for another "+ secondsLeft +" seconds!");
		                event.setCancelled(true);
		                return;
		            }
		        }
		        
		        // No cooldown found or cooldown has expired, save new cooldown
		        cooldowns.put(player.getName(), System.currentTimeMillis());
		        
			    if(nearest == null) {
			    	player.sendMessage(ChatColor.RED + "No players to reveal!");
			    	event.setCancelled(true);
			        return;
			    }
		        // Do Command Here
			    PotionEffect potionEffect = new PotionEffect(PotionEffectType.GLOWING, 10, 1, false, false);
			    PotionEffect potionEffect2 = new PotionEffect(PotionEffectType.GLOWING, 20, 1, false, false);
			    nearest.addPotionEffect(potionEffect);
			    nearest.sendMessage(String.format("%s%sYou have been revealed by %s", ChatColor.DARK_RED, ChatColor.BOLD, player.getName()));
			    player.addPotionEffect(potionEffect2);
			    player.sendMessage(String.format("%s%sA player has been revealed!", ChatColor.AQUA, ChatColor.BOLD));
	    	} 
	    	event.setCancelled(true);
		}
	}
	  
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
	    if(plugin.getHunters().contains(player.getUniqueId()))
	    	player.getInventory().addItem(new ItemStack[] { plugin.getCompass() }); 
	}
}
