package org.betterx.bclib.recipes;

import org.betterx.bclib.BCLib;
import org.betterx.bclib.util.RecipeHelper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.ItemLike;

public abstract class AbstractDoubleInputRecipeBuilder<T extends AbstractDoubleInputRecipeBuilder, R extends Recipe<? extends RecipeInput>> extends AbstractSingleInputRecipeBuilder<T, R> {
    protected Ingredient secondaryInput;

    protected AbstractDoubleInputRecipeBuilder(
            ResourceLocation id,
            ItemLike output
    ) {
        super(id, output);
    }

    public T setSecondaryInput(ItemLike... inputs) {
        for (ItemLike item : inputs) {
            this.alright &= RecipeHelper.exists(item);
        }
        this.secondaryInput = Ingredient.of(inputs);
        return (T) this;
    }

    public T setSecondaryInput(TagKey<Item> input) {
        this.secondaryInput = Ingredient.of(input);
        return (T) this;
    }

    public T setSecondaryInputAndUnlock(TagKey<Item> input) {
        setPrimaryInput(input);
        this.unlockedBy(input);
        return (T) this;
    }

    public T setSecondaryInputAndUnlock(ItemLike... inputs) {
        setSecondaryInput(inputs);
        for (ItemLike item : inputs) unlockedBy(item);

        return (T) this;
    }

    @Override
    protected boolean checkRecipe() {
        if (secondaryInput == null) {
            BCLib.LOGGER.warn(
                    "Secondary input for Recipe can't be 'null', recipe {} will be ignored!",
                    id
            );
            return false;
        }
        return super.checkRecipe();
    }
}
