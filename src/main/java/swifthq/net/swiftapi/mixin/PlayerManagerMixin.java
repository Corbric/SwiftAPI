package swifthq.net.swiftapi.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.UserCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import swifthq.net.swiftapi.callbacks.PlayerJoinCallback;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Inject(at=@At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ServerPlayerEntity;listenToContainer()V", shift = At.Shift.AFTER), method = "onPlayerConnect", locals = LocalCapture.CAPTURE_FAILHARD)
    public void playerJoinCallback(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci, GameProfile gameProfile, UserCache userCache, String string, CompoundTag compoundTag, String string2, ServerWorld serverWorld) {
        PlayerJoinCallback.EVENT.invoker().playerJoin(player);
    }
}
