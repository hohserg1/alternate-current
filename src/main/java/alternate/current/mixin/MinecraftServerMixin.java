package alternate.current.mixin;

import gloomyfolken.hooklib.api.*;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;

import alternate.current.wire.WireHandler;

import net.minecraft.server.MinecraftServer;

@HookContainer
public class MinecraftServerMixin {

    @Hook(targetMethod = "saveAllWorlds")
    @OnBegin
	public static void alternate_current$save(MinecraftServer self, boolean silent) {
        WorldServer overworld = self.getWorld(DimensionType.OVERWORLD.getId());
		WireHandler wireHandler = ServerWorldMixin.wireHandler.get(overworld);

		wireHandler.getConfig().save(silent);
	}
}
