package com.hyfata.najoan.koreanpatch.client;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.win32.StdCallLibrary;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(value=EnvType.CLIENT)
public class KoreanPatchClient
implements ClientModInitializer {
    public static boolean KOREAN = false;
    public static boolean SEARCH = false;
    public static int KEYCODE = 346;
    public static int SCANCODE = 498;

    public void onInitializeClient() {
        if (Platform.isWindows()) {
            Imm32.INSTANCE.ImmDisableIME(-1);
            KEYCODE = GLFW.GLFW_KEY_RIGHT_ALT;
        }
        else if (Platform.isMac()) {
            KEYCODE = GLFW.GLFW_KEY_RIGHT_ALT;
        }


    }

    public static interface Imm32
    extends StdCallLibrary {
        public static final Imm32 INSTANCE = (Imm32)Native.load((String)"Imm32", Imm32.class);

        public boolean ImmDisableIME(int var1);
    }
}

