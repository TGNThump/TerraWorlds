package uk.co.terragaming.TerraWorlds.commands;

import javax.inject.Inject;

import org.spongepowered.api.Server;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.storage.WorldProperties;

import uk.co.terragaming.TerraCore.Commands.annotations.Command;
import uk.co.terragaming.TerraCore.Commands.annotations.Desc;
import uk.co.terragaming.TerraCore.Commands.annotations.Perm;
import uk.co.terragaming.TerraCore.Util.Context;

public class DisableCommand {
	
	@Inject
	Server server;
	
	@Command("world disable")
	@Desc("Disable a World.")
	@Perm("tc.world.disable")
	public CommandResult onDisable(Context context,
		@Desc("The world to disable.") WorldProperties world
	){
		CommandSource source = context.get(CommandSource.class);
		
		world.setEnabled(false);
		
		source.sendMessage(Text.of(TextColors.YELLOW, world.getWorldName(), TextColors.AQUA, " has been enabled."));
		return CommandResult.success();
	}
}
