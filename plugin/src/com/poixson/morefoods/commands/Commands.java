package com.poixson.morefoods.commands;

import java.io.Closeable;

import com.poixson.morefoods.MoreFoodsPlugin;


public class Commands implements Closeable {

	// /morefoods
	protected final Command_MoreFoods cmd_morefoods;



	public Commands(final MoreFoodsPlugin plugin) {
		this.cmd_morefoods = new Command_MoreFoods(plugin);
	}



	@Override
	public void close() {
		this.cmd_morefoods.close();
	}



}
