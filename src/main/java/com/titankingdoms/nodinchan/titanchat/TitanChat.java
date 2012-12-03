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

package com.titankingdoms.nodinchan.titanchat;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import com.titankingdoms.nodinchan.titanchat.core.command.CommandManager;
import com.titankingdoms.nodinchan.titanchat.core.addon.Addon;
import com.titankingdoms.nodinchan.titanchat.core.addon.AddonManager;
import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChannelManager;
import com.titankingdoms.nodinchan.titanchat.core.channel.Type;
import com.titankingdoms.nodinchan.titanchat.format.FormatHandler;
import com.titankingdoms.nodinchan.titanchat.info.InfoHandler;
import com.titankingdoms.nodinchan.titanchat.metrics.Metrics;
import com.titankingdoms.nodinchan.titanchat.participant.ChannelParticipant;
import com.titankingdoms.nodinchan.titanchat.participant.ParticipantManager;
import com.titankingdoms.nodinchan.titanchat.permission.Permissions;
import com.titankingdoms.nodinchan.titanchat.util.C;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;
import com.titankingdoms.nodinchan.titanchat.util.Debugger.DebugLevel;
import com.titankingdoms.nodinchan.titanchat.util.Messaging;
import com.titankingdoms.nodinchan.titanchat.format.VariableManager;

/**
 * TitanChat - Main class of TitanChat
 * 
 * @author NodinChan
 *
 */
public final class TitanChat extends JavaPlugin {
	
	private static TitanChat instance;
	
	private final String NAME = "TitanChat Pre-v4.0";
	
	private static final Logger log = Logger.getLogger("TitanLog");
	private static final Debugger db = new Debugger(0, "TitanChat");
	
	private AddonManager addonManager;
	private ChannelManager channelManager;
	private CommandManager commandManager;
	private ParticipantManager participantManager;
	private InfoHandler info;
	private Permissions perms;
	private FormatHandler formatHandler;
	private VariableManager variableManager;
	
	/**
	 * Logs the line to the console
	 * 
	 * @param line The line to log
	 */
	public void chatLog(String line) {
		if (getConfig().getBoolean("logging.colouring"))
			getConsoleSender().sendMessage(line.trim());
		else
			getConsoleSender().sendMessage(line.replaceAll("(?i)(\u00A7)([0-9a-fk-or])", "").trim());
	}
	
	/**
	 * Creates a list in String form seperated by commas
	 * 
	 * @param list The String list to create with
	 * 
	 * @return The created list
	 */
	public String createList(List<String> list) {
		StringBuilder str = new StringBuilder();
		
		for (String item : list) {
			if (str.length() > 0)
				str.append(", ");
			
			str.append(item);
		}
		
		db.i("TitanChat: Created list in String form: " + str.toString());
		return str.toString();
	}
	
	/**
	 * Checks if channels are enabled
	 * 
	 * @return True if channels are enabled
	 */
	public boolean enableChannels() {
		return getConfig().getBoolean("channels.enable");
	}
	
	/**
	 * Checks if join messages are enabled
	 * 
	 * @return True if join messages are enabled
	 */
	public boolean enableJoinMessage() {
		return getConfig().getBoolean("channels.messages.join");
	}
	
	/**
	 * Checks if leave messages are enabled
	 * 
	 * @return True if leave messages are enabled
	 */
	public boolean enableLeaveMessage() {
		return getConfig().getBoolean("channels.messages.leave");
	}
	
	public Addon getAddon(String name) {
		return addonManager.getAddon(name);
	}
	
	public AddonManager getAddonManager() {
		return addonManager;
	}
	
	public Channel getChannel(String name) {
		return channelManager.getChannelByAlias(name);
	}
	
	public ChannelLoader getChannelLoader(String name) {
		return channelManager.getLoader(name);
	}
	
	public ChannelManager getChannelManager() {
		return channelManager;
	}
	
	public CommandManager getCommandManager() {
		return commandManager;
	}
	
