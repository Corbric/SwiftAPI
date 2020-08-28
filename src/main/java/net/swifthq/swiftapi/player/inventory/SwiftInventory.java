package net.swifthq.swiftapi.player.inventory;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

public abstract class SwiftInventory implements Inventory {

    public static final ItemStack EMPTY = new ItemStack(Blocks.AIR);

    private final ItemStack[] items;
    private final List<PlayerEntity> viewers;
    private final String title;

    public SwiftInventory(int size, String title) {
        Validate.notNull(title, "Title cannot be null");
        this.items = new ItemStack[size];
        this.title = title;
        this.viewers = new ArrayList<>();
    }

    public static ItemStack copyNMSStack(ItemStack original, int amount) {
        ItemStack stack = original.copy();
        stack.count = amount;
        return stack;
    }

    @Override
    public int getInvSize() {
        return items.length;
    }

    @Override
    public ItemStack getInvStack(int slot) {
        return items[slot];
    }

    @Override
    public ItemStack takeInvStack(int slot, int amount) {
        ItemStack stack = this.getInvStack(slot);
        ItemStack result;
        if (stack == null) return null;
        if (stack.count <= amount) {
            this.setInvStack(slot, null);
            result = stack;
        } else {
            result = copyNMSStack(stack, amount);
            stack.count -= amount;
        }
        this.markDirty();
        return result;
    }

    @Override
    public ItemStack removeInvStack(int slot) {
        items[slot] = EMPTY;
        return EMPTY;
    }

    @Override
    public void setInvStack(int slot, ItemStack stack) {
        items[slot] = stack;
    }

    @Override
    public int getInvMaxStackAmount() {
        return 0;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean canPlayerUseInv(PlayerEntity player) {
        return true;
    }

    @Override
    public void onInvOpen(PlayerEntity player) {
        viewers.add(player);
    }

    @Override
    public void onInvClose(PlayerEntity player) {
        viewers.remove(player);
    }

    @Override
    public boolean isValidInvStack(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public void setProperty(int id, int value) {
    }

    @Override
    public void clear() {}

    @Override
    public int getProperty(int key) {
        return 0;
    }

    @Override
    public int getProperties() {
        return 0;
    }

    @Override
    public String getTranslationKey() {
        return title;
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public Text getName() {
        return new LiteralText(title);
    }
}
