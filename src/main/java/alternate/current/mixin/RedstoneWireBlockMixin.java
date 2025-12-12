package alternate.current.mixin;

import gloomyfolken.hooklib.api.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.WorldServer;

import alternate.current.AlternateCurrentMod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@HookContainer
public class RedstoneWireBlockMixin {

    @Hook(targetMethod = "updateSurroundingRedstone")
    @OnBegin
	public static ReturnSolve<IBlockState> alternate_current$onUpdate(BlockRedstoneWire wire, World world, BlockPos pos, IBlockState state) {
		if (AlternateCurrentMod.on) {
			// Using redirects for calls to this method makes conflicts with
			// other mods more likely, so we inject-cancel instead.
            return ReturnSolve.yes(state);
		}
        return ReturnSolve.no();
	}

    @Hook(targetMethod = "onBlockAdded")
    @OnMethodCall("updateSurroundingRedstone")
	public static void alternate_current$onAdded(BlockRedstoneWire wire, World world, BlockPos pos, IBlockState state) {
		if (AlternateCurrentMod.on) {
            ServerWorldMixin.wireHandler.get((WorldServer) world).onWireAdded(pos);
		}
	}

    @Hook(targetMethod = "breakBlock")
    @OnMethodCall("updateSurroundingRedstone")
	public static void alternate_current$onRemoved(BlockRedstoneWire wire, World world, BlockPos pos, IBlockState state) {
		if (AlternateCurrentMod.on) {
            ServerWorldMixin.wireHandler.get((WorldServer) world).onWireRemoved(pos, state);
		}
	}

    @Hook(targetMethod = "neighborChanged")
    @OnBegin
	public static ReturnSolve<Void> alternate_current$onNeighborChanged(BlockRedstoneWire wire,
                                                                        IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos) {
		if (AlternateCurrentMod.on) {
			if (ServerWorldMixin.wireHandler.get((WorldServer) world).onWireUpdated(pos)) {
				// needed to fix duplication bugs
                return ReturnSolve.yes(null);
			}
		}
        return ReturnSolve.no();
	}
}
