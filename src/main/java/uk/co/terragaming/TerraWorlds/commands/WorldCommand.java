package uk.co.terragaming.TerraWorlds.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import uk.co.terragaming.TerraCore.Commands.annotations.Alias;
import uk.co.terragaming.TerraCore.Commands.annotations.Command;
import uk.co.terragaming.TerraCore.Commands.annotations.Desc;
import uk.co.terragaming.TerraCore.Commands.annotations.Perm;
import uk.co.terragaming.TerraCore.Util.Context;


public class WorldCommand {
	
	@Command("world")
	@Desc("World Management Root Command.")
	@Perm("tc.world")
	@Alias("w")
	public CommandResult onWorld(Context context){
		CommandSource sender = context.get(CommandSource.class);
		sender.sendMessage(Text.of(TextColors.AQUA, "Try ", TextColors.YELLOW, Text.builder("/world help").onClick(TextActions.runCommand("/world help")).build(), TextColors.AQUA, " for a list of commands."));
		
		return CommandResult.success();
	}
	
}
