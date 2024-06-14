package org.betterx.bclib.complexmaterials.set.common;

import org.betterx.bclib.complexmaterials.ComplexMaterial;
import org.betterx.bclib.complexmaterials.entry.MaterialSlot;
import org.betterx.bclib.complexmaterials.entry.SimpleMaterialSlot;
import org.betterx.bclib.complexmaterials.set.stone.StoneSlots;
import org.betterx.wover.recipe.api.RecipeBuilder;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractWall<M extends ComplexMaterial> extends SimpleMaterialSlot<M> {
    public AbstractWall() {
        super("wall");
    }

    protected AbstractWall(@NotNull String postfix) {
        super(postfix + "_wall");
    }

    @Override
    protected @Nullable void makeRecipe(RecipeOutput context, ComplexMaterial parentMaterial, ResourceLocation id) {
        RecipeBuilder.crafting(id, parentMaterial.getBlock(suffix))
                     .outputCount(6)
                     .shape("###", "###")
                     .addMaterial('#', parentMaterial.getBlock(StoneSlots.SOURCE))
                     .group("wall")
                     .build(context);
    }

    @Nullable
    protected abstract MaterialSlot<M> getSourceBlockSlot();
}
