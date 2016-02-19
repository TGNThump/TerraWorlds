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
import uk.co.terragaming.TerraWorlds.commands.CreateCommand;
import uk.co.terragaming.TerraWorlds.commands.DeleteCommand;
import uk.co.terragaming.TerraWorlds.commands.DisableCommand;
import uk.co.terragaming.TerraWorlds.commands.EnableCommand;
import uk.co.terragaming.TerraWorlds.commands.ListCommand;
import uk.co.terragaming.TerraWorlds.commands.LoadCommand;
import uk.co.terragaming.TerraWorlds.commands.RenameCommand;
import uk.co.terragaming.TerraWorlds.commands.SpawnCommand;
import uk.co.terragaming.TerraWorlds.commands.TeleportCommand;
import uk.co.terragaming.TerraWorlds.commands.UnloadCommand;
import uk.co.terragaming.TerraWorlds.commands.WorldCommand;

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
		commandService.registerCommands(plugin, new ListCommand());
		commandService.registerCommands(plugin, new CreateCommand());
		commandService.registerCommands(plugin, new DeleteCommand());
		commandService.registerCommands(plugin, new TeleportCommand());
		commandService.registerCommands(plugin, new UnloadCommand());
		commandService.registerCommands(plugin, new RenameCommand());
		commandService.registerCommands(plugin, new SpawnCommand());
		commandService.registerCommands(plugin, new LoadCommand());
		commandService.registerCommands(plugin, new EnableCommand());
		commandService.registerCommands(plugin, new DisableCommand());
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