package uk.co.terragaming.TerraWorlds.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import uk.co.terragaming.TerraCore.Commands.annotations.Command;
import uk.co.terragaming.TerraCore.Commands.annotations.Desc;
import uk.co.terragaming.TerraCore.Commands.annotations.Perm;
import uk.co.terragaming.TerraCore.Commands.exceptions.CommandException;
import uk.co.terragaming.TerraCore.Util.Context;


public class TeleportCommand {

	@Command("world tp")
	@Desc("Teleport to a world.")
	@Perm("tc.world.tp")
	public CommandResult onSpawn(Context context,
		@Desc("The world to teleport to") World world
	) throws CommandException{
		CommandSource source = context.get(CommandSource.class);
		
		if (source instanceof Player){
			Player player = (Player) source;
			
			if (!world.isLoaded()){
				source.sendMessage(Text.of(TextColors.RED, "The world ", TextColors.YELLOW, world.getName(), TextColors.RED, " is not loaded."));
				return CommandResult.empty();
			}
			
			if (player.transferToWorld(world.getUniqueId(), world.getSpawnLocation().getPosition())){
				source.sendMessage(Text.of(TextColors.AQUA, "Teleported you to world ", TextColors.YELLOW, world.getName(), TextColors.AQUA, "."));
			} else {
				source.sendMessage(Text.of(TextColors.RED, "Could not teleport you to world ", TextColors.YELLOW, world.getName(), TextColors.RED, "."));
				return CommandResult.empty();
			}

			return CommandResult.success();
		} else {
			source.sendMessage(Text.of(TextColors.RED, "This command can only be run as a Player."));
			return CommandResult.empty();
		}
	}
}
