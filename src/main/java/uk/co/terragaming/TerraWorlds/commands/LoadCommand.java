package uk.co.terragaming.TerraWorlds.commands;

import java.util.Optional;

import javax.inject.Inject;

import org.spongepowered.api.Server;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import uk.co.terragaming.TerraCore.CorePlugin;
import uk.co.terragaming.TerraCore.Commands.annotations.Command;
import uk.co.terragaming.TerraCore.Commands.annotations.Desc;
import uk.co.terragaming.TerraCore.Commands.annotations.Perm;
import uk.co.terragaming.TerraCore.Util.Context;


public class LoadCommand {
	
	@Inject
	Server server;
	
	@Inject
	Scheduler scheduler;
	
	@Inject
	CorePlugin plugin;
	
	@Command("world load")
	@Desc("Load a World.")
	@Perm("tc.world.load")
	public CommandResult onLoad(Context context,
		@Desc("The world to load.") WorldProperties world
	){
		CommandSource source = context.get(CommandSource.class);
		
		if (server.getWorld(world.getUniqueId()).isPresent()){
			source.sendMessage(Text.of(TextColors.RED, "The world ", TextColors.YELLOW, world.getWorldName(), TextColors.RED, " is allready loaded."));
			return CommandResult.empty();
		}
		
		scheduler.createTaskBuilder().name("TC_WORLDS_" + world.getWorldName()).delayTicks(20).execute(new Runnable(){

			@Override
			public void run() {
				Optional<World> load = server.loadWorld(world);
				
				if (!load.isPresent()){
					source.sendMessage(Text.of(TextColors.RED, "Could not load ", TextColors.YELLOW, world.getWorldName(), TextColors.RED, "."));
					return;
				}
				
				World world = load.get();
				world.setKeepSpawnLoaded(true);
				source.sendMessage(Text.of(TextColors.YELLOW, world.getName(), TextColors.AQUA, " loaded successfully."));
			}
			
		}).submit(plugin);
		
		return CommandResult.success();
	}
}
