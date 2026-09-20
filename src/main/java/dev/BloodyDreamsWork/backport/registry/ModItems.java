package dev.BloodyDreamsWork.backport.registry;

import dev.BloodyDreamsWork.backport.Backport;
import dev.BloodyDreamsWork.backport.content.CushionItem;
import dev.BloodyDreamsWork.backport.content.ExplorerMapItem;
import dev.BloodyDreamsWork.backport.content.ExplorerMapType;
import net.minecraft.world.item.BlockItem;
import dev.BloodyDreamsWork.backport.content.PoplarBoatItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;

public final class ModItems {
    public static final ModRegister<Item> ITEMS = ModRegister.create(BuiltInRegistries.ITEM);

    private static ModRegister.Entry<BlockItem> simpleBlockItem(ModRegister.Entry<? extends Block> block) {
        return ITEMS.register(block.getId().getPath(),
                // Vanilla registers every block item through Items.registerBlock, which applies
                // useBlockDescriptionPrefix(). Without it Item.Properties defaults to the item.
                // prefix, so a BlockItem looks up item.backport.<name> while the language files
                // (and the block itself) use block.backport.<name> - and the raw key shows in game.
                () -> new BlockItem(block.get(), new Item.Properties().useBlockDescriptionPrefix()));
    }

    public static final ModRegister.Entry<BlockItem> POPLAR_LOG = simpleBlockItem(ModBlocks.POPLAR_LOG);
    public static final ModRegister.Entry<BlockItem> POPLAR_WOOD = simpleBlockItem(ModBlocks.POPLAR_WOOD);
    public static final ModRegister.Entry<BlockItem> STRIPPED_POPLAR_LOG = simpleBlockItem(ModBlocks.STRIPPED_POPLAR_LOG);
    public static final ModRegister.Entry<BlockItem> STRIPPED_POPLAR_WOOD = simpleBlockItem(ModBlocks.STRIPPED_POPLAR_WOOD);
    public static final ModRegister.Entry<BlockItem> POPLAR_PLANKS = simpleBlockItem(ModBlocks.POPLAR_PLANKS);
    public static final ModRegister.Entry<BlockItem> POPLAR_STAIRS = simpleBlockItem(ModBlocks.POPLAR_STAIRS);
    public static final ModRegister.Entry<BlockItem> POPLAR_SLAB = simpleBlockItem(ModBlocks.POPLAR_SLAB);
    public static final ModRegister.Entry<BlockItem> POPLAR_FENCE = simpleBlockItem(ModBlocks.POPLAR_FENCE);
    public static final ModRegister.Entry<BlockItem> POPLAR_FENCE_GATE = simpleBlockItem(ModBlocks.POPLAR_FENCE_GATE);
    public static final ModRegister.Entry<BlockItem> POPLAR_PRESSURE_PLATE = simpleBlockItem(ModBlocks.POPLAR_PRESSURE_PLATE);
    public static final ModRegister.Entry<BlockItem> POPLAR_BUTTON = simpleBlockItem(ModBlocks.POPLAR_BUTTON);
    public static final ModRegister.Entry<BlockItem> POPLAR_TRAPDOOR = simpleBlockItem(ModBlocks.POPLAR_TRAPDOOR);
    public static final ModRegister.Entry<BlockItem> POPLAR_DOOR = simpleBlockItem(ModBlocks.POPLAR_DOOR);

    public static final ModRegister.Entry<BlockItem> RED_POPLAR_LEAVES = simpleBlockItem(ModBlocks.RED_POPLAR_LEAVES);
    public static final ModRegister.Entry<BlockItem> ORANGE_POPLAR_LEAVES = simpleBlockItem(ModBlocks.ORANGE_POPLAR_LEAVES);
    public static final ModRegister.Entry<BlockItem> YELLOW_POPLAR_LEAVES = simpleBlockItem(ModBlocks.YELLOW_POPLAR_LEAVES);

    public static final ModRegister.Entry<BlockItem> POPLAR_SAPLING = simpleBlockItem(ModBlocks.POPLAR_SAPLING);

