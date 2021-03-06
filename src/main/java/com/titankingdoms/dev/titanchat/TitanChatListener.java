/*
 *     Copyright (C) 2013  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.dev.titanchat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.titankingdoms.dev.titanchat.core.EndPoint;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;
import com.titankingdoms.dev.titanchat.util.Messaging;
import com.titankingdoms.dev.titanchat.util.update.Update;

/**
 * {@link TitanChatListener} - The listener of TitanChat
 * 
 * @author NodinChan
 *
 */
public final class TitanChatListener implements Listener {
	
	private final TitanChat plugin;
	
	private final String site = "http://dev.bukkit.org/server-mods/titanchat/";
	private final Update update;
	
	public TitanChatListener() {
		this.plugin = TitanChat.getInstance();
		this.update = new Update(site + "files.rss", "4.2");
	}
	
	/**
	 * Processes chat
	 * 
	 * @param event {@link AsyncPlayerChatEvent}
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		if (event == null)
			return;
		
		event.setCancelled(true);
		
		Participant participant = plugin.getParticipantManager().getParticipant(event.getPlayer());
		String message = event.getMessage();
		EndPoint target = participant.getCurrentEndPoint();
		
		if (message.startsWith("@") && message.split(" ").length > 1) {
			Channel channel = plugin.getChannelManager().getChannel(message.split(" ")[0].substring(1));
			
			if (channel == null) {
				Messaging.sendMessage(event.getPlayer(), "&4Channel does not exist");
				return;
			}
			
			message = message.substring(message.indexOf(" ") + 1, message.length());
		}
		
		target.processConversation(participant, target.getConversationFormat(), message);
	}
	
	/**
	 * Registers {@link Player}s on join
	 * 
	 * @param event {@link PlayerJoinEvent}
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (event == null)
			return;
		
		Participant participant = plugin.getParticipantManager().getParticipant(event.getPlayer());
		
		if (participant.getCurrentEndPoint() != null)
			participant.notice("&bYou are now speaking in " + participant.getCurrent());
		
		if (!plugin.getConfig().getBoolean("update-notify", true))
			return;
		
		if (participant.hasPermission("TitanChat.update")) {
			if (update.verify()) {
				participant.notice("&5A new version of TitanChat is out!");
				participant.notice("&5You are running &6v" + update.getCurrentVersion());
				participant.notice("&5Update at &9" + site);
			}
		}
	}
	
	/**
	 * Unregisters the {@link Player}s on quit
	 * 
	 * @param event {@link PlayerQuitEvent}
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (event == null)
			return;
		
		Participant participant = plugin.getParticipantManager().getParticipant(event.getPlayer());
		plugin.getParticipantManager().unregisterParticipant(participant);
	}
}