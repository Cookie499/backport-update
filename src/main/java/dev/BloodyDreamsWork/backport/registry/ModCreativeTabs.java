package dev.BloodyDreamsWork.backport.registry;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ColorCollection;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class ModCreativeTabs {

    /**
     * Files the backported content into the vanilla creative tabs at the same spots Minecraft 26.3
     * uses, rather than opening a tab of its own. 26.3 adds no new tab - it puts poplar under
     * Building Blocks, the coloured families under Colored Blocks, the plants under Natural Blocks,
     * the signs and the straw bed under Functional Blocks and the boats under Tools & Utilities.
     * Every family is inserted after the entry it follows in 26.3.
     */
    public static void register() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS)
                .register(ModCreativeTabs::buildingBlocks);
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COLORED_BLOCKS)
                .register(ModCreativeTabs::coloredBlocks);
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS)
                .register(ModCreativeTabs::naturalBlocks);
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS)
                .register(ModCreativeTabs::functionalBlocks);
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(ModCreativeTabs::toolsAndUtilities);
    }

    /** Vanilla's own colour order, which 26.3 follows for the coloured families. */
    private static final List<DyeColor> COLOR_ORDER = List.of(
            DyeColor.WHITE, DyeColor.ORANGE, DyeColor.MAGENTA, DyeColor.LIGHT_BLUE,
            DyeColor.YELLOW, DyeColor.LIME, DyeColor.PINK, DyeColor.GRAY,
            DyeColor.LIGHT_GRAY, DyeColor.CYAN, DyeColor.PURPLE, DyeColor.BLUE,
            DyeColor.BROWN, DyeColor.GREEN, DyeColor.RED, DyeColor.BLACK);

    private static void buildingBlocks(FabricCreativeModeTabOutput output) {
        ItemLike previous = Items.PALE_OAK_BUTTON;
        previous = add(output, previous, ModItems.POPLAR_LOG.get());
        previous = add(output, previous, ModItems.POPLAR_WOOD.get());
        previous = add(output, previous, ModItems.STRIPPED_POPLAR_LOG.get());
        previous = add(output, previous, ModItems.STRIPPED_POPLAR_WOOD.get());
        previous = add(output, previous, ModItems.POPLAR_PLANKS.get());
        previous = add(output, previous, ModItems.POPLAR_STAIRS.get());
        previous = add(output, previous, ModItems.POPLAR_SLAB.get());
        previous = add(output, previous, ModItems.POPLAR_FENCE.get());
        previous = add(output, previous, ModItems.POPLAR_FENCE_GATE.get());
        previous = add(output, previous, ModItems.POPLAR_DOOR.get());
        previous = add(output, previous, ModItems.POPLAR_TRAPDOOR.get());
        previous = add(output, previous, ModItems.POPLAR_PRESSURE_PLATE.get());
        add(output, previous, ModItems.POPLAR_BUTTON.get());
    }

    private static void coloredBlocks(FabricCreativeModeTabOutput output) {
        // 26.3 inserts each family as a block of 16 colours, straight after its vanilla counterpart
        ItemLike previous = coloredFamily(output, Items.WOOL, ModItems.WOOL_STAIRS);
        previous = coloredFamily(output, previous, ModItems.WOOL_SLABS);
        previous = coloredFamily(output, previous, ModItems.CONCRETE_STAIRS);
        previous = coloredFamily(output, previous, ModItems.CONCRETE_SLABS);
        coloredFamily(output, previous, ModItems.CUSHIONS);
    }

    private static void naturalBlocks(FabricCreativeModeTabOutput output) {
        ItemLike previous = add(output, Items.PALE_OAK_LOG, ModItems.POPLAR_LOG.get());
        previous = add(output, Items.PALE_OAK_LEAVES, ModItems.RED_POPLAR_LEAVES.get());
        previous = add(output, previous, ModItems.ORANGE_POPLAR_LEAVES.get());
        previous = add(output, previous, ModItems.YELLOW_POPLAR_LEAVES.get());
        previous = add(output, Items.PALE_OAK_SAPLING, ModItems.POPLAR_SAPLING.get());
        previous = add(output, Items.RED_MUSHROOM, ModItems.SHELF_MUSHROOM.get());
        add(output, Items.BUSH, ModItems.RED_SHRUB.get());
    }

    private static void functionalBlocks(FabricCreativeModeTabOutput output) {
        ItemLike previous = add(output, Items.PALE_OAK_HANGING_SIGN, ModItems.POPLAR_SIGN.get());
        previous = add(output, previous, ModItems.POPLAR_HANGING_SIGN.get());
        add(output, previous, ModItems.STRAW_BED.get());
    }

    private static void toolsAndUtilities(FabricCreativeModeTabOutput output) {
        ItemLike previous = add(output, Items.PALE_OAK_CHEST_BOAT, ModItems.POPLAR_BOAT.get());
        add(output, previous, ModItems.POPLAR_CHEST_BOAT.get());
    }

    /** Places one item directly after {@code anchor} and returns it, so families chain in order. */
    private static ItemLike add(FabricCreativeModeTabOutput output, ItemLike anchor, Item item) {
        output.insertAfter(anchor, item);
        return item;
    }

    /**
     * Places one of our coloured families after {@code vanillaFamily}, following vanilla's own
     * colour order. The anchor is the last colour of the vanilla family, matching where 26.3 puts
     * these (its own helper walks gameplayColorOrder for both the vanilla and the poplar entries).
     */
    private static ItemLike coloredFamily(FabricCreativeModeTabOutput output,
                                          ColorCollection<Item> vanillaFamily,
                                          Map<DyeColor, ? extends Supplier<? extends Item>> family) {
        return coloredFamily(output, vanillaFamily.pick(DyeColor.BLACK), family);
    }

    private static ItemLike coloredFamily(FabricCreativeModeTabOutput output, ItemLike anchor,
                                          Map<DyeColor, ? extends Supplier<? extends Item>> family) {
        ItemLike previous = anchor;
        for (DyeColor color : COLOR_ORDER) {
            previous = add(output, previous, family.get(color).get());
        }
        return previous;
    }

    private ModCreativeTabs() {
    }
}
