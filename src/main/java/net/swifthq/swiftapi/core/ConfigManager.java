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

    public static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("config");

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

    public static <T> Optional<T> read(String name, Type type) {
        try (BufferedReader reader = Files.newBufferedReader(CONFIG_DIR.resolve(name + ".json"))) {
            return Optional.of(SwiftApi.GSON.fromJson(reader, type));
        } catch (Throwable ignored) {
            return Optional.empty();
        }
    }
}
