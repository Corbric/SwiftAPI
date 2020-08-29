package net.swifthq.swiftapi.core;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.swifthq.swiftapi.player.Player;

import java.util.HashMap;
import java.util.Map;

public class SwiftManager {

    private static SwiftManager INSTANCE;

    public Map<ServerPlayerEntity, Player> playerMap = new HashMap<>();

    public SwiftManager() {
        INSTANCE = this;
    }

    public static SwiftManager getInstance() {
        return INSTANCE;
    }

    public void addPlayer(ServerPlayerEntity player) {
        playerMap.put(player, new Player(player));
    }

    public boolean isPlayerBusy(Player player) {
        return !player.inBattle;
    }

    public void removePlayer(ServerPlayerEntity player) {
        playerMap.remove(player);
    }
}
