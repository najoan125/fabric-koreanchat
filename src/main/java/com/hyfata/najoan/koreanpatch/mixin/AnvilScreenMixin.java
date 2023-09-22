package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(value = {AnvilScreen.class})
public class AnvilScreenMixin extends Screen {
    @Shadow private TextFieldWidget nameField;
    private final Text KOREAN = Text.literal("\uD55C");
    private final Text ENGLISH = Text.literal("\uC601");
    protected AnvilScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = {@At(value = "TAIL")}, method = {"renderForeground"})
    private void customLabel(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        int x = nameField.getX();
        int y = nameField.getY();
        if (KoreanPatchClient.KOREAN) {
            context.fill(x - 6 -10, y - 2, x + 6 -10, y + 10, -65536);
            context.fill(x - 5 -10, y - 1, x + 5 -10, y + 9, Objects.requireNonNull(this.client).options.getTextBackgroundColor(-587202560));
            context.drawCenteredTextWithShadow(this.client.textRenderer, KOREAN, x-10, y, 16777215);
        } else {
            context.fill(x - 6 -10, y - 2, x + 6 -10, y + 10, -16711936);
            context.fill(x - 5 -10, y - 1, x + 5 -10, y + 9, Objects.requireNonNull(this.client).options.getTextBackgroundColor(-587202560));
            context.drawCenteredTextWithShadow(this.client.textRenderer, ENGLISH, x-10, y, 16777215);
        }
    }
}