package alternate.current;

import alternate.current.command.AlternateCurrentCommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import alternate.current.util.profiler.ACProfiler;
import alternate.current.util.profiler.Profiler;

import static alternate.current.AlternateCurrentMod.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION)
public class AlternateCurrentMod {

	public static final String MOD_ID = "alternate-current";
	public static final String MOD_NAME = "Alternate Current";
	public static final String MOD_VERSION = "1.9.0";
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
	public static final boolean DEBUG = false;

	public static boolean on = true;

	public static Profiler createProfiler() {
		return DEBUG ? new ACProfiler() : Profiler.DUMMY;
	}

    @EventHandler
    public void registerCommands(FMLServerStartingEvent event){
        event.registerServerCommand(new AlternateCurrentCommand());
    }
}
