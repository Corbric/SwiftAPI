package net.swifthq.swiftapi.mixin.sw;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;
import net.swifthq.swiftapi.sw.SwPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements SwPlayer {

    private final Map<Identifier, Object> nonPersistentData = new HashMap<>();
    private final Map<Identifier, CompoundTag> persistentData = new HashMap<>();

    @Override
    public ServerPlayerEntity getRaw() {
        return (ServerPlayerEntity) (Object) this;
    }

    @Override
    public <T> T getData(Identifier identifier) {
        return (T) nonPersistentData.get(identifier);
    }

    @Override
    public <T> void putData(Identifier identifier, T data) {
        nonPersistentData.put(identifier, data);
    }

    @Override
    public CompoundTag getOrCreatePersistentContainer(Identifier identifier) {
        return persistentData.computeIfAbsent(identifier, $ -> new CompoundTag());
    }

    @Inject(method = "serialize", at = @At("RETURN"))
    private void serialize(CompoundTag tag, CallbackInfo callbackInfo) {
        CompoundTag data = new CompoundTag();

        for (Map.Entry<Identifier, CompoundTag> entry : persistentData.entrySet()) {
            data.put(entry.getKey().toString(), entry.getValue());
        }

        tag.put("SwiftPersistentData", data);
    }

    @Inject(method = "deserialize", at = @At("RETURN"))
    private void deserialize(CompoundTag tag, CallbackInfo callbackInfo) {
        Tag data = tag.get("SwiftPersistentData");
        persistentData.clear();

        if (data instanceof CompoundTag) {
            CompoundTag compoundTag = (CompoundTag) data;

            for (String key : compoundTag.getKeys()) {
                persistentData.put(new Identifier(key), compoundTag.getCompound(key));
            }
        }
    }
}
