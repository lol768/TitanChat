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

package com.titankingdoms.dev.titanchat.core.user.console;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.core.user.User;

public final class Console extends User {
	
	public Console() {
		super("CONSOLE");
	}
	
	@Override
	public CommandSender getCommandSender() {
		return plugin.getServer().getConsoleSender();
	}
	
	@Override
	public void sendRawLine(String line) {
		getCommandSender().sendMessage(line);
	}
}