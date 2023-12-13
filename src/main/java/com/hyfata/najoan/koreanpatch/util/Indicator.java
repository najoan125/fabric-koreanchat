package com.hyfata.najoan.koreanpatch.util;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.Objects;

public class Indicator {
    static MinecraftClient client = MinecraftClient.getInstance();
    static Text KOREAN = Text.literal("한");
    static Text ENGLISH = Text.literal("영");

    public static void showIndicator(DrawContext context, int x, int y, boolean center) {
        if (center) {
            x -= 6;
            y -= 2;
        }

        if (KoreanPatchClient.KOREAN) {
            context.fill(x, y, x+12, y+12, -65536);
            context.fill(x+1, y+1, x+11, y+11, Objects.requireNonNull(client).options.getTextBackgroundColor(-587202560));
            drawCenteredText(context, KOREAN, x+6, y+2);
        }
        else{
            context.fill(x, y, x+12, y+12, -16711936);
            context.fill(x+1, y+1, x+11, y+11, Objects.requireNonNull(client).options.getTextBackgroundColor(-587202560));
            drawCenteredText(context, ENGLISH, x+6, y+2);
        }
    }

    private static void drawCenteredText(DrawContext context, Text text, int x, int y) {
        context.drawText(client.textRenderer, text, x - client.textRenderer.getWidth(text) / 2, y, 16777215, false);
    }
}
