package me.tulXoro.manhuntPlus;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.tulXoro.manhuntPlus.commands.FreeForAllCommand;
import me.tulXoro.manhuntPlus.commands.HunterCommand;
import me.tulXoro.manhuntPlus.commands.WallCommand;
import net.md_5.bungee.api.ChatColor;

public class ManHunt extends JavaPlugin implements CommandExecutor{
	
	private Set<UUID> hunters;
	private ItemStack compass = new ItemStack(Material.COMPASS);
	
	private boolean walls = false;
	private PluginModes mode = PluginModes.Idle;
	private Player infiltrator;
	
	private CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
		
		// Enables Commands
		new HunterCommand(this);
		new WallCommand(this);
		new FreeForAllCommand(this);
		
		// list of hunters
		this.hunters = new HashSet<>();
		
		// Sets compass as a lodestone compass
		// allows Nether tracking
	    //compassMeta.setLodestoneTracked(true);
	    compassMeta.setDisplayName(String.format("%s%sTracking Compass", ChatColor.DARK_PURPLE, ChatColor.BOLD));
	    compass.setItemMeta(compassMeta);
	}
	
	// Setters
	public void setWalls(boolean bool) {
		this.walls = bool;
	}
	
	public void setCompassMeta(CompassMeta data) { 
		this.compassMeta = data;
		compass.setItemMeta(this.compassMeta);
	}
	
	public void setPluginMode(PluginModes mode) {
		this.mode = mode;
	}
	
	public void setInfiltrator(Player infiltrator) {
		this.infiltrator = infiltrator;
	}
	
	// Getters
	public Set<UUID> getHunters() {
		return this.hunters;
	}
	
	public ItemStack getCompass() {
		return this.compass;
	}
	
	public CompassMeta getCompassMeta() {
		return this.compassMeta;
	}
	
	public boolean getWalls() {
		return this.walls;
	}
	
	public PluginModes getPluginMode() { 
		return this.mode;
	}
	
	public Player getInfiltrator() {
		return this.infiltrator;
	}
}
