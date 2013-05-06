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

package com.titankingdoms.dev.titanchat.command.defaults;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.format.ChatUtils;
import com.titankingdoms.dev.titanchat.format.Format;
import com.titankingdoms.dev.titanchat.info.Index;
import com.titankingdoms.dev.titanchat.info.Topic;
import com.titankingdoms.dev.titanchat.info.TopicManager;

public final class InfoCommand extends Command {
	
	public InfoCommand() {
		super("Info");
		setAliases("?");
		setArgumentRange(0, 1024);
		setDescription("Information about TitanChat");
		setUsage("<topic/page>...");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		TopicManager manager = plugin.getTopicManager();
		
		Topic topic = manager.getGeneralIndex();
		int page = 1;
		int pageHeight = manager.getConfig().getInt("page-height", 7);
		
		if (args.length > 0) {
			for (int arg = 0; arg < args.length; arg++) {
				if (!NumberUtils.isNumber(args[arg])) {
					if (topic instanceof Index)
						topic = ((Index) topic).getTopic(args[arg]);
					else
						break;
					
				} else {
					page = NumberUtils.toInt(args[arg]);
					break;
				}
			}
		}
		
		String[][] pages = ChatUtils.paginate(Format.colourise(topic.getInformation()), 119, pageHeight);
		String header = topic.getName() + " (" + page + "/" + pages.length + ")";
		
		sendMessage(sender, ChatColor.AQUA + StringUtils.center(" " + header + " ", 119, '='));
		sendMessage(sender, pages[page]);
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return true;
	}
}