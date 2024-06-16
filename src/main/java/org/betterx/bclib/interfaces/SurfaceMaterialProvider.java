package org.betterx.bclib.interfaces;

import org.betterx.bclib.api.v2.levelgen.surface.SurfaceRuleBuilder;
import org.betterx.wover.tag.api.event.context.TagBootstrapContext;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface SurfaceMaterialProvider {
    MapCodec<SurfaceMaterialProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    BlockState.CODEC.fieldOf("top").forGetter(o -> o.getTopMaterial()),
                    BlockState.CODEC.fieldOf("under").forGetter(o -> o.getUnderMaterial()),
                    BlockState.CODEC.fieldOf("alt").forGetter(o -> o.getAltTopMaterial()),
                    Codec.BOOL.fieldOf("floor_rule").forGetter(o -> o.generateFloorRule())
            ).apply(instance, SurfaceMaterialProvider::create));

    public static SurfaceMaterialProvider create(
            BlockState top,
            BlockState under,
            BlockState alt,
            boolean genFloorRule
    ) {
        return new SurfaceMaterialProvider() {
            @Override
            public BlockState getTopMaterial() {
                return top;
            }

            @Override
            public BlockState getUnderMaterial() {
                return under;
            }

            @Override
            public BlockState getAltTopMaterial() {
                return alt;
            }

            @Override
            public boolean generateFloorRule() {
                return genFloorRule;
            }

            @Override
            public SurfaceRuleBuilder surface() {
                return null;
            }
        };
    }

    BlockState getTopMaterial();
    BlockState getUnderMaterial();
    BlockState getAltTopMaterial();

    boolean generateFloorRule();
    SurfaceRuleBuilder surface();

    default void addBiomeSurfaceToEndGroup(TagBootstrapContext<Block> context, TagKey<Block> groundTag) {
        context.add(groundTag, this.getTopMaterial().getBlock());
        context.add(groundTag, this.getAltTopMaterial().getBlock());
        context.add(groundTag, this.getUnderMaterial().getBlock());
    }
}
