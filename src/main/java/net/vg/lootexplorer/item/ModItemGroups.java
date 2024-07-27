package net.vg.lootexplorer.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.vg.lootexplorer.LootExplorer;
import net.vg.lootexplorer.util.LootHandler;

public class ModItemGroups {
    // Creating a new ItemGroup called LOOT_GROUP and registering it with the game
    public static final ItemGroup LOOT_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(LootExplorer.MOD_ID, "loot_group"),
            FabricItemGroup.builder().displayName(Text.translatable("item_group.loot_group"))
                    .icon(() -> new ItemStack(Items.CHEST)).entries((displayContext, entries) -> {
                        // Defining the entries (items) that will be in this item group
                        // Building the chests using the LootHandler
                        LootHandler.buildChests();
                        // Adding all the chests from LootHandler to the item group
                        entries.addAll(LootHandler.itemList);
                    }).build());

    // Method to register the item groups, called during the mod initialization
    public static void registerItemGroups() {
        LootExplorer.LOGGER.info("Registering Item Groups for " + LootExplorer.MOD_ID);

    }
}
