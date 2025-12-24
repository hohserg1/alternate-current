package alternate.current.mixin;

import alternate.current.wire.WireHandler;
import gloomyfolken.hooklib.api.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;

@HookContainer
public class MinecraftServerMixin {

    @Hook(targetMethod = "saveAllWorlds")
    @OnBegin
    public static void alternate_current$save(MinecraftServer self, boolean silent) {
        WorldServer overworld = self.getWorld(DimensionType.OVERWORLD.getId());
        WireHandler wireHandler = ServerWorldMixin.getWireHandler(overworld);
        wireHandler.getConfig().save(silent);
    }
}
