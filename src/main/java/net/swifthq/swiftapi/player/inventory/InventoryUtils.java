package net.swifthq.swiftapi.player.inventory;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.c2s.play.ClickWindowC2SPacket;
import net.minecraft.network.packet.s2c.play.ContainerSlotUpdateS2CPacket;

public class InventoryUtils {

    /**
     * used for stopping users from moving items with the cursor
     * @param player the player that is using the inventory
     * @param packet the packet from the {@link net.swifthq.swiftapi.callbacks.ClickContainerCallback}
     */
    public static void cancelCursorPickup(ServerPlayerEntity player, ClickWindowC2SPacket packet){
        player.networkHandler.sendPacket(new ContainerSlotUpdateS2CPacket(-1, -1, player.inventory.getCursorStack()));
        player.networkHandler.sendPacket(new ContainerSlotUpdateS2CPacket(player.openContainer.syncId, packet.getSlot(), player.openContainer.getSlot(packet.getSlot()).getStack()));
    }

}
