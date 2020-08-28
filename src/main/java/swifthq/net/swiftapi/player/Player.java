package swifthq.net.swiftapi.player;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.Packet;
import swifthq.net.swiftapi.player.inventory.SwiftInventory;

public class Player {

    public boolean inBattle;
    public boolean requestsDisabled;
    public ServerPlayerEntity minecraftPlayer;

    public Player(ServerPlayerEntity minecraftPlayer) {
        this.minecraftPlayer = minecraftPlayer;
    }

    public void showInventory(SwiftInventory inventory) {
        minecraftPlayer.openInventory(inventory);
    }

    public void sendPacket(Packet packet) {
        minecraftPlayer.networkHandler.sendPacket(packet);
    }
}
