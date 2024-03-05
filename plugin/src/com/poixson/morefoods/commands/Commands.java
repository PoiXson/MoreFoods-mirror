package com.poixson.morefoods.commands;

import com.poixson.morefoods.MoreFoodsPlugin;
import com.poixson.tools.commands.pxnCommandsHandler;


public class Commands extends pxnCommandsHandler<MoreFoodsPlugin> {



	public Commands(final MoreFoodsPlugin plugin) {
		super(
			"morefoods"
		);
		this.addCommand(new Command_Age(plugin));
	}



}
