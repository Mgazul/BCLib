package org.betterx.datagen.bclib.tests;

import org.betterx.bclib.api.v3.levelgen.features.BCLFeature;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.RandomPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class TestPlacedFeatures {
    static BCLFeature<RandomPatchFeature, RandomPatchConfiguration> YELLOW_PLACED = TestConfiguredFeatures
            .YELLOW_FEATURE
            .place()
            .count(10)
            .squarePlacement()
            .onHeightmap(Heightmap.Types.WORLD_SURFACE)
            .decoration(GenerationStep.Decoration.VEGETAL_DECORATION)
            .isEmptyAndOn(BlockPredicate.matchesBlocks(Blocks.YELLOW_CONCRETE))
            .onlyInBiome()
            .build();

    public static void bootstrap(BootstrapContext<PlacedFeature> bootstrapContext) {

    }
}
