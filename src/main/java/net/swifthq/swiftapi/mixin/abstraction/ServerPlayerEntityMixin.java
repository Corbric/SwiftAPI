package net.swifthq.swiftapi.mixin.abstraction;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.level.LevelInfo;
import net.swifthq.swiftapi.player.SwPlayer;
import net.swifthq.swiftapi.player.inventory.SwiftInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements SwPlayer {

    public ServerPlayerEntityMixin(World world, GameProfile profile) {
        super(world, profile);
    }

    @Shadow
    public abstract void openInventory(Inventory inventory);

    @Shadow
    public ServerPlayNetworkHandler networkHandler;
    @Shadow @Final public ServerPlayerInteractionManager interactionManager;
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
    public HitResult getLookingAt()  {
        Vec3d playerPos = new Vec3d(this.x, this.y + (double) this.getEyeHeight(), this.z);

        float pitch = this.pitch;
        float yaw = this.yaw;
        float f3 = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
        float f4 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
        float f5 = -MathHelper.cos(-pitch * 0.017453292F);
        float f6 = MathHelper.sin(-pitch * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double reachDistance = this.interactionManager.getGameMode() == LevelInfo.GameMode.CREATIVE ? 5.0D : 4.5D;
        Vec3d endRaytracePoint = playerPos.add((double) f7 * reachDistance, (double) f6 * reachDistance, (double) f8 * reachDistance);
        return this.world.rayTrace(playerPos, endRaytracePoint, false, false, false);
    }

    @Override
    public CompoundTag getOrCreatePersistentContainer(Identifier identifier) {
        return persistentData.computeIfAbsent(identifier, $ -> new CompoundTag());
    }

    @Override
    public void showInventory(SwiftInventory inventory) {
        openInventory(inventory);
    }

    @Override
    public void sendPacket(Packet<?> packet) {
        networkHandler.sendPacket(packet);
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
