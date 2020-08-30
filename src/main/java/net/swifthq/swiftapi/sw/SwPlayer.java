package net.swifthq.swiftapi.sw;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

public interface SwPlayer {

    static SwPlayer from(ServerPlayerEntity playerEntity) {
        return (SwPlayer) playerEntity;
    }

    /**
     * Gets the player this is wrapping
     *
     * @return The internal player
     */
    ServerPlayerEntity getRaw();

    /**
     * Gets data which was assigned to this player. This is not persistent
     *
     * @param identifier Identifier of this data
     * @param <T>        Data type
     * @return Data found, not <code>null</code> if none was found by this identifier
     */
    <T> T getData(Identifier identifier);

    /**
     * Puts data which is assigned to this player. This is not persistent
     *
     * @param identifier Identifier of the data
     * @param data       The data being set
     * @param <T>        Data type
     */
    <T> void putData(Identifier identifier, T data);

    /**
     * Access to the persistent data, which is stored between saves and restarts
     *
     * @param identifier The identifier of the data container
     * @return The data container
     */
    CompoundTag getOrCreatePersistentContainer(Identifier identifier);
}
