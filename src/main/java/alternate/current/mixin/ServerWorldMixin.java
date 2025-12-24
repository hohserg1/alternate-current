package alternate.current.mixin;

import alternate.current.wire.WireHandler;
import gloomyfolken.hooklib.api.*;
import net.minecraft.world.WorldServer;

@HookContainer
public class ServerWorldMixin {

    @FieldLens(createField = true)
    private static FieldAccessor<WorldServer, WireHandler> wireHandler;

    public static WireHandler getWireHandler(WorldServer world) {
        WireHandler wh = wireHandler.get(world);
        if (wh != null) {
            return wh;
        } else {
            WireHandler wh2 = new WireHandler(world);
            wireHandler.set(world, wh2);
            return wh2;
        }
    }
}
