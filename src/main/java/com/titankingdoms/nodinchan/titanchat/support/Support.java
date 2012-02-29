package com.titankingdoms.nodinchan.titanchat.support;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;

public abstract class Support {
	
	protected TitanChat plugin;
	
	private String name;
	
	private File configFile = null;
	private FileConfiguration config = null;
	
	public Support(TitanChat plugin, String name) {
		this.plugin = plugin;
		this.name = name;
	}
	
	public abstract void chatMade(String name, String message);
	
	public abstract String chatMade(Player player, String message);
	
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	public File getDataFolder() {
		File dir = new File(plugin.getSupportsFolder(), name);
		
		dir.mkdir();
		
		return dir;
	}
	
	public Logger getLogger(String name) {
		return Logger.getLogger(name);
	}
	
	public String getName() {
		return name;
	}
	
	public InputStream getResource(String filename) {
		File file = new File(plugin.getSupportsFolder(), plugin.getSupportLoader().getPluginAddonJar(name));
		
		try {
			JarFile jarFile = new JarFile(file);
			Enumeration<JarEntry> entries = jarFile.entries();
			
			while (entries.hasMoreElements()) {
				JarEntry element = entries.nextElement();
				
				if (element.getName().equalsIgnoreCase(filename))
					return jarFile.getInputStream(element);
			}
			
		} catch (IOException e) {}
		
		return null;
	}
	
	public abstract void init();
	
	public void reloadConfig() {
		if (configFile == null)
			configFile = new File(getDataFolder(), "config.yml");
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = getResource("config.yml");
		
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}
	
	public void saveConfig() {
		if (config == null || configFile == null)
			return;
		
		try { config.save(configFile); } catch (IOException e) {}
	}
}