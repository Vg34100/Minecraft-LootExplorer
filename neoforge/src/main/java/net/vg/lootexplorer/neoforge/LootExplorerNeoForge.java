package net.vg.lootexplorer.neoforge;

import net.neoforged.fml.common.Mod;

import net.vg.lootexplorer.Constants;
import net.vg.lootexplorer.LootExplorer;

@Mod(Constants.MOD_ID)
public final class LootExplorerNeoForge {
    public LootExplorerNeoForge() {
        // Run our common setup.
        LootExplorer.init();
    }
}