	public ConsoleCommandSender getConsoleSender() {
		return getServer().getConsoleSender();
	}
	
	/**
	 * Gets the format handler
	 * 
	 * @return The format handler
	 */
	public FormatHandler getFormatHandler() {
		return formatHandler;
	}
	
	/**
	 * Gets the info handler
	 * 
	 * @return The info handler
	 */
	public InfoHandler getInfoHandler() {
		return info;
	}
	
	/**
	 * Gets an instance of this
	 * 
	 * @return The TitanChat instance
	 */
	public static TitanChat getInstance() {
		return instance;
	}
	
	/**
	 * Gets the offline player with the specified name
	 * 
	 * @param name The name of the player
	 * 
	 * @return The offline player with the name whether the player had player before or not
	 */
	public OfflinePlayer getOfflinePlayer(String name) {
		OfflinePlayer player = getServer().getOfflinePlayer(name);
		return player;
	}
	
	public ChannelParticipant getParticipant(String name) {
		return participantManager.getParticipant(name);
	}
	
	public ParticipantManager getParticipantManager() {
		return participantManager;
	}
	
	/**
	 * Gets the permissions loader
	 * 
	 * @return The permissions loader
	 */
	public Permissions getPermissions() {
		return perms;
	}
	
	/**
	 * Gets the player with the specified name
	 * 
	 * @param name The name of the player
	 * 
	 * @return The player with the specified name if online, otherwise null
	 */
	public Player getPlayer(String name) {
		return getServer().getPlayer(name);
	}
	
	public VariableManager getVariableManager() {
		return variableManager;
	}
	
	/**
	 * Initialise Metrics
	 * 
	 * @return True if Metrics is successfully initialised
	 */
	private boolean initMetrics() {
		log(Level.INFO, "Hooking Metrics");
		
		try {
			Metrics metrics = new Metrics(this);
			
			if (metrics.isOptOut())
				return true;
			
			return metrics.start();
			
		} catch (Exception e) { return false; }
	}
	
	/**
	 * Checks if the sender has TitanChat.staff
	 * 
	 * @param sender The sender to check
	 * 
	 * @return True if the sender has TitanChat.staff
	 */
	public boolean isStaff(CommandSender sender) {
		return sender.hasPermission("TitanChat.staff");
	}
	
	/**
	 * Logs to console
	 * 
	 * @param level The level
	 * 
	 * @param msg The message
	 */
	public void log(Level level, String msg) {
		log.log(level, "[" + NAME + "] " + msg);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("titanchat")) {
			if (args.length < 1 || (args[0].startsWith("@") && args.length < 2)) {
				db.debug(DebugLevel.I, "/TitanChat");
				Messaging.sendMessage(sender, C.DARK_PURPLE + "You are running " + C.GOLD + this);
				Messaging.sendMessage(sender, C.GOLD + "\"/titanchat help [page]\" for help");
				return true;
			}
			
			String commandName = "";
			String channelName = "";
			
			if (args[0].startsWith("@")) {
				commandName = args[1];
				channelName = args[0].substring(1);
				args = Arrays.copyOfRange(args, 2, args.length);
				
			} else {
				commandName = args[0];
				args = Arrays.copyOfRange(args, 1, args.length);
			}
			
			Channel channel = null;
			
			if (!channelName.isEmpty()) {
				if (!channelManager.existingChannel(channelName)) {
					Messaging.sendMessage(sender, C.RED + "Channel does not exist");
					return true;
				}
				
				channel = getChannel(channelName);
				
			} else { channel = getParticipant(sender.getName()).getCurrentChannel(); }
			
			if (channel != null) {
				if (channel.hasCommand(commandName)) {
					channel.getCommand(commandName).execute(sender, channel, args);
					return true;
				}
			}
			
			db.debug(DebugLevel.I, "Dispatching command...");
			commandManager.dispatch(sender, channel, channelName, args);
			return true;
		}
		
