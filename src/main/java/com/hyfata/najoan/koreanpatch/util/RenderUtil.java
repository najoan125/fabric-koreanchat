package com.hyfata.najoan.koreanpatch.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.OrderedText;
import org.joml.Matrix4f;

public class RenderUtil {
    static MinecraftClient client = MinecraftClient.getInstance();

    public static void drawCenteredText(DrawContext context, OrderedText text, float x, float y) {
        TextRenderer textRenderer = client.textRenderer;
        float textWidth = textRenderer.getWidth(text);
        float xPosition = x - textWidth / 2.0f;
        float yPosition = y - client.textRenderer.fontHeight / 2.0f;
        drawText(context, text, xPosition, yPosition);
    }

    public static void drawText(DrawContext context, OrderedText text, float x, float y) {
        TextRenderer textRenderer = client.textRenderer;
        Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();
        VertexConsumerProvider vertexConsumers = context.getVertexConsumers();
        textRenderer.draw(text, x, y, -1, false, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 15728880);
    }

    public static void fill(DrawContext context, float x1, float y1, float x2, float y2, int color) {
        Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();
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

        VertexConsumer vertexConsumer = context.getVertexConsumers().getBuffer(RenderLayer.getGui());
        vertexConsumer.vertex(matrix, x1, y1, 0f).color(color).next();
        vertexConsumer.vertex(matrix, x1, y2, 0f).color(color).next();
        vertexConsumer.vertex(matrix, x2, y2, 0f).color(color).next();
        vertexConsumer.vertex(matrix, x2, y1, 0f).color(color).next();
        context.draw();
    }
}
