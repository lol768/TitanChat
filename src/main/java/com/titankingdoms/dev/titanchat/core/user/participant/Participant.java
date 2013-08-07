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

package com.titankingdoms.dev.titanchat.core.user.participant;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.core.user.User;

public final class Participant extends User {
	
	public Participant(Player player) {
		super(player.getName());
	}
	
	@Override
	public CommandSender getCommandSender() {
		return getPlayer();
	}
	
	public Player getPlayer() {
		return plugin.getServer().getPlayerExact(getName());
	}
	
	@Override
	public String getType() {
		return "Participant";
	}
	
	public boolean isOnline() {
		return getPlayer() != null;
	}
}