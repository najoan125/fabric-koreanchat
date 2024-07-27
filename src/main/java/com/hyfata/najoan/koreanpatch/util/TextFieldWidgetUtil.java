package com.hyfata.najoan.koreanpatch.util;

import com.hyfata.najoan.koreanpatch.mixin.accessor.TextFieldWidgetAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class TextFieldWidgetUtil {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static float getCursorX(TextFieldWidget textField) {
        TextFieldWidgetAccessor accessor = (TextFieldWidgetAccessor) textField;
        int firstCharacterIndex = accessor.getFirstCharacterIndex();
        int selectionStart = accessor.getSelectionStart();

        float cursorX = textField.x + client.textRenderer.getTextHandler().getWidth(textField.getText().substring(firstCharacterIndex, selectionStart));
        float endX = textField.x + textField.getWidth() - 1.2f * Indicator.getIndicatorWidth();

        return Math.min(cursorX, endX);
    }

    public static float calculateIndicatorY(TextFieldWidget textField) {
        return textField.y - Indicator.getIndicatorHeight() / 1.5f;
    }

    public static float getCursorXWithText(TextFieldWidget textField, Text text, int x) {
        int textWidth = client.textRenderer.getWidth(text);
        return Math.max(x + textWidth, getCursorX(textField));
    }
}
