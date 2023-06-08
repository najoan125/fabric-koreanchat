package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {SelectWorldScreen.class})
public class SelectWorldScreenMixin extends Screen {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final Text KOREAN = Text.literal("\uD55C");
    private final Text ENGLISH = Text.literal("\uC601");
    protected SelectWorldScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = {@At(value = "TAIL")}, method = {"render"})
    private void addCustomLabel(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        if (KoreanPatchClient.KOREAN) {
            context.fill(this.width / 2 - 6 -110, 55 - 2 - 28, this.width / 2 + 6 -110, 55 + 10 - 28, -65536);
            context.fill(this.width / 2 - 5 -110, 55 - 1 - 28, this.width / 2 + 5 -110, 55 + 9 - 28, this.client.options.getTextBackgroundColor(-587202560));
            context.drawCenteredTextWithShadow(this.client.textRenderer, KOREAN, this.width / 2 -110, 55 - 28, 16777215);
        } else {
            context.fill(this.width / 2 - 6 -110, 55 - 2 - 28, this.width / 2 + 6 -110, 55 + 10 - 28, -16711936);
            context.fill(this.width / 2 - 5 -110, 55 - 1 - 28, this.width / 2 + 5 -110, 55 + 9 - 28, this.client.options.getTextBackgroundColor(-587202560));
            context.drawCenteredTextWithShadow(this.client.textRenderer, ENGLISH, this.width / 2 -110, 55 - 28, 16777215);
        }
    }

    @Inject(at = {@At(value = "HEAD")}, method = {"keyPressed"})
    private void keyListener(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir){
        if (this.client.currentScreen != null && (keyCode == KoreanPatchClient.KEYCODE || scanCode == KoreanPatchClient.SCANCODE)) {
            KoreanPatchClient.KOREAN = !KoreanPatchClient.KOREAN;
        }
    }
}
