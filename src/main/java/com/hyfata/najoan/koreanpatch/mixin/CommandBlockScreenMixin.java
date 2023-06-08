package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractCommandBlockScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(value = AbstractCommandBlockScreen.class)
public class CommandBlockScreenMixin extends Screen {
    protected CommandBlockScreenMixin(Text title) {
        super(title);
    }
    private final Text KOREAN = Text.literal("\uD55C");
    private final Text ENGLISH = Text.literal("\uC601");

    @Inject(at = {@At(value="TAIL")}, method = {"render"})
    private void addCustomLabel(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        if (KoreanPatchClient.KOREAN) {
            context.fill(this.width / 2-6, 55-2-20, this.width / 2+6, 55+10-20, -65536);
            context.fill(this.width / 2-5, 55-1-20, this.width / 2+5, 55+9-20, Objects.requireNonNull(this.client).options.getTextBackgroundColor(-587202560));
            context.drawCenteredTextWithShadow(this.client.textRenderer, KOREAN, this.width / 2, 55-20, 16777215);
        }
        else{
            context.fill(this.width / 2-6, 55-2-20, this.width / 2+6, 55+10-20, -16711936);
            context.fill(this.width / 2-5, 55-1-20, this.width / 2+5, 55+9-20, Objects.requireNonNull(this.client).options.getTextBackgroundColor(-587202560));
            context.drawCenteredTextWithShadow(this.client.textRenderer, ENGLISH, this.width / 2, 55-20, 16777215);
        }
    }
}
