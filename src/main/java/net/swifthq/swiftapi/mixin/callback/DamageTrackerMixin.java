package net.swifthq.swiftapi.mixin.callback;

import net.swifthq.swiftapi.util.ActionResult;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.swifthq.swiftapi.callbacks.entity.damage.EntityDamageCallback;
import net.swifthq.swiftapi.callbacks.entity.player.damage.PlayerDamageCallback;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageTracker.class)
public class DamageTrackerMixin {

}
