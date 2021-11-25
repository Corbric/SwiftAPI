package net.swifthq.swiftapi.world;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.AnvilWorldSaveHandler;
import net.minecraft.world.SaveHandler;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import org.apache.logging.log4j.LogManager;

import java.io.File;

public class CustomWorldManager {
    private static int customWorldCount = 0;

    /**
     * if a world exists it will load a world. if it doesnt than it will be created.
     *
     * @param name the name of the world
     * @param seed the seed of the world
     * @param server the minecraftServer
     * @param gameMode the default Gamemode of the world
     * @return the world loaded
     */
    public static ServerWorld loadWorld(String name, int seed, MinecraftServer server, LevelInfo.GameMode gameMode) {
        assert name != null;

        File worldFolder = server.gameDir;
        if ((worldFolder.exists()) && (!worldFolder.isDirectory())) {
            throw new RuntimeException("File exists with the name '" + name + "' and isn't a folder");
        }

        int dimId = 3 + customWorldCount;

        SaveHandler saveHandler = new AnvilWorldSaveHandler(worldFolder, name, true);
        LevelProperties properties = new LevelProperties(new LevelInfo(seed, gameMode, false, false, LevelGeneratorType.FLAT), name);

        ServerWorld world = new ServerWorld(server, saveHandler, properties, dimId, server.profiler);
        server.worlds[3 + customWorldCount] = world;

        LogManager.getLogger("Swift World Loader").info("Preparing level '" + world.getLevelProperties().getLevelName() + "'");
        customWorldCount++;
        world.getWorld();
        return world;
    }

}
