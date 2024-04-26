package com.poixson.morefoods.commands;

import com.poixson.morefoods.MoreFoodsPlugin;
import com.poixson.tools.commands.xCMD_Children;


public class Command_MoreFoods extends xCMD_Children {

	protected final Command_Age  cmd_age;
	protected final Command_Info cmd_info;



	public Command_MoreFoods(final MoreFoodsPlugin plugin) {
		super(
			"morefoods",
			"morefood",
			"foods",
			"food"
		);
		this.addCommand(this.cmd_age  = new Command_Age(plugin));
		this.addCommand(this.cmd_info = new Command_Info(plugin));
	}



}
