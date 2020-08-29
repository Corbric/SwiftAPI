package net.swifthq.swiftapi.core;

import net.fabricmc.loader.api.FabricLoader;
import net.swifthq.swiftapi.SwiftApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class ConfigManager {

    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("config");

    /**
     * Writes a serializable data class to the standard configuration directory
     *
     * @param name     The name of the file to write to
     * @param instance The instance to write
     * @param <T>      The type of the instance
     */
    public static <T> void write(String name, T instance) {
        try {
            Path path = CONFIG_DIR.resolve(name + ".json");
            Files.createDirectories(path.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                SwiftApi.GSON.toJson(instance, writer);
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Reads a configuration from the standard configuration directory
     *
     * @param name The name of the file to read from
     * @param type The type of the config
     * @param <T>  The type of the config
     * @return An Optional of the file if read, or empty if the file could not be read or parsed
     */
    public static <T> Optional<T> read(String name, Type type) {
        try (BufferedReader reader = Files.newBufferedReader(CONFIG_DIR.resolve(name + ".json"))) {
            return Optional.of(SwiftApi.GSON.fromJson(reader, type));
        } catch (Throwable ignored) {
            return Optional.empty();
        }
    }

    /**
     * See {@link #read(String, Type)}. This method writes and returns a default config if none could be loaded
     *
     * @param name            The name of the file to read from
     * @param defaultInstance The default instance to write and return
     * @param <T>             The type of the config
     * @return The configuration on the file system, or the default instance
     */
    public static <T> T readWithDefault(String name, T defaultInstance) {
        return ConfigManager.<T>read(name, defaultInstance.getClass())
                .orElseGet(() -> {
                    write(name, defaultInstance);
                    return defaultInstance;
                });
    }
}
