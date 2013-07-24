/*
 *     Copyright (C) 2013  Nodin Chan
 *     
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *     
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *     
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */

package com.titankingdoms.dev.titanchat.util.loading;

import java.io.File;
import java.io.InputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.titankingdoms.dev.titanchat.TitanChat;

public class Loadable implements Comparable<Loadable>, Listener {
	
	protected final TitanChat plugin;
	
	private final String name;
	
	private ClassLoader loader;
	private File file;
	private File dataFolder;
	
	private File configFile;
	private FileConfiguration config;
	
	private boolean initialised = false;
	
	public Loadable(String name) {
		this.plugin = TitanChat.getInstance();
		this.name = name;
	}
	
	final void commenceInitialise(ClassLoader loader, File file, File dataFolder) {
		if (initialised)
			return;
		
		this.loader = loader;
		this.file = file;
		this.dataFolder = dataFolder;
		this.initialised = true;
	}
	
	@Override
	public int compareTo(Loadable loadable) {
		return getName().compareTo((loadable != null) ? loadable.getName() : "");
	}
	
	public final ClassLoader getClassLoader() {
		return loader;
	}
	
	public final FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	public File getConfigFile() {
		return new File(getDataFolder(), "config.yml");
	}
	
	public File getDataFolder() {
		return dataFolder;
	}
	
	public InputStream getDefaultConfigStream() {
		return getResource("config.yml");
	}
	
	protected final File getFile() {
		return file;
	}
	
	public final String getName() {
		return name;
	}
	
	public final InputStream getResource(String name) {
		return loader.getResourceAsStream(name);
	}
	
	public final boolean isInitialised() {
		return initialised;
	}
	
	public InitResult onInitialise() {
		return new InitResult(true);
	}
	
	public final void registerListener(Listener listener, Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
	}
	
	protected final void registerListener(Listener listener) {
		registerListener(listener, plugin);
	}
	
	public final void reloadConfig() {
		if (configFile == null)
			configFile = getConfigFile();
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = getDefaultConfigStream();
		
		if (defConfigStream != null)
			config.setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));
	}
	
	public final void saveConfig() {
		if (configFile == null || config == null)
			return;
		
		try { config.save(configFile); } catch (Exception e) {}
	}
	
	public final class InitResult {
		
		private final boolean success;
		private final String message;
		
		public InitResult(boolean success, String message) {
			this.success = success;
			this.message = (message != null) ? message : "";
		}
		
		public InitResult(boolean success) {
			this(success, "");
		}
		
		public String getMessage() {
			return message;
		}
		
		public boolean hasMessage() {
			return !message.isEmpty();
		}
		
		public boolean isSuccessful() {
			return success;
		}
	}
}