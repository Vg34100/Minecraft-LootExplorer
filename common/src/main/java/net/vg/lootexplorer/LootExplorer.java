package net.vg.lootexplorer;

import dev.architectury.event.events.common.LootEvent;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.vg.lootexplorer.item.ModItemGroups;
import net.vg.lootexplorer.util.LootHandler;
import net.vg.lootexplorer.util.ModKeyMaps;

public final class LootExplorer {

    public static void init() {
        Constants.LOGGER.info("Initializing Loot Explorer");

        // Register the creative tab
        ModItemGroups.register();



        // Register the LootHandler
        LootHandler.register();
        ModKeyMaps.register();

        Constants.LOGGER.info("Loot Explorer initialized");    }
}
