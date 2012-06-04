package me.NerdsWBNerds.SoftBlock;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import org.bukkit.plugin.java.JavaPlugin;

public class SoftBlock extends JavaPlugin implements Listener, CommandExecutor{
	public ArrayList<Integer> softBlocks = new ArrayList<Integer>();
	
	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(this, this);
		
		this.getCommand("sb").setExecutor(this);
		load();
	}
	
	public void onDisable(){
		load();
	}
	
	public void load(){
		softBlocks = new ArrayList<Integer>();
		
		if(getConfig().get("softBlocks")==null){
			getConfig().set("softBlocks", "88,82,80,12,35");
			saveConfig();
		}else{
			String sb = getConfig().getString("softBlocks");
			String sbl[] = sb.split(",");
			
			for(String i: sbl){
				softBlocks.add(Integer.parseInt(i));
			}
		}
	}
	
	@EventHandler
	public void onTakeDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			if(e.getCause() == DamageCause.FALL){
				Location playerFall = e.getEntity().getLocation().subtract(0, 1, 0);
				
				if(isSoft(playerFall.getBlock().getTypeId())){
					e.setCancelled(true);
					e.setDamage(0);
				}
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(sender instanceof Player){
			Player player = (Player) sender;
			
			if(!player.isOp()){
				player.sendMessage(ChatColor.RED + "[SoftBlock] You do not have permission to do this.");
				return true;
			}
			
			if(cmd.getName().equalsIgnoreCase("sb")){
				load();
				player.sendMessage(ChatColor.GOLD + "[SoftBlock] " + ChatColor.GREEN + "Config reloaded.");
				return true;
			}
		}else{
			if(cmd.getName().equalsIgnoreCase("sb")){
				load();
				System.out.println("[SoftBlock] Config reloaded.");
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isSoft(int i){
		for(int ii: softBlocks){
			if(ii==i)
				return true;
		}
		
		return false;
	}
}
