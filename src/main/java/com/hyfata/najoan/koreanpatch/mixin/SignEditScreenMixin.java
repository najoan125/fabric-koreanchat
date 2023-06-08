package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={AbstractSignEditScreen.class})
public abstract class SignEditScreenMixin extends Screen {
    public final MinecraftClient client = MinecraftClient.getInstance();
    public final Text KOREAN = Text.literal("\uD55C");
    public final Text ENGLISH = Text.literal("\uC601");

    protected SignEditScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = {@At(value="RETURN")}, method = {"render"})
    public void addCustomLabel(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        if (KoreanPatchClient.KOREAN) {
            context.fill(this.width / 2-6, 55-2, this.width / 2+6, 55+10, -65536);
            context.fill(this.width / 2-5, 55-1, this.width / 2+5, 55+9, this.client.options.getTextBackgroundColor(-587202560));
            context.drawCenteredTextWithShadow(this.client.textRenderer, KOREAN, this.width / 2, 55, 16777215);
        }
        else{
            context.fill(this.width / 2-6, 55-2, this.width / 2+6, 55+10, -16711936);
            context.fill(this.width / 2-5, 55-1, this.width / 2+5, 55+9, this.client.options.getTextBackgroundColor(-587202560));
            context.drawCenteredTextWithShadow(this.client.textRenderer, ENGLISH, this.width / 2, 55, 16777215);
        }
    }

    @Inject(at={@At(value="HEAD")}, method={"keyPressed(III)Z"})
    public void init(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this.client.currentScreen != null && (keyCode == KoreanPatchClient.KEYCODE || scanCode == KoreanPatchClient.SCANCODE)) {
            KoreanPatchClient.KOREAN = !KoreanPatchClient.KOREAN;
        }
    }
}

