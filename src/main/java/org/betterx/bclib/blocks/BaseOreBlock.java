package org.betterx.bclib.blocks;

import org.betterx.bclib.behaviours.BehaviourBuilders;
import org.betterx.bclib.behaviours.interfaces.BehaviourOre;
import org.betterx.bclib.interfaces.BlockModelProvider;
import org.betterx.bclib.util.LegacyTiers;
import org.betterx.bclib.util.LootUtil;
import org.betterx.bclib.util.MHelper;
import org.betterx.wover.block.api.BlockTagProvider;
import org.betterx.wover.tag.api.event.context.TagBootstrapContext;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class BaseOreBlock extends DropExperienceBlock implements BlockModelProvider, BlockTagProvider, BehaviourOre {
    private final Supplier<Item> dropItem;
    private final int minCount;
    private final int maxCount;
    private final TagKey<Block> miningTag;

    public BaseOreBlock(Supplier<Item> drop, int minCount, int maxCount, int experience) {
        this(drop, minCount, maxCount, experience, null);
    }

    public BaseOreBlock(Supplier<Item> drop, int minCount, int maxCount, int experience, TagKey<Block> miningTag) {
        this(
                BehaviourBuilders
                        .createStone(MapColor.SAND)
                        .requiresCorrectToolForDrops()
                        .destroyTime(3F)
                        .explosionResistance(9F)
                        .sound(SoundType.STONE),
                drop, minCount, maxCount, experience, miningTag
        );
    }

    public BaseOreBlock(Properties properties, Supplier<Item> drop, int minCount, int maxCount, int experience) {
        this(properties, drop, minCount, maxCount, experience, null);
    }

    public BaseOreBlock(
            Properties properties,
            Supplier<Item> drop,
            int minCount,
            int maxCount,
            int experience,
            TagKey<Block> miningTag
    ) {
        super(UniformInt.of(experience > 0 ? 1 : 0, experience), properties);
        this.dropItem = drop;
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.miningTag = miningTag;
    }

    @Deprecated(forRemoval = true)
    public BaseOreBlock(Supplier<Item> drop, int minCount, int maxCount, int experience, int miningLevel) {
        this(
                BehaviourBuilders
                        .createStone(MapColor.SAND)
                        .requiresCorrectToolForDrops()
                        .destroyTime(3F)
                        .explosionResistance(9F)
                        .sound(SoundType.STONE),
                drop, minCount, maxCount, experience, miningLevel
        );
    }

    @Deprecated(forRemoval = true)
    public BaseOreBlock(
            Properties properties,
            Supplier<Item> drop,
            int minCount,
            int maxCount,
            int experience,
            int miningLevel
    ) {
        this(properties, drop, minCount, maxCount, experience, LegacyTiers
                .forLevel(miningLevel)
                .map(t -> t.toolRequirementTag)
                .orElse(null));
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return LootUtil
                .getDrops(this, state, builder)
                .orElseGet(
                        () -> BaseOreBlock.getDroppedItems(
                                this,
                                dropItem.get(),
                                maxCount,
                                minCount,
                                miningTag,
                                state,
                                builder
                        )
                );
    }

    public static List<ItemStack> getDroppedItems(
            ItemLike block,
            Item dropItem,
            int maxCount,
            int minCount,
            TagKey<Block> miningTag,
            BlockState state,
            LootParams.Builder builder
    ) {
        ItemStack tool = builder.getParameter(LootContextParams.TOOL);
        if (tool != null && tool.isCorrectToolForDrops(state) && dropItem != null) {
            boolean canMine = miningTag == null;
            if (tool.getItem() instanceof TieredItem tired) {
                canMine = tool.isCorrectToolForDrops(state);
                //canMine = tired.getTier().getLevel() >= miningLevel;
            }
            if (canMine) {
                if (EnchantmentHelper.getItemEnchantmentLevel(new Holder.Direct(Enchantments.SILK_TOUCH), tool) > 0) {
                    return Collections.singletonList(new ItemStack(block));
                }
                int count;
                int enchantment = EnchantmentHelper.getItemEnchantmentLevel(new Holder.Direct(Enchantments.FORTUNE), tool);
                if (enchantment > 0) {
                    int min = Mth.clamp(minCount + enchantment, minCount, maxCount);
                    //int max = maxCount + (enchantment / Enchantments.FORTUNE.getMaxLevel());
                    int max = maxCount + (enchantment / 2);
                    if (min == max) {
                        return Collections.singletonList(new ItemStack(dropItem, max));
                    }
                    count = MHelper.randRange(min, max, MHelper.RANDOM_SOURCE);
                } else {
                    count = MHelper.randRange(minCount, maxCount, MHelper.RANDOM_SOURCE);
                }
                return Collections.singletonList(new ItemStack(dropItem, count));
            }
        }
        return Collections.emptyList();
    }

    @Override
    public BlockModel getItemModel(ResourceLocation resourceLocation) {
        return getBlockModel(resourceLocation, defaultBlockState());
    }


    @Override
    public void registerItemTags(ResourceLocation location, TagBootstrapContext<Block> context) {
        if (this.miningTag != null) {
            context.add(this.miningTag, this);
        }
    }
}
