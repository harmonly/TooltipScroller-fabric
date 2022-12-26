package io.github.harmonly.tooltipscroller;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.slot.Slot;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class ClientTooltipScroller implements ClientModInitializer {

    public static KeyBinding KEY_UP;
    public static KeyBinding KEY_DOWN;

    public static boolean rendering;
    public static Slot slot;
    public static int Y_OFFSET;
    public static int height;
    public static int screenHeight;

    public static void scroll(Type type, int offset) {
        switch (type) {
            case DOWN -> {
                Y_OFFSET -= offset;
                if (Y_OFFSET < screenHeight - height + 2) Y_OFFSET = screenHeight - height + 2;
            }
            case UP -> {
                Y_OFFSET += offset;
                if (Y_OFFSET > 4) Y_OFFSET = 4;
            }
        }
    }

    public static void reset() {
        Y_OFFSET = 4;
        rendering = false;
    }

    @Override
    public void onInitializeClient() {
        rendering = false;
        Y_OFFSET = height = screenHeight = 0;
        KEY_UP = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.tooltipscroller.up",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_PAGE_UP,
                "category.tooltipscroller.title"));
        KEY_DOWN = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.tooltipscroller.down",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_PAGE_DOWN,
                "category.tooltipscroller.title"));
    }
}
