package com.poixson.morefoods.commands;

import com.poixson.commonmc.tools.commands.pxnCommandsHandler;
import com.poixson.morefoods.MoreFoodsPlugin;


public class Commands extends pxnCommandsHandler<MoreFoodsPlugin> {



	public Commands(final MoreFoodsPlugin plugin) {
		super(plugin,
			"morefoods"
		);
		this.addCommand(new Command_Age(plugin));
	}



}
