package org.betterx.bclib.mixin.client;

import org.betterx.bclib.interfaces.UnknownReceipBookCategory;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeHolder;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public abstract class ClientRecipeBookMixin {
    @Inject(method = "getCategory", at = @At("HEAD"), cancellable = true)
    private static void be_getGroupForRecipe(
            RecipeHolder<?> recipe,
            CallbackInfoReturnable<RecipeBookCategories> cir
    ) {
        if (recipe.value() instanceof UnknownReceipBookCategory) {
            cir.setReturnValue(RecipeBookCategories.UNKNOWN);
        }
    }
}
