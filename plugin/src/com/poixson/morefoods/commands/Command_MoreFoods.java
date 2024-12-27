package com.poixson.morefoods.commands;

import com.poixson.morefoods.MoreFoodsPlugin;
import com.poixson.tools.commands.pxnCommandRoot;


// /morefoods
public class Command_MoreFoods extends pxnCommandRoot {

	protected final Command_Age  cmd_age;  // /morefoods age
	protected final Command_Info cmd_info; // /morefoods info



	public Command_MoreFoods(final MoreFoodsPlugin plugin) {
		super(
			plugin,
			"morefoods", // namespace
			null, // desc
			null, // usage
			null, // perm
			// labels
			"morefoods",
			"morefood",
			"more-foods",
			"more-food",
			"foods",
			"food"
		);
		this.addCommand(this.cmd_age  = new Command_Age(plugin)); // /morefoods age
		this.addCommand(this.cmd_info = new Command_Info());      // /morefoods info
	}



}
