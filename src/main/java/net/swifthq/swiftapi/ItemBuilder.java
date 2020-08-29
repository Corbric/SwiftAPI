package net.swifthq.swiftapi;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

/**
 * used to do item building
 */
public class ItemBuilder {

    /**
     * used to apply the builder to an itemstack
     *
     * @param stack   the stack to apply to
     * @param builder the builder to apply
     * @return the applied itemstack
     */
    public static ItemStack apply(ItemStack stack, Builder builder) {
        stack.setCustomName(builder.name);
        return stack;
    }

    /**
     * sets the colour of a string
     *
     * @param text  the text
     * @param style the colour/style to set
     * @return the coloured string
     */
    public static String colourString(String text, Style style) {
        return style.getColor() +
                text;
    }

    /**
     * used to get a builder instance
     *
     * @return a builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        public String name;

        private Builder() {
        }

        /**
         * sets the name in the builder
         *
         * @param name the name
         * @return the builder
         */
        public Builder setName(String name) {
            this.name = name;
            return this;
        }


        /**
         * set the colour of the builder
         *
         * @param formatting the colour
         * @return the builder
         */
        public Builder setColour(Formatting formatting) {
            this.name = colourString(name, new Style().setColor(formatting));
            return this;
        }

        /**
         * builds the builder to an itemstack
         *
         * @param stack the itemstack to build on
         * @return the built itemstack
         */
        public ItemStack build(ItemStack stack) {
            return apply(stack, this);
        }
    }

}
