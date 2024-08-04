package com.hyfata.najoan.koreanpatch.client;

import com.hyfata.najoan.koreanpatch.plugin.InputController;
import com.hyfata.najoan.koreanpatch.plugin.InputManager;
import com.hyfata.najoan.koreanpatch.util.ModLogger;
import com.hyfata.najoan.koreanpatch.util.ReflectionFieldChecker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.JigsawBlockScreen;
import net.minecraft.client.gui.screen.ingame.StructureBlockScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.SelectionManager;

import java.util.ArrayList;
import java.util.Arrays;

public class EventListener {
    private static ArrayList<Class<?>> patchedScreenClazz = new ArrayList<>();

    protected static void onClientStarted(MinecraftClient client) {
        InputManager.applyController(InputController.newController());

        String[] patchedScreens = {
                "arm32x.minecraft.commandblockide.client.gui.screen.CommandIDEScreen"
        };
        patchedScreenClazz = getExistingClasses(patchedScreens);
    }

    protected static void afterScreenChange(MinecraftClient client, Screen screen, int scaledWidth, int scaledHeight) {
        if (client.currentScreen != null) {
            ModLogger.debug("Current screen: " + client.currentScreen);

            // injection bypass screens
            Class<?>[] bypassScreens = {JigsawBlockScreen.class, StructureBlockScreen.class};
            KoreanPatchClient.bypassInjection = Arrays.stream(bypassScreens)
                    .anyMatch(cls -> cls.isInstance(client.currentScreen));

            // IME set focus
            boolean screenPatched = false;
            for (Class<?> cls : patchedScreenClazz) {
                if (cls.isInstance(client.currentScreen)) {
                    screenPatched = true;
                    break;
                }
            }

            if (InputManager.getController() != null) {
                if (!screenPatched) {
                    boolean hasTextFieldWidget = ReflectionFieldChecker.hasFieldOfType(client.currentScreen, TextFieldWidget.class);
                    boolean hasSelectionManager = ReflectionFieldChecker.hasFieldOfType(client.currentScreen, SelectionManager.class);
                    InputManager.getController().setFocus(!hasTextFieldWidget && !hasSelectionManager);
                } else {
                    InputManager.getController().setFocus(false);
                }
            }
        }
    }

    private static ArrayList<Class<?>> getExistingClasses(String[] clazz) {
        ArrayList<Class<?>> result = new ArrayList<>();
        for (String className : clazz) {
            try {
                Class<?> cls = Class.forName(className);
                result.add(cls);
            } catch (ClassNotFoundException ignored) {}
        }
        return result;
    }

    protected static void onClientTick(MinecraftClient client) {
        if (InputManager.getController() == null) return;

        if (client.currentScreen == null) {
            InputManager.getController().setFocus(false);
        }
    }
}
