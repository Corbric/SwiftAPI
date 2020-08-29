package net.swifthq.swiftapi.dimension;

import net.minecraft.world.dimension.Dimension;

import java.util.ArrayList;
import java.util.List;

public class DimensionRegistry {

    public static final List<Dimension> FABRIC_DIMENSIONS = new ArrayList<>();

    public int registerDimension(Dimension dimension) {
        FABRIC_DIMENSIONS.add(dimension);

        return 3 + FABRIC_DIMENSIONS.size();
    }
}
