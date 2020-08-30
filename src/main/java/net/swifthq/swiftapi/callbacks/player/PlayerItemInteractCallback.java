package net.swifthq.swiftapi.callbacks.player;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface PlayerItemInteractCallback {

    /**
     * This is currently the best way to be able to cancel the login message. any suggestions are open.
     */
    Event<PlayerItemInteractCallback> EVENT = EventFactory.createArrayBacked(PlayerItemInteractCallback.class, (listeners) -> (player, item) -> {
        List<Boolean> results = new ArrayList<>();
        for (PlayerItemInteractCallback callback : listeners) {
            results.add(callback.interactItem(player, item));
        }
        return !results.contains(Boolean.FALSE);
    });

    boolean interactItem(PlayerEntity player, ItemStack item);
}
