package org.betterx.bclib.mixin.common;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;

import org.betterx.bclib.BCLib;
import org.betterx.bclib.interfaces.BiomeSourceAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;

@Mixin(BiomeSource.class)
public abstract class BiomeSourceMixin implements BiomeSourceAccessor {

    @Shadow
    public abstract Set<Biome> possibleBiomes();

    public void bclRebuildFeatures() {
        //Feature sorting is now a task in ChunkGenerator
        BCLib.LOGGER.info("Rebuilding features in BiomeSource " + this);
        //featuresPerStep = Suppliers.memoize(() -> FeatureSorter.buildFeaturesPerStep(this.possibleBiomes().stream().toList(), true));
    }

    @Inject(method = "<init>(Ljava/util/List;)V", at = @At("TAIL"))
    public void bcl_init(List list, CallbackInfo ci) {
        System.out.println("new BiomeSource (" + Integer.toHexString(hashCode()) + ", biomes=" + possibleBiomes().size() + ")");
        if (possibleBiomes().size() == 27) {
            System.out.println("Nether????");
        } else if (possibleBiomes().size() == 2) {
            System.out.println("Datapack Nether???");
        }
    }
}