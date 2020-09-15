package net.swifthq.swiftapi.mixin.abstraction;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;
import net.swifthq.swiftapi.player.SwPlayer;
import net.swifthq.swiftapi.player.inventory.SwiftInventory;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;
import java.util.*;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements SwPlayer {

    @Shadow
    public abstract void openInventory(Inventory inventory);

    @Shadow
    public ServerPlayNetworkHandler networkHandler;
    private final Map<Identifier, Object> nonPersistentData = new HashMap<>();
    private final Map<Identifier, CompoundTag> persistentData = new HashMap<>();

    @Override
    public ServerPlayerEntity getRaw() {
        return (ServerPlayerEntity) (Object) this;
    }

    @Override
    public <T> T getData(Identifier identifier) {
        return (T) nonPersistentData.get(identifier);
    }

    @Override
    public <T> void putData(Identifier identifier, T data) {
        nonPersistentData.put(identifier, data);
    }

    @Override
    public CompoundTag getOrCreatePersistentContainer(Identifier identifier) {
        return persistentData.computeIfAbsent(identifier, $ -> new CompoundTag());
    }

    @Override
    public void showInventory(SwiftInventory inventory) {
        openInventory(inventory);
    }

    @Override
    public void sendPacket(Packet<?> packet) {
        networkHandler.sendPacket(packet);
    }

    @Inject(method = "serialize", at = @At("RETURN"))
    private void serialize(CompoundTag tag, CallbackInfo callbackInfo) {
        CompoundTag data = new CompoundTag();

        for (Map.Entry<Identifier, CompoundTag> entry : persistentData.entrySet()) {
            data.put(entry.getKey().toString(), entry.getValue());
        }

        tag.put("SwiftPersistentData", data);
    }

    @Inject(method = "deserialize", at = @At("RETURN"))
    private void deserialize(CompoundTag tag, CallbackInfo callbackInfo) {
        Tag data = tag.get("SwiftPersistentData");
        persistentData.clear();

        if (data instanceof CompoundTag) {
            CompoundTag compoundTag = (CompoundTag) data;

            for (String key : compoundTag.getKeys()) {
                persistentData.put(new Identifier(key), compoundTag.getCompound(key));
            }
        }
    }
}
