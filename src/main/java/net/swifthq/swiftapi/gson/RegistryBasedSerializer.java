package net.swifthq.swiftapi.gson;

import com.google.gson.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;

import java.lang.reflect.Type;

public class RegistryBasedSerializer<E> implements JsonSerializer<E>, JsonDeserializer<E> {

    private final SimpleRegistry<Identifier, E> registry;

    public RegistryBasedSerializer(SimpleRegistry<Identifier, E> registry) {
        this.registry = registry;
    }

    @Override
    public E deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return registry.get(context.deserialize(json, Identifier.class));
    }

    @Override
    public JsonElement serialize(E src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(registry.getIdentifier(src));
    }
}
