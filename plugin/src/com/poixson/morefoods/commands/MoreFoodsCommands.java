package com.poixson.morefoods.commands;

import static com.poixson.morefoods.MoreFoodsDefines.CMD_DESC_FOOD;
import static com.poixson.morefoods.MoreFoodsDefines.CMD_LABELS_FOOD;

import com.poixson.morefoods.MoreFoodsPlugin;
import com.poixson.tools.commands.PluginCommandsHolder;

import io.papermc.paper.command.brigadier.Commands;


public class MoreFoodsCommands extends PluginCommandsHolder<MoreFoodsPlugin> implements
CMD_Food {



	public MoreFoodsCommands(final MoreFoodsPlugin plugin) {
		super(plugin);
	}



	@Override
	protected void register_commands(final Commands registrar) {
		this.register_cmd(registrar, CMD_Food.super.register_Food(this.plugin), CMD_DESC_FOOD.NODE, CMD_LABELS_FOOD.NODES); // /food <cmd>
	}



}
