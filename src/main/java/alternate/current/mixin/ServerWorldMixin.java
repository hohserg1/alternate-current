package alternate.current.mixin;

import gloomyfolken.hooklib.api.*;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

import alternate.current.wire.WireHandler;

import net.minecraft.server.MinecraftServer;

@HookContainer
public class ServerWorldMixin {

    @FieldLens(createField = true)
    public static FieldAccessor<WorldServer, WireHandler > wireHandler;


    @Hook(targetMethod = Constants.CONSTRUCTOR_NAME)
    @OnReturn
	public static void alternate_current$parseConfig(WorldServer self,
                                                     MinecraftServer server, ISaveHandler storage, WorldInfo data, int dimension, Profiler profiler) {
        wireHandler.set(self, new WireHandler(self, storage));
	}
}
