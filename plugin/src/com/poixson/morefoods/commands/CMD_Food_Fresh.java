package com.poixson.morefoods.commands;

import static com.poixson.morefoods.MoreFoodsDefines.CMD_LABELS_FOOD_FRESH;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.poixson.morefoods.MoreFoodsPlugin;
import com.poixson.tools.commands.PluginCommand;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;


// /food fresh
public interface CMD_Food_Fresh extends PluginCommand {



	default ArgumentBuilder<CommandSourceStack, ?> register_Food_Fresh(final MoreFoodsPlugin plugin) {
		return Commands.literal(CMD_LABELS_FOOD_FRESH.NODE)
			// /food fresh
			.executes(context -> this.onCommand_Food_Fresh(context, plugin));
	}



	default int onCommand_Food_Fresh(final CommandContext<CommandSourceStack> context, final MoreFoodsPlugin plugin) {
//TODO
context.getSource().getSender().sendPlainMessage("Command is unfinished!");
return SUCCESS;
/*
		final CommandSourceStack source = context.getSource();
		final CommandSender sender = source.getSender();
		final Player self = PlayerOnlyNoConsole(sender);
		// permission
		if (!HasPermissionUseCMD(sender, PERM_CMD_FOOD_FRESH.NODE))
			return FAILURE;
		final PlayerInventory inventory = self.getInventory();
		final ItemStack stack = inventory.getItemInMainHand();
		if (IsEmptyOrAir(stack))
			return FAILURE;
		final Set<CustomFoodDAO> states = plugin.getFood(stack.getType());
		if (IsEmpty(states))
			return FAILURE;
		final int model = GetCustomModel(stack);
		
		
		return SUCCESS;
*/
	}



}
