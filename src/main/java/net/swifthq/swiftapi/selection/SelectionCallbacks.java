package net.swifthq.swiftapi.selection;

import net.fabricmc.fabric.impl.base.util.ActionResult;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.swifthq.swiftapi.callbacks.entity.player.ItemInteractCallback;
import net.swifthq.swiftapi.callbacks.world.BlockBreakCallback;
import net.swifthq.swiftapi.player.SwPlayer;

public class SelectionCallbacks {

	public static void initialize() {
		ItemInteractCallback.EVENT.register((player, pos, stack) -> {
			if(pos != null) {
				Block block = player.world.getBlockState(pos).getBlock();
				if(stack.getItem() == Items.WOODEN_AXE && isPlayerOp(player) && block != Blocks.AIR) {
					if(((SwPlayer) player).getData(SwPlayer.SELECTION_POS_2) != pos) {
						((SwPlayer) player).putData(SwPlayer.SELECTION_POS_2, pos);
						player.sendMessage(new LiteralText("Position 2 set to " + friendlyToString(pos)).setStyle(new Style().setColor(Formatting.LIGHT_PURPLE)));
					}
				}
			}
			return ActionResult.PASS;
		});

		BlockBreakCallback.EVENT.register((player, pos) -> {
			if(pos != null) {
				ItemStack stack = player.getMainHandStack();
				Block block = player.world.getBlockState(pos).getBlock();
				if(stack.getItem() == Items.WOODEN_AXE && isPlayerOp(player) && block != Blocks.AIR) {
					if(((SwPlayer) player).getData(SwPlayer.SELECTION_POS_1) != pos) {
						((SwPlayer) player).putData(SwPlayer.SELECTION_POS_1, pos);
						player.sendMessage(new LiteralText("Position 1 set to " + friendlyToString(pos)).setStyle(new Style().setColor(Formatting.LIGHT_PURPLE)));
						return ActionResult.FAIL;
					}
				}
			}
			return ActionResult.PASS;
		});
	}

	private static String friendlyToString(BlockPos pos) {
		return "(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
	}

	private static boolean isPlayerOp(PlayerEntity player) {
		return player.canUseCommand(4, "op");
	}
}
