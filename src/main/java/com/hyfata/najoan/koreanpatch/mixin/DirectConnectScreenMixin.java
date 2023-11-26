package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.DirectConnectScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(value = {DirectConnectScreen.class})
public class DirectConnectScreenMixin extends Screen {
    @Unique
    private final Text KOREAN = Text.literal("한");
    @Unique
    private final Text ENGLISH = Text.literal("영");
    protected DirectConnectScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = {@At(value="TAIL")}, method = {"render"})
    private void addCustomLabel(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        if (KoreanPatchClient.KOREAN) {
            context.fill(this.width / 2 - 6, 55 - 2 + 35, this.width / 2 + 6, 55 + 10 + 35, -65536);
            context.fill(this.width / 2 - 5, 55 - 1 + 35, this.width / 2 + 5, 55 + 9 + 35, Objects.requireNonNull(this.client).options.getTextBackgroundColor(-587202560));
            context.drawCenteredTextWithShadow(this.client.textRenderer, KOREAN, this.width / 2, 55 + 35, 16777215);
        } else {
            context.fill(this.width / 2 - 6, 55 - 2 + 35, this.width / 2 + 6, 55 + 10 + 35, -16711936);
            context.fill(this.width / 2 - 5, 55 - 1 + 35, this.width / 2 + 5, 55 + 9 + 35, Objects.requireNonNull(this.client).options.getTextBackgroundColor(-587202560));
            context.drawCenteredTextWithShadow(this.client.textRenderer, ENGLISH, this.width / 2, 55 + 35, 16777215);
        }
    }
}
