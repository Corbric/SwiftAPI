package net.swifthq.swiftapi.player.inventory;

import net.swifthq.swiftapi.util.ActionResult;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.network.packet.c2s.play.ClickWindowC2SPacket;
import net.swifthq.swiftapi.callbacks.container.ClickContainerCallback;
import net.swifthq.swiftapi.callbacks.inventory.InventoryCloseCallback;

public class InventoryUtils {

    /**
     * used for stopping users from moving items with the cursor
     *
     * @param player the player that is using the inventory
     * @param packet the packet from the {@link ClickContainerCallback}
     */
    public static void cancelCursorPickup(ServerPlayerEntity player, ClickWindowC2SPacket packet) {
        player.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(-1, -1, player.inventory.getCursorStack()));
        player.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(player.openScreenHandler.syncId, packet.getSlot(), player.openScreenHandler.getSlot(packet.getSlot()).getStack()));
    }

    /**
     * forces a player out of an {@link SwiftInventory}
     * @param player the player to force out
     */
    public static void closeInventory(ServerPlayerEntity player){
        if(InventoryCloseCallback.EVENT.invoker().onCloseInventory(player) == ActionResult.FAIL) return;
        player.closeHandledScreen();
        player.closeOpenedScreenHandler();
    }

}
