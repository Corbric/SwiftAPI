package net.swifthq.swiftapi.mixin;

import com.google.common.base.MoreObjects;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockPattern.Result.class)
public class BlockPatternResultMixin {

    @Shadow
    @Final
    private Direction up;

    @Shadow
    @Final
    private Direction forward;

    @Shadow
    @Final
    private BlockPos frontTopLeft;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("up", this.up).add("forwards", this.forward).add("frontTopLeft", this.frontTopLeft).toString();
    }
}
