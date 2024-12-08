package com.poixson.morefoods.commands;

import static com.poixson.morefoods.MoreFoodsPlugin.CHAT_PREFIX;
import static com.poixson.utils.BlockUtils.GetCustomModel;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.poixson.tools.commands.pxnCommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


// /morefoods info
public class Command_Info extends pxnCommand {



	public Command_Info() {
		super(
			"info"
		);
	}



	@Override
	public boolean onCommand(final CommandSender sender, final String[] args) {
//TODO: more info - percent aged
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			if (!player.hasPermission("morefoods.cmd.info")) return false;
			final PlayerInventory inventory = player.getInventory();
			final ItemStack stack = inventory.getItemInMainHand();
			if (stack != null) {
				final int model = GetCustomModel(stack);
				player.sendMessage(CHAT_PREFIX
					.append(Component.text("Model: ").color(NamedTextColor.AQUA))
					.append(Component.text(model    ).color(NamedTextColor.GOLD))
				);
				return true;
			}
		}
		return false;
	}



	@Override
	public List<String> onTabComplete(final CommandSender sender, final String[] args) {
//TODO
System.out.println("TAB:"); for (final String arg : args) System.out.println("  "+arg);
return null;
	}



}
