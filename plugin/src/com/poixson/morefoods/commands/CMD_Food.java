package com.poixson.morefoods.commands;

import static com.poixson.morefoods.MoreFoodsDefines.CMD_LABELS_FOOD;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.poixson.morefoods.MoreFoodsPlugin;
import com.poixson.tools.commands.PluginCommand;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /food <cmd>
public interface CMD_Food extends PluginCommand,
CMD_Food_Info,
CMD_Food_Rot {



	default ArgumentBuilder<CommandSourceStack, ?> register_Food(final MoreFoodsPlugin plugin) {
		// /food <cmd>
		return Commands.literal(CMD_LABELS_FOOD.NODE)
			.then(CMD_Food_Info .super.register_Food_Info (plugin))  // /food info
			.then(CMD_Food_Rot  .super.register_Food_Rot  (plugin)); // /food rot
	}



}
