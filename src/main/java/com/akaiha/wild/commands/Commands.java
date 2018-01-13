package com.akaiha.wild.commands;

import org.bukkit.command.CommandExecutor;

import java.util.SplittableRandom;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.akaiha.wild.Wild;

public class Commands implements CommandExecutor {
	
	private Wild plugin;
	private SplittableRandom rand;
	
	public Commands(Wild plugin) {
		this.plugin = plugin;
		this.rand = new SplittableRandom();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			if (args.length == 0 && sender instanceof Player && sender.hasPermission("wild")) {
				Player p = (Player) sender;
				int max = plugin.getConfig().getInt("max");
				int min = plugin.getConfig().getInt("min");
				teleport(p,wild(p,max,min,max,min));
			}
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("wild.reload")) {
					plugin.configLoad();
					return false;
				}
				if (sender.hasPermission("wild.others")) {
					Player p = plugin.getServer().getPlayer(args[0]);
					if (p != null) {
						int max = plugin.getConfig().getInt("max");
						int min = plugin.getConfig().getInt("min");
						teleport(p,wild(p,max,min,max,min));
					}
				}
			}
			if (args.length == 2 && sender instanceof Player && sender.hasPermission("wild.override")) {
				Player p = (Player) sender;
				if (p != null) {
					int max = Integer.parseInt(args[1]);
					int min = Integer.parseInt(args[0]);
					teleport(p,wild(p,max,min,max,min));
				}
			}
			if (args.length > 2 && sender.hasPermission("wild.others.override")) {
				Player p = plugin.getServer().getPlayer(args[0]);
				if (p != null) {
					int max = Integer.parseInt(args[2]);
					int min = Integer.parseInt(args[1]);
					teleport(p,wild(p,max,min,max,min));
				}
			}
		} catch (Exception e) {
			plugin.getLogger().severe("Execution Error: /wild");
			e.printStackTrace();
		}
		return false;
	}
	
	public Location wild(Player p, int maxX, int minX, int maxZ, int minZ) {
		Location l;
		int count = 0;
		do { 
			l = p.getWorld().getHighestBlockAt(rand.nextInt(minX,maxX),rand.nextInt(minZ,maxZ)).getLocation();
			count++;
		} while (!isSafeLocation(l) && count <= 5);
		if (count == 5) {
			l = p.getLocation();
		}
		return l;
	}
	
	public boolean isSafeLocation(Location location) {
        Block feet = location.getBlock();
        if (!feet.getType().isTransparent() && !feet.getLocation().add(0, 1, 0).getBlock().getType().isTransparent()) {
            return false;
        }
        Block head = feet.getRelative(BlockFace.UP);
        if (!head.getType().isTransparent()) {
            return false;
        }
        Block ground = feet.getRelative(BlockFace.DOWN);
        if (!ground.getType().isSolid()) {
            return false;
        }
        return true;
    }
	
	public void teleport(Player p, Location l) {
		p.teleport(l);
	}
}
