package com.akaiha.wild;

import org.bukkit.plugin.java.JavaPlugin;

import com.akaiha.wild.commands.Commands;

public class Wild extends JavaPlugin {
	
    @Override
    public void onEnable(){
    	configLoad();
        getCommand("wild").setExecutor(new Commands(this));
    }
    
    public void configLoad() {
    	getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }
}