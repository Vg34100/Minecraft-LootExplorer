package net.vg.lootexplorer.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyboardHandler {
    private static KeyBinding copyLootTableKey;

    public static void registerKeyboard() {
        // Register the keybinding
        copyLootTableKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.lootexplorer.copy_loot_table",
                GLFW.GLFW_KEY_J, // Keybinding set to 'J'
                "category.lootexplorer"
        ));

        // Check every click if the keybind is pressed
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (copyLootTableKey.wasPressed()) {
                handleCopyLootTable(client);
            }
        });
    }

    /**
     * Copy's the lore of the player's currently held item.
     */
    private static void handleCopyLootTable(MinecraftClient client) {
        if (client.player != null && client.player.getMainHandStack() != null) {
            // Get the item in the player's main hand
            ItemStack mainHandStack = client.player.getMainHandStack();

            // Check if the item has lore
            if (mainHandStack.contains(DataComponentTypes.LORE)) {
                LoreComponent loreComponent = mainHandStack.get(DataComponentTypes.LORE);
                if (loreComponent != null && !loreComponent.lines().isEmpty()) {
                    // Get the first line of the lore
                    Text loreText = loreComponent.lines().getFirst();
                    String loreString = loreText.getString();

                    // Copy the lore text to the clipboard
                    client.keyboard.setClipboard(loreString);

                    // Send a message to the player
                    client.player.sendMessage(Text.literal("Copied loot table name to clipboard: " + loreString), true);
                }
            }
        }
    }
}
