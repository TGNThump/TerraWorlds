package uk.co.terragaming.TerraWorlds;

import java.util.Optional;

import javax.inject.Inject;

import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import uk.co.terragaming.TerraCore.CorePlugin;
import uk.co.terragaming.TerraCore.Commands.MethodCommandService;
import uk.co.terragaming.TerraCore.Foundation.GuiceModule;
import uk.co.terragaming.TerraCore.Foundation.Module;
import uk.co.terragaming.TerraCore.Util.Logger.TerraLogger;
import uk.co.terragaming.TerraWorlds.commands.WorldCommand;
import uk.co.terragaming.TerraWorlds.commands.WorldCreateCommand;
import uk.co.terragaming.TerraWorlds.commands.WorldDeleteCommand;

@Module(name = PomData.NAME, version = PomData.VERSION)
public class TerraWorlds extends GuiceModule{
	
	@Inject
	CorePlugin plugin;
	
	@Inject
	MethodCommandService commandService;
	
	@Inject
	Server server;
	
	@Inject
	TerraLogger logger;
	
	@Listener
    public void onInitialize(GameInitializationEvent event) {
		commandService.registerCommands(plugin, new WorldCommand());
		commandService.registerCommands(plugin, new WorldCreateCommand());
		commandService.registerCommands(plugin, new WorldDeleteCommand());
	}
	
	@Listener
	public void onServerStarting(GameStartingServerEvent event){
		for (WorldProperties world : server.getUnloadedWorlds()){
			Optional<World> load = server.loadWorld(world);
			if (load.isPresent()){
				logger.info("<l>Loaded <h>" + world.getWorldName() + "<l>.");
			} else {
				logger.warn("<b>Failed to load <h>" + world.getWorldName() + "<b>.");
			}
		}
	}
	
}