		return false;
	}
	
	@Override
	public void onDisable() {
		log(Level.INFO, "is now disabling...");
		log(Level.INFO, "Unloading managers...");
		
		addonManager.unload();
		commandManager.unload();
		info.unload();
		
		log(Level.INFO, "is now disabled");
	}
	
	@Override
	public void onEnable() {
		log(Level.INFO, "is now enabling...");
		
		if (instance == null)
			instance = this;
		
		for (int id : getConfig().getIntegerList("logging.debug"))
			Debugger.startDebug(id);
		
		register(new TitanChatListener());
		
		if (!initMetrics())
			log(Level.WARNING, "Failed to hook into Metrics");
		
		info = new InfoHandler();
		perms = new Permissions();
		formatHandler = new FormatHandler();
		
		InputStream permissionStream = getResource("permissions.yml");
		
		if (permissionStream != null) {
			Map<?, ?> yamlMap = (Map<?, ?>) new Yaml(new SafeConstructor()).load(permissionStream);
			Map<?, ?> permissionMap = (Map<?, ?>) yamlMap.get("permissions");
			
			List<Permission> permissions = new LinkedList<Permission>();
			
			if (permissionMap != null)
				permissions.addAll(Permission.loadPermissions(permissionMap, "Permission node '%s' in plugin description file for " + getDescription().getFullName() + " is invalid", PermissionDefault.OP));
			
			for (Permission permission : permissions)
				try { getServer().getPluginManager().addPermission(permission); } catch (Exception e) {}
		}
		
		addonManager.load();
		channelManager.load();
		commandManager.load();
		
		info.loadLoadedInfo();
		info.loadPlayerInfo();
		
		for (Player player : getServer().getOnlinePlayers())
			info.loadCachedInfo(player);
		
		if (channelManager.getChannels(Type.DEFAULT).isEmpty()) {
			log(Level.SEVERE, "A default channel is not defined");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		log(Level.INFO, "is now enabled");
	}
	
	@Override
	public void onLoad() {
		instance = this;
		
		File config = new File(getDataFolder(), "config.yml");
		
		if (!config.exists()) {
			log(Level.INFO, "Loading default config...");
			saveResource("config.yml", false);
		}
		
		this.addonManager = new AddonManager();
		this.channelManager = new ChannelManager();
		this.commandManager = new CommandManager();
		
		if (channelManager.getChannelDirectory().mkdirs()) {
			log(Level.INFO, "Creating channel directory...");
			saveResource("channels/Default.yml", false);
			saveResource("channels/Global.yml", false);
			saveResource("channels/Local.yml", false);
			saveResource("channels/Password.yml", false);
			saveResource("channels/Private.yml", false);
			saveResource("channels/Public.yml", false);
			saveResource("channels/README.txt", false);
			saveResource("channels/Staff.yml", false);
			saveResource("channels/World.yml", false);
		}
		
		File info = new File(getDataFolder(), "info.yml");
		
		if (!info.exists()) {
			log(Level.INFO, "Loading default info.yml...");
			saveResource("info.yml", false);
		}
	}
	
	/**
	 * Registers the listeners
	 * 
	 * @param listeners the listeners to register
	 */
	public void register(Listener... listeners) {
		for (Listener listener : listeners)
			getServer().getPluginManager().registerEvents(listener, this);
	}
	
	/**
	 * Checks if the player can speak in the channel
	 * 
	 * @param player The player to check
	 * 
	 * @param channel The channel to speak in
	 * 
	 * @param message Whether the player should be told about the check
	 * 
	 * @return True if the player can't speak
	 */
	public boolean voiceless(Player player, Channel channel, boolean message) {
		if (player.hasPermission("TitanChat.voice." + channel.getName()))
			return false;
		
		if (!player.hasPermission("TitanChat.speak." + channel.getName())) {
			if (message)
				Messaging.sendMessage(player, C.RED + "You do not have permission");
			
			return true;
		}
		
		if (getParticipantManager().getParticipant(player).isMuted(channel)) {
			if (message)
				Messaging.sendMessage(player, C.RED + "You have been muted");
			
			return true;
		}
		
		return false;
	}
}