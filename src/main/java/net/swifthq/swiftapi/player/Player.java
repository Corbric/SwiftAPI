package net.swifthq.swiftapi.player;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.Packet;
import net.swifthq.swiftapi.player.inventory.SwiftInventory;

public class Player {

    // TODO: implement a properties system for players so mods can register stuff

    public boolean inBattle;
    public ServerPlayerEntity minecraftPlayer;

    public Player(ServerPlayerEntity minecraftPlayer) {
        this.minecraftPlayer = minecraftPlayer;
    }

    public void showInventory(SwiftInventory inventory) {
        minecraftPlayer.openInventory(inventory);
    }

    public void sendPacket(Packet<?> packet) {
        minecraftPlayer.networkHandler.sendPacket(packet);
    }
}