    public static final ModRegister.Entry<SignItem> POPLAR_SIGN = ITEMS.register("poplar_sign",
            () -> new SignItem(ModBlocks.POPLAR_SIGN.get(), ModBlocks.POPLAR_WALL_SIGN.get(),
                    new Item.Properties().stacksTo(16)));

    public static final ModRegister.Entry<HangingSignItem> POPLAR_HANGING_SIGN = ITEMS.register("poplar_hanging_sign",
            () -> new HangingSignItem(ModBlocks.POPLAR_HANGING_SIGN.get(), ModBlocks.POPLAR_WALL_HANGING_SIGN.get(),
                    new Item.Properties().stacksTo(16)));

    public static final ModRegister.Entry<BlockItem> SHELF_MUSHROOM = simpleBlockItem(ModBlocks.SHELF_MUSHROOM);
    public static final ModRegister.Entry<BlockItem> RED_SHRUB = simpleBlockItem(ModBlocks.RED_SHRUB);

    public static final ModRegister.Entry<PoplarBoatItem> POPLAR_BOAT = ITEMS.register("poplar_boat",
            () -> new PoplarBoatItem(false, new Item.Properties().stacksTo(1)));

    public static final ModRegister.Entry<PoplarBoatItem> POPLAR_CHEST_BOAT = ITEMS.register("poplar_chest_boat",
            () -> new PoplarBoatItem(true, new Item.Properties().stacksTo(1)));

    public static final ModRegister.Entry<BlockItem> STRAW_BED = simpleBlockItem(ModBlocks.STRAW_BED);

    public static final Map<DyeColor, ModRegister.Entry<CushionItem>> CUSHIONS = cushions();

    public static final Map<ExplorerMapType, ModRegister.Entry<ExplorerMapItem>> EXPLORER_MAPS = explorerMaps();

    public static final Map<DyeColor, ModRegister.Entry<BlockItem>> WOOL_STAIRS =
            blockItems(ModBlocks.WOOL_STAIRS);
    public static final Map<DyeColor, ModRegister.Entry<BlockItem>> WOOL_SLABS =
            blockItems(ModBlocks.WOOL_SLABS);
    public static final Map<DyeColor, ModRegister.Entry<BlockItem>> CONCRETE_STAIRS =
            blockItems(ModBlocks.CONCRETE_STAIRS);
    public static final Map<DyeColor, ModRegister.Entry<BlockItem>> CONCRETE_SLABS =
            blockItems(ModBlocks.CONCRETE_SLABS);

    private static Map<ExplorerMapType, ModRegister.Entry<ExplorerMapItem>> explorerMaps() {
        Map<ExplorerMapType, ModRegister.Entry<ExplorerMapItem>> result = new LinkedHashMap<>();
        for (ExplorerMapType type : ExplorerMapType.values()) {
            result.put(type, ITEMS.register(type.itemName(),
                    () -> new ExplorerMapItem(type, new Item.Properties().stacksTo(1))));
        }
        return Collections.unmodifiableMap(result);
    }

    private static Map<DyeColor, ModRegister.Entry<CushionItem>> cushions() {
        Map<DyeColor, ModRegister.Entry<CushionItem>> result = new LinkedHashMap<>();
        for (DyeColor color : DyeColor.values()) {
            result.put(color, ITEMS.register(color.getName() + "_cushion",
                    () -> new CushionItem(color, new Item.Properties())));
        }
        return Collections.unmodifiableMap(result);
    }

    private static <T extends Block> Map<DyeColor, ModRegister.Entry<BlockItem>> blockItems(
            Map<DyeColor, ModRegister.Entry<T>> blocks) {
        Map<DyeColor, ModRegister.Entry<BlockItem>> result = new LinkedHashMap<>();
        blocks.forEach((color, block) -> result.put(color, simpleBlockItem(block)));
        return Collections.unmodifiableMap(result);
    }

    public static void register() {
    }

    private ModItems() {
    }
}
