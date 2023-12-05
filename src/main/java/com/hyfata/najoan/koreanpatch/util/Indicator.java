package com.hyfata.najoan.koreanpatch.util;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.Objects;

public class Indicator {
    public static void showIndicator(DrawContext context, int x, int y, boolean center) {
        MinecraftClient client = MinecraftClient.getInstance();
        Text KOREAN = Text.literal("한");
        Text ENGLISH = Text.literal("영");

        if (center) {
            x -= 6;
            y -= 2;
        }

        if (KoreanPatchClient.KOREAN) {
            context.fill(x, y, x+12, y+12, -65536);
            context.fill(x+1, y+1, x+11, y+11, Objects.requireNonNull(client).options.getTextBackgroundColor(-587202560));
            context.drawCenteredTextWithShadow(client.textRenderer, KOREAN, x+6, y+2, 16777215);
        }
        else{
            context.fill(x, y, x+12, y+12, -16711936);
            context.fill(x+1, y+1, x+11, y+11, Objects.requireNonNull(client).options.getTextBackgroundColor(-587202560));
            context.drawCenteredTextWithShadow(client.textRenderer, ENGLISH, x+6, y+2, 16777215);
        }
    }
}
