package com.titankingdoms.nodinchan.titanchat.addon;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import com.nodinchan.ncbukkit.loader.Loader;
import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;

/*     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public final class AddonManager {
	
	private final TitanChat plugin;
	
	private static final Debugger db = new Debugger(1);
	
	private List<Addon> addons;
	
	public AddonManager() {
		this.plugin = TitanChat.getInstance();
		
		if (getAddonDir().mkdir())
			plugin.log(Level.INFO, "Creating addon directory...");
		
		this.addons = new ArrayList<Addon>();
	}
	
	public Addon getAddon(String name) {
		for (Addon addon : addons) {
			if (addon.getName().equalsIgnoreCase(name))
				return addon;
		}
		
		return null;
	}
	
	public File getAddonDir() {
		return new File(plugin.getDataFolder(), "addons");
	}
	
	public List<Addon> getAddons() {
		return Collections.unmodifiableList(addons);
	}
	
	public void load() {
		Loader<Addon> loader = new Loader<Addon>(plugin, getAddonDir(), new Object[0]);
		for (Addon addon : loader.load()) { register(addon); }
		addons = loader.sort(addons);
		
		if (addons.size() < 1)
			return;
		
		StringBuilder str = new StringBuilder();
		
		for (Addon addon : addons) {
			if (str.length() > 0)
				str.append(", ");
			
			str.append(addon.getName());
		}
		
		plugin.log(Level.INFO, "Addons loaded: " + str.toString());
	}
	
	public void postReload() {
		load();
	}
	
	public void preReload() {
		unload();
	}
	
	public void register(Addon addon) {
		db.i("Registering addon: " + addon.getName());
		addons.add(addon);
	}
	
	public void unload() {
		for (Addon addon : addons)
			addon.unload();
		
		addons.clear();
	}
}