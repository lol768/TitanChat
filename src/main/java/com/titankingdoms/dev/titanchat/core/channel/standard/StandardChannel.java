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

package com.titankingdoms.dev.titanchat.core.channel.standard;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.info.Range;
import com.titankingdoms.dev.titanchat.core.channel.info.Status;
import com.titankingdoms.dev.titanchat.format.Format;

public class StandardChannel extends Channel {
	
	public StandardChannel(String name, Status status) {
		super(name, "Standard", status);
	}
	
	@Override
	public String[] getAliases() {
		return getConfig().getStringList("aliases").toArray(new String[0]);
	}
	
	@Override
	public String getDisplayColour() {
		return getConfig().getString("display-colour", "");
	}
	
	@Override
	public String getFormat() {
		return getConfig().getString("format", Format.getFormat());
	}
	
	@Override
	public Range getRange() {
		return Range.fromName(getConfig().getString("range", "channel"));
	}
	
	@Override
	public void reload() {}
}