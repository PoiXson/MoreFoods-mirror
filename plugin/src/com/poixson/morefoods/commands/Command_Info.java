package com.poixson.morefoods.commands;

import static com.poixson.morefoods.MoreFoodsPlugin.CHAT_PREFIX;
import static com.poixson.utils.BlockUtils.GetCustomModel;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.poixson.morefoods.MoreFoodsPlugin;
import com.poixson.tools.commands.xCMD_Labels;


public class Command_Info extends xCMD_Labels {



	public Command_Info(final MoreFoodsPlugin plugin) {
		super("info");
	}



	@Override
	public boolean run(final CommandSender sender, final String[] args) {
//TODO: more info - percent aged
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			if (!player.hasPermission("morefoods.cmd.info")) {
				player.sendMessage("You don't have permission to use this command.");
				return true;
			}
			final PlayerInventory inventory = player.getInventory();
			final ItemStack stack = inventory.getItemInMainHand();
			if (stack != null) {
				final int model = GetCustomModel(stack);
				player.sendMessage(String.format("%sCurrent model number: %d", CHAT_PREFIX, Integer.valueOf(model)));
				return true;
			}
		}
		return false;
	}



}
