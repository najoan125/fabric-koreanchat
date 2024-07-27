package com.hyfata.najoan.koreanpatch.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.util.math.Matrix4f;

public class RenderUtil {
    static MinecraftClient client = MinecraftClient.getInstance();

    public static void drawCenteredText(MatrixStack context, OrderedText text, float x, float y) {
        TextRenderer textRenderer = client.textRenderer;
        float textWidth = textRenderer.getWidth(text);
        float xPosition = x - textWidth / 2.0f;
        float yPosition = y - client.textRenderer.fontHeight / 2.0f;
        drawText(context, text, xPosition, yPosition);
    }

    public static void drawText(MatrixStack context, OrderedText text, float x, float y) {
        TextRenderer textRenderer = client.textRenderer;
        textRenderer.draw(context, text, x, y, -1);
    }

    public static void fill(MatrixStack context, float x1, float y1, float x2, float y2, int color) {
        Matrix4f matrix = context.peek().getPositionMatrix();
        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, x1, y1, 0f).color(color).next();
        bufferBuilder.vertex(matrix, x1, y2, 0f).color(color).next();
        bufferBuilder.vertex(matrix, x2, y2, 0f).color(color).next();
        bufferBuilder.vertex(matrix, x2, y1, 0f).color(color).next();
        BufferRenderer.drawWithShader(bufferBuilder.end());
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}
