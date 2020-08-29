package net.swifthq.swiftapi.core;

import net.fabricmc.loader.api.FabricLoader;
import net.swifthq.swiftapi.SwiftApi;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class ConfigManager {

    public static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("conf");

    /**
     * Reads a json file from the disk. Will save the default config if no config exists.
     *
     * @param name             name of the config file
     * @param gsonSerialisator what the json will be serialized into
     * @param <T>              the deserializable class
     * @return the json config as the serializable class
     */
    public static <T> T readGson(String name, Class<T> gsonSerialisator) {
        CONFIG_DIR.toFile().mkdirs();
        File configFile = new File(CONFIG_DIR.toAbsolutePath() + "/" + name);
        if (!configFile.exists()) {
            try {
                writeGson(name, gsonSerialisator.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        try {
            String text = FileUtils.readFileToString(CONFIG_DIR.resolve(name).toFile(), StandardCharsets.UTF_8);
            return SwiftApi.createDefaultBuilder().create().fromJson(text, gsonSerialisator);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param name               the name of the file to write to
     * @param serializationClass the class to be serialized to
     * @param <T>                the serializable class
     */
    public static <T> void writeGson(String name, T serializationClass) {
        try {
            CONFIG_DIR.toFile().mkdirs();
            FileWriter writer = new FileWriter(CONFIG_DIR.resolve(name).toFile());
            writer.write(SwiftApi.createDefaultBuilder().create().toJson(serializationClass));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
