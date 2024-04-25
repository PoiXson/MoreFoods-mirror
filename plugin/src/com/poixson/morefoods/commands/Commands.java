package com.poixson.morefoods.commands;

import com.poixson.morefoods.MoreFoodsPlugin;
import com.poixson.tools.commands.xCMD_Handler;


public class Commands extends xCMD_Handler {

	protected final Command_MoreFoods cmd_morefoods;



	public Commands(final MoreFoodsPlugin plugin) {
		super(plugin);
		this.addCommand(this.cmd_morefoods = new Command_MoreFoods(plugin));
	}



}
