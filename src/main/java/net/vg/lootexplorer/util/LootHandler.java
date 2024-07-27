package net.vg.lootexplorer.util;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerLootComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.vg.lootexplorer.LootExplorer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class LootHandler implements SimpleSynchronousResourceReloadListener {
    // Lists to store loot table identifiers and generated item stacks.
    public static List<String> tables = new ArrayList<>();
    public static List<ItemStack> itemList = new ArrayList<>();

    @Override
    public Identifier getFabricId() {
        return Identifier.of(LootExplorer.MOD_ID, "loot_handler");
    }

    @Override
    public void reload(ResourceManager resourceManager) {
        // Clear table to remove old resources
        tables.clear();

        Predicate<Identifier> filter = id -> id.getPath().startsWith("loot_table/chests") || id.getPath().startsWith("loot_table/loot");
        // Find resources using the filter
        Map<Identifier, Resource> resources = resourceManager.findResources("loot_table", filter);
        resources.forEach((id, resource) -> {
            // Log the loot table identifiers
            LootExplorer.LOGGER.info("Found loot table: " + id.toString());
            if (!tables.contains(id.toString())) {
                tables.add(id.toString());
            }
        });
    }

    public static void buildChests() {
        itemList.clear();
        int counter = 1;

        for (final String table : tables) {
            for (ItemStack chestItem : List.of(new ItemStack(Items.CHEST), new ItemStack(Items.BARREL), new ItemStack(Items.TRAPPED_CHEST))) {
                try {
                    String tableName = table.toString().replace(".json", "").replace("loot_table/", ""); // Remove .json extension

                    Identifier lootTableIdentifier = Identifier.of(tableName);
                    RegistryKey<LootTable> lootTableKey = RegistryKey.of(RegistryKeys.LOOT_TABLE, lootTableIdentifier);

                    // Setting the Loot Table within the container
                    ContainerLootComponent lootComponent = new ContainerLootComponent(lootTableKey, 0); // Seed of 0 generates randomly
                    chestItem.set(DataComponentTypes.CONTAINER_LOOT, lootComponent);

                    // Determine the color of the lore based on the table name
                    Text loreText;
                    if (table.contains("minecraft:")) {
                        loreText = Text.literal(tableName); // Default
                    } else {
                        loreText = Text.literal(tableName).setStyle(Style.EMPTY.withColor(0x55FF55)); // Green color
                    }

                    // Set the lore component
                    LoreComponent loreComponent = new LoreComponent(List.of(loreText));
                    chestItem.set(DataComponentTypes.LORE, loreComponent);

                    // Determine the item type for the custom name using translation keys
                    String translationKey = chestItem.getTranslationKey();
                    Text itemTypeName = Text.translatable(translationKey);

                    // Set the custom name for the chest item
                    Text customName = Text.literal(itemTypeName.getString() + " (#" + String.format("%04d", counter) + ")");
                    chestItem.set(DataComponentTypes.ITEM_NAME, customName);


                    // Add the chest stack to the item list
                    itemList.add(chestItem);
                } catch (Exception e) {
                    LootExplorer.LOGGER.error("Error building chest for loot table " + table, e);
                }
            }
            // Increment the counter for the next chest item
            counter++;
        }
    }
}
