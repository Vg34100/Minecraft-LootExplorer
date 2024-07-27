package net.vg.lootexplorer;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.vg.lootexplorer.item.ModItemGroups;
import net.vg.lootexplorer.util.KeyboardHandler;
import net.vg.lootexplorer.util.LootHandler;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class LootExplorer implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("lootexplorer");

	// Constants for mod information.
	public static final String MOD_ID = "lootexplorer";
	public static final String MOD_NAME = "Loot Explorer";
	public static final String MOD_VERSION = fetchModVersion();


	/**
	 * This method is called when Minecraft is ready to load mods.
	 * It initializes the mod by registering configurations and block entities.
	 */
	@Override
	public void onInitialize() {
		LOGGER.info("Initailizing Mod: {} v{}", MOD_ID, MOD_VERSION);

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new LootHandler());
		ModItemGroups.registerItemGroups();
		KeyboardHandler.registerKeyboard();



		LOGGER.info("Initialized Mod: {} v{}", MOD_NAME, MOD_VERSION);
	}

	/**
	 * Fetches the mod version from the mod metadata.
	 *
	 * @return The version of the mod as a String.
	 */
	private static String fetchModVersion() {
		// Attempt to get the mod container from the FabricLoader
		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(MOD_ID);

		// If the mod container is found, return the version from the metadata.
		// Otherwise, return a default version "1.0.0".
		return modContainer.map(container -> container.getMetadata().getVersion().getFriendlyString()).orElse("1.0.0");
	}
}