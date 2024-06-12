package org.betterx.datagen.bclib.tests;

import org.betterx.bclib.BCLib;
import org.betterx.bclib.api.v3.levelgen.features.BCLConfigureFeature;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeatureBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.RandomPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

public class TestConfiguredFeatures {
    static BCLConfigureFeature<RandomPatchFeature, RandomPatchConfiguration> YELLOW_FEATURE = BCLFeatureBuilder
            .startWeighted(BCLib.makeID("temp_yellow_feature"))
            .add(Blocks.YELLOW_STAINED_GLASS, 30)
            .add(Blocks.YELLOW_CONCRETE_POWDER, 30)
            .add(Blocks.YELLOW_GLAZED_TERRACOTTA, 5)
            .inlinePlace()
            .isEmpty()
            .inRandomPatch(BCLib.makeID("yellow_feature"))
            .build();

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> bootstrapContext) {
        Holder<ConfiguredFeature<?, ?>> holder = bootstrapContext.lookup(Registries.CONFIGURED_FEATURE)
                                                                 .getOrThrow(ResourceKey.create(
                                                                         Registries.CONFIGURED_FEATURE,
                                                                         YELLOW_FEATURE.id
                                                                 ));
    }
}
