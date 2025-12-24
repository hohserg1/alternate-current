package alternate.current.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import alternate.current.AlternateCurrentMod;
import alternate.current.mixin.ServerWorldMixin;
import alternate.current.util.profiler.ProfilerResults;
import alternate.current.wire.UpdateOrder;
import alternate.current.wire.WireHandler;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class AlternateCurrentCommand extends CommandBase {

	@Override
	public String getName() {
		return "alternatecurrent";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender source) {
		return AlternateCurrentMod.DEBUG ? "/alternatecurrent [on/off/updateOrder[ updateOrder]/resetProfiler]" : "/alternatecurrent [on/off/updateOrder[ updateOrder]]";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender source, String[] args) throws CommandException {
		switch (args.length) {
		case 0:
			queryEnabled(source);
			return;
		case 1:
			switch (args[0]) {
			case "on":
				setEnabled(source, true);
				return;
			case "off":
				setEnabled(source, false);
				return;
			case "updateOrder":
				queryUpdateOrder(source);
				return;
			case "resetProfiler":
				if (AlternateCurrentMod.DEBUG) {
					notifyCommandListener(source, this, "profiler results have been cleared!");

					ProfilerResults.log();
					ProfilerResults.clear();

					return;
				}
			}

			break;
		case 2:
			switch (args[0]) {
			case "updateOrder":
				for (UpdateOrder updateOrder : UpdateOrder.values()) {
					if (args[1].equals(updateOrder.id())) {
						setUpdateOrder(source, updateOrder);
						return;
					}
				}
			}
		}

		throw new WrongUsageException(getUsage(source));
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender source, String[] args, BlockPos pos) {
		switch (args.length) {
		case 0:
			return AlternateCurrentMod.DEBUG
				? getListOfStringsMatchingLastWord(args, "on", "off", "updateOrder", "resetProfiler")
				: getListOfStringsMatchingLastWord(args, "on", "off", "updateOrder");
		case 1:
			switch (args[0]) {
			case "updateOrder":
				List<String> updateOrders = new ArrayList<>();

				for (UpdateOrder updateOrder : UpdateOrder.values()) {
					updateOrders.add(updateOrder.id());
				}

				return getListOfStringsMatchingLastWord(args, updateOrders);
			}
		}

		return Collections.emptyList();
	}


	private void queryEnabled(ICommandSender source) {
		World world = source.getEntityWorld();
		WireHandler wireHandler = ServerWorldMixin.getWireHandler((WorldServer) world);

		String state = wireHandler.getConfig().getEnabled()? "enabled" : "disabled";
		source.sendMessage(new TextComponentString(String.format("Alternate Current is currently %s", state)));
	}

	private void setEnabled(ICommandSender source, boolean on) {
		World world = source.getEntityWorld();
		WireHandler wireHandler = ServerWorldMixin.getWireHandler((WorldServer) world);

		wireHandler.getConfig().setEnabled(on);

		String state = wireHandler.getConfig().getEnabled() ? "enabled" : "disabled";
		notifyCommandListener(source, this, String.format("Alternate Current has been %s!", state));
	}

	private void queryUpdateOrder(ICommandSender source) {
		World world = source.getEntityWorld();
		WireHandler wireHandler = ServerWorldMixin.getWireHandler((WorldServer) world);

		String value = wireHandler.getConfig().getUpdateOrder().id();
		source.sendMessage(new TextComponentString(String.format("Update order is currently %s", value)));
	}

	private void setUpdateOrder(ICommandSender source, UpdateOrder updateOrder) {
		World world = source.getEntityWorld();
		WireHandler wireHandler = ServerWorldMixin.getWireHandler((WorldServer) world);

		wireHandler.getConfig().setUpdateOrder(updateOrder);

		String value = wireHandler.getConfig().getUpdateOrder().id();
		notifyCommandListener(source, this, String.format("update order has been set to %s!", value));
	}
}
