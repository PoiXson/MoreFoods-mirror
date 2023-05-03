package com.poixson.morefoods.commands;

import static com.poixson.morefoods.MoreFoodsPlugin.CHAT_PREFIX;

import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.poixson.commonmc.tools.commands.pxnCommand;
import com.poixson.commonmc.utils.ItemUtils;
import com.poixson.morefoods.CustomFoodDAO;
import com.poixson.morefoods.MoreFoodsPlugin;
import com.poixson.utils.Utils;


public class Command_Age extends pxnCommand<MoreFoodsPlugin> {



	public Command_Age(final MoreFoodsPlugin plugin) {
		super(plugin,
			"age",
			"rot"
		);
	}



	@Override
	public boolean run(final CommandSender sender, final String label, final String[] args) {
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			if (!player.hasPermission("morefoods.agecmd")) {
				player.sendMessage(CHAT_PREFIX + "You don't have permission to use this command.");
				return true;
			}
			final PlayerInventory inventory = player.getInventory();
			final ItemStack stack = inventory.getItemInMainHand();
			if (stack != null) {
				final Set<CustomFoodDAO> states = this.plugin.getFood(stack.getType());
				if (Utils.notEmpty(states)) {
					final int model = ItemUtils.GetCustomModel(stack);
					for (final CustomFoodDAO dao : states) {
						if (dao.model == model) {
							int modelNew = model;
							for (int i=0; i<100; i++) {
								if (!this.plugin.getAgeHandler().ageItem(stack)) {
									sender.sendMessage(CHAT_PREFIX + "The item in your main hand cannot be aged");
									return false;
								}
								modelNew = ItemUtils.GetCustomModel(stack);
								if (model != modelNew)
									break;
							}
							inventory.setItemInMainHand(stack);
							if (player.hasPermission("morefoods.details")) {
								sender.sendMessage(String.format("%sThe food spoils in your hand, from model %d to %d",
										CHAT_PREFIX, Integer.valueOf(model), Integer.valueOf(modelNew)));
							} else {
								sender.sendMessage(CHAT_PREFIX + "The food spoils in your hand");
							}
							return true;
						}
					}
				}
			}
		}
		return false;
	}



}
