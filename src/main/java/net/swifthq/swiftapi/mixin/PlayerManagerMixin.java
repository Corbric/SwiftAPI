package net.swifthq.swiftapi.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.UserCache;
import net.minecraft.util.math.BlockPos;
import net.swifthq.swiftapi.callbacks.entity.player.PlayerJoinCallback;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Shadow
    @Final
    private MinecraftServer server;

    @Shadow
    public abstract void method_6206(Entity entity, int i, ServerWorld serverWorld, ServerWorld serverWorld2);

    @Shadow
    public abstract void method_6204(ServerPlayerEntity player, ServerWorld world);

    @Shadow
    public abstract void sendWorldInfo(ServerPlayerEntity player, ServerWorld world);

    @Shadow
    public abstract void method_6230(ServerPlayerEntity player);

    /**
     * @author hydos
     * @reason because serverside dimensions
     */
    @Overwrite
    public void teleportToDimension(ServerPlayerEntity player, int i) {
        int oldPlayerDimension = player.dimension;
        ServerWorld serverWorld = this.server.getWorld(player.dimension);
        player.dimension = i;
        ServerWorld serverWorld2;
        serverWorld2 = server.getWorld(i);
        player.networkHandler.sendPacket(new PlayerRespawnS2CPacket(0, serverWorld.getGlobalDifficulty(), serverWorld.levelProperties.getGeneratorType(), player.interactionManager.getGameMode()));
        player.networkHandler.sendPacket(new PlayerRespawnS2CPacket(player.dimension, player.world.getGlobalDifficulty(), player.world.levelProperties.getGeneratorType(), player.interactionManager.getGameMode()));
        serverWorld.method_404(player);
        player.removed = false;
        this.method_6206(player, oldPlayerDimension, serverWorld, serverWorld2);
        this.method_6204(player, serverWorld);
        player.networkHandler.requestTeleport(player.x, player.y, player.z, player.yaw, player.pitch);
        player.interactionManager.method_6089(serverWorld2);
        this.sendWorldInfo(player, serverWorld2);
        this.method_6230(player);

        for (StatusEffectInstance effect : player.method_7134()) {
            player.networkHandler.sendPacket(new EntityStatusEffectS2CPacket(player.getEntityId(), effect));
        }
    }

    @Redirect(method = "method_6206", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getForcedSpawnPoint()Lnet/minecraft/util/math/BlockPos;"))
    public BlockPos redirect2ElectiricBoogaloo(ServerWorld world, Entity entity, int i, ServerWorld serverWorld, ServerWorld serverWorld2) {
        if (i > 2) {
            return world.getSpawnPos();
        }
        return new BlockPos(0, 60, 0);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ServerPlayerEntity;listenToContainer()V", shift = At.Shift.AFTER), method = "onPlayerConnect", locals = LocalCapture.CAPTURE_FAILHARD)
    public void playerJoinCallback(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci, GameProfile gameProfile, UserCache userCache, String string, CompoundTag compoundTag, String string2, ServerWorld serverWorld) {
        PlayerJoinCallback.EVENT.invoker().playerJoin(player);
    }
}
