package com.titankingdoms.nodinchan.titanchat.permissionshook;

import java.util.logging.Level;

import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.titankingdoms.nodinchan.titanchat.TitanChat;

import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.util.CalculableType;
import de.bananaco.bpermissions.imp.Permissions;

public class PermissionsHook implements Listener {
	
	private TitanChat plugin;
	
	private Plugin permissionsPlugin;
	
	private String name;
	
	public PermissionsHook(TitanChat plugin) {
		this.plugin = plugin;
	}
	
	public boolean exists(String...packages) {
		try { for (String pkg : packages) { Class.forName(pkg); } return true; } catch (Exception e) { return false; }
	}
	
	public String getGroupPrefix(Player player) {
		String prefix = "";
		
		if (permissionsPlugin != null) {
			if (permissionsPlugin instanceof PermissionsEx) {
				String[] groups = PermissionsEx.getPermissionManager().getUser(player).getGroupsNames();
				String group = (groups != null && groups.length > 0) ? groups[0] : "";
				prefix = PermissionsEx.getPermissionManager().getGroup(group).getPrefix(player.getWorld().getName());
			}
			
			if (permissionsPlugin instanceof Permissions) {
				String[] groups = ApiLayer.getGroups(player.getWorld().getName(), CalculableType.USER, player.getName());
				String group = (groups != null && groups.length > 0) ? groups[0] : "";
				prefix = (!group.equals("")) ? ApiLayer.getValue(player.getWorld().getName(), CalculableType.GROUP, group, "prefix") : "";
			}
			
			if (permissionsPlugin instanceof GroupManager) {
				String group = ((GroupManager) permissionsPlugin).getWorldsHolder().getWorldPermissions(player).getGroup(player.getName());
				prefix = ((GroupManager) permissionsPlugin).getWorldsHolder().getWorldPermissions(player).getGroupPrefix(group);
			}
		}
		
		return prefix;
	}
	
	public String getGroupSuffix(Player player) {
		String suffix = "";
		
		if (permissionsPlugin != null) {
			if (permissionsPlugin instanceof PermissionsEx) {
				String[] groups = PermissionsEx.getPermissionManager().getUser(player).getGroupsNames();
				String group = (groups != null && groups.length > 0) ? groups[0] : "";
				suffix = (!group.equals("")) ? PermissionsEx.getPermissionManager().getGroup(group).getSuffix(player.getWorld().getName()) : "";
			}
			
			if (permissionsPlugin instanceof Permissions) {
				String[] groups = ApiLayer.getGroups(player.getWorld().getName(), CalculableType.USER, player.getName());
				String group = (groups != null && groups.length > 0) ? groups[0] : "";
				suffix = (!group.equals("")) ? ApiLayer.getValue(player.getWorld().getName(), CalculableType.GROUP, group, "suffix") : "";
			}
			
			if (permissionsPlugin instanceof GroupManager) {
				String group = ((GroupManager) permissionsPlugin).getWorldsHolder().getWorldPermissions(player).getGroup(player.getName());
				suffix = ((GroupManager) permissionsPlugin).getWorldsHolder().getWorldPermissions(player).getGroupSuffix(group);
			}
		}
		
		return suffix;
	}
	
	public String getPlayerPrefix(Player player) {
		String prefix = "";
		
		if (permissionsPlugin != null) {
			if (permissionsPlugin instanceof PermissionsEx) {
				PermissionUser user = PermissionsEx.getPermissionManager().getUser(player);
				prefix = (user != null) ? user.getPrefix() : "";
			}
			
			if (permissionsPlugin instanceof Permissions) {
				String[] groups = ApiLayer.getGroups(player.getWorld().getName(), CalculableType.USER, player.getName());
				String group = (groups != null && groups.length > 0) ? groups[0] : "";
				prefix = (!group.equals("")) ? ApiLayer.getValue(player.getWorld().getName(), CalculableType.USER, group, "prefix") : "";
			}
			
			if (permissionsPlugin instanceof GroupManager) {
				prefix = ((GroupManager) permissionsPlugin).getWorldsHolder().getWorldPermissions(player).getUserPrefix(player.getName());
			}
		}
		
		return prefix;
	}
	
	public String getPlayerSuffix(Player player) {
		String suffix = "";
		
		if (permissionsPlugin != null) {
			if (permissionsPlugin instanceof PermissionsEx) {
				PermissionUser user = PermissionsEx.getPermissionManager().getUser(player);
				suffix = (user != null) ? user.getSuffix() : "";
			}
			
			if (permissionsPlugin instanceof Permissions) {
				String[] groups = ApiLayer.getGroups(player.getWorld().getName(), CalculableType.USER, player.getName());
				String group = (groups != null && groups.length > 0) ? groups[0] : "";
				suffix = (!group.equals("")) ? ApiLayer.getValue(player.getWorld().getName(), CalculableType.USER, group, "suffix") : "";
			}
			
			if (permissionsPlugin instanceof GroupManager) {
				suffix = ((GroupManager) permissionsPlugin).getWorldsHolder().getWorldPermissions(player).getUserSuffix(player.getName());
			}
		}
		
		return suffix;
	}
	
	public boolean has(Player player, String permission) {
		if (permissionsPlugin != null) {
			if (permissionsPlugin instanceof PermissionsEx)
				return PermissionsEx.getPermissionManager().getUser(player).has(permission, player.getWorld().getName());
			
			if (permissionsPlugin instanceof Permissions)
				return ApiLayer.hasPermission(player.getWorld().getName(), CalculableType.USER, player.getName(), permission);
			
			if (permissionsPlugin instanceof GroupManager)
				return ((GroupManager) permissionsPlugin).getWorldsHolder().getWorldPermissions(player.getWorld().getName()).permission(player, permission);
		}
		
		return player.hasPermission(permission);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPluginDisable(PluginDisableEvent event) {
		if (permissionsPlugin != null) {
			if (event.getPlugin().getName().equals(name)) {
				permissionsPlugin = null;
				plugin.log(Level.INFO, name + " unhooked");
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPluginEnable(PluginEnableEvent event) {
		if (permissionsPlugin == null) {
			Plugin perms = null;
			
			if (perms == null) {
				if (exists("ru.tehkode.permissions.bukkit.PermissionsEx"))
					perms = plugin.getServer().getPluginManager().getPlugin("PermissionsEx");
				
				if (exists("de.bananaco.bpermissions.imp.Permissions"))
					perms = plugin.getServer().getPluginManager().getPlugin("bPermissions");
				
				if (exists("com.platymuus.bukkit.permissions.PermissionsPlugin"))
					perms = plugin.getServer().getPluginManager().getPlugin("PermissionsBukkit");
				
				if (exists("org.anjocaido.groupmanager.GroupManager"))
					perms = plugin.getServer().getPluginManager().getPlugin("GroupManager");
				
				if (exists("org.tyrannyofheaven.bukkit.zPermissions.ZPermissionsPlugin"))
					perms = plugin.getServer().getPluginManager().getPlugin("zPermissions");
			}
			
			if (perms != null && perms.isEnabled()) {
				permissionsPlugin = perms;
				name = permissionsPlugin.getName();
				
			} else { name = "SuperPerms"; }
			
			if (!plugin.vaultSetup()) { plugin.log(Level.INFO, name + " detected and hooked"); }
		}
	}
}