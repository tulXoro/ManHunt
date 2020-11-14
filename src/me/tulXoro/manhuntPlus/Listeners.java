package me.tulXoro.manhuntPlus;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Listeners implements Listener {
	private HashMap<String, Long> cooldowns = new HashMap<String, Long>();
	
	private ManHunt plugin;
	
	public Listeners (ManHunt plugin) {
		this.plugin = plugin;
	}
	
	  @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		// Basically checks if a hunter has a compass and right clicks it
	    if(plugin.getHunters().contains(player.getUniqueId()) && event.hasItem() && event.getItem().getType() == Material.COMPASS && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
	    	// An algorithm which finds the nearest non-hunter player
	    	Player nearest = null;
	    	
	    	double distance = Double.MAX_VALUE;
	    
	    	for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
	    		if(onlinePlayer.equals(player) || !onlinePlayer.getWorld().equals(player.getWorld()) || plugin.getHunters().contains(onlinePlayer.getUniqueId())
	    					|| plugin.getPluginMode() == PluginModes.FreeForAll)
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
	    	
	    	// tests if is overworld
	    	if(player.getWorld().getEnvironment() == World.Environment.NORMAL) {
		    	
		    	// Checks if compass is lodestone tracked (compass won't work
		    	// in the overworld if the compass is lodestone tracked
		    	if(((CompassMeta) event.getItem().getItemMeta()).hasLodestone()) {
		    		player.getInventory().remove(event.getItem());
		    		
		    		player.getInventory().addItem(new ItemStack(Material.COMPASS));
		    	}
	    		player.setCompassTarget(nearest.getLocation());
	    	}else {
	    		player.getInventory().removeItem(event.getItem());
	    		// a block representing the location of a player
	    		// at the bottom of the nether
	    		Location lodestone = new Location(nearest.getWorld(), nearest.getLocation().getX(), 0, nearest.getLocation().getZ());
	    		
	    		lodestone.getBlock().setType(Material.LODESTONE);
	    		
	    		// makes the compass into a lodestone compass
	    		// tracking the lodestone created at the top of the nether
		    	plugin.getCompassMeta().setLodestone(lodestone);
		    	plugin.setCompassMeta(plugin.getCompassMeta());
			    
			    player.getInventory().addItem(plugin.getCompass());
	    	}
	    	
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
		    		if(onlinePlayer.equals(player) || !onlinePlayer.getWorld().equals(player.getWorld()))
		    			continue; 
		    		if(plugin.getHunters().contains(onlinePlayer.getUniqueId()) && plugin.getPluginMode() != PluginModes.FreeForAll)
		    			continue;
		    		double distanceSquared = onlinePlayer.getLocation().distanceSquared(player.getLocation());
		    		if(distanceSquared < distance) {
		    			distance = distanceSquared;
		    			nearest = onlinePlayer;
		    		} 
		    	} 
		    	
		        int cooldownTime = 300; // Get number of seconds from wherever you want
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
			    PotionEffect potionEffect = new PotionEffect(PotionEffectType.GLOWING, 400, 1, false, false);
			    PotionEffect potionEffect2 = new PotionEffect(PotionEffectType.GLOWING, 200, 1, false, false);
			    nearest.addPotionEffect(potionEffect2);
			    nearest.sendMessage(String.format("%s%sYou have been revealed!", ChatColor.DARK_RED, ChatColor.BOLD));
			    player.addPotionEffect(potionEffect);
			    player.sendMessage(String.format("%s%sA player has been revealed!", ChatColor.AQUA, ChatColor.BOLD));
	    	} 
	    	event.setCancelled(true);
		}
	}
	  
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
	    if(plugin.getPluginMode() == PluginModes.FreeForAll) {
	    	player.setGameMode(GameMode.SPECTATOR);
	    	plugin.getHunters().remove(player.getUniqueId());
	    } else if(plugin.getHunters().contains(player.getUniqueId()))
	    	player.getInventory().addItem(new ItemStack[] { plugin.getCompass() }); 
	}
}
