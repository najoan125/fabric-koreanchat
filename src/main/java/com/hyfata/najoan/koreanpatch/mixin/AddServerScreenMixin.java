package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(value = {AddServerScreen.class})
public class AddServerScreenMixin extends Screen {
    private final Text KOREAN = Text.literal("\uD55C");
    private final Text ENGLISH = Text.literal("\uC601");
    protected AddServerScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = {@At(value = "TAIL")}, method = {"render"})
    private void addCustomLabel(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci){
        if (KoreanPatchClient.KOREAN) {
            fill(matrices, this.width / 2 - 6, 55 - 2 - 10, this.width / 2 + 6, 55 + 10 - 10, -65536);
            fill(matrices, this.width / 2 - 5, 55 - 1 - 10, this.width / 2 + 5, 55 + 9 - 10, Objects.requireNonNull(this.client).options.getTextBackgroundColor(-587202560));
            drawCenteredTextWithShadow(matrices, this.client.textRenderer, KOREAN, this.width / 2, 55 - 10, 16777215);
        } else {
            fill(matrices, this.width / 2 - 6, 55 - 2 - 10, this.width / 2 + 6, 55 + 10 - 10, -16711936);
            fill(matrices, this.width / 2 - 5, 55 - 1 - 10, this.width / 2 + 5, 55 + 9 - 10, Objects.requireNonNull(this.client).options.getTextBackgroundColor(-587202560));
            drawCenteredTextWithShadow(matrices, this.client.textRenderer, ENGLISH, this.width / 2, 55 - 10, 16777215);
        }
    }
}
