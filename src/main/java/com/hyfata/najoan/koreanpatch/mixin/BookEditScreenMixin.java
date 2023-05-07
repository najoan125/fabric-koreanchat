package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={BookEditScreen.class})
public abstract class BookEditScreenMixin extends Screen {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final Text KOREAN = Text.literal("\uD55C");
    private final Text ENGLISH = Text.literal("\uC601");

    protected BookEditScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = {@At(value="RETURN")}, method = {"render"})
    private void addCustomLabel(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci){
        if (KoreanPatchClient.KOREAN) {
            fill(matrices, (this.width - 192) / 2 + 10 - 6, 30 - 2, (this.width - 192) / 2 + 10 + 6, 30 + 10, -65536);
            fill(matrices, (this.width - 192) / 2 + 10 - 5, 30 - 1, (this.width - 192) / 2 + 10 + 5, 30 + 9, this.client.options.getTextBackgroundColor(-587202560));
            drawCenteredTextWithShadow(matrices, this.client.textRenderer, KOREAN, (this.width - 192) / 2 + 10, 30, 16777215);
        }
        else{
            fill(matrices, (this.width - 192) / 2 + 10 - 6, 30 - 2, (this.width - 192) / 2 + 10 + 6, 30 + 10, -16711936);
            fill(matrices, (this.width - 192) / 2 + 10 - 5, 30 - 1, (this.width - 192) / 2 + 10 + 5, 30 + 9, this.client.options.getTextBackgroundColor(-587202560));
            drawCenteredTextWithShadow(matrices, this.client.textRenderer, ENGLISH, (this.width - 192) / 2 + 10, 30, 16777215);
        }
    }

    @Inject(at={@At(value="HEAD")}, method={"keyPressed(III)Z"})
    private void init(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this.client.currentScreen != null && (keyCode == KoreanPatchClient.KEYCODE || scanCode == KoreanPatchClient.SCANCODE)) {
            KoreanPatchClient.KOREAN = !KoreanPatchClient.KOREAN;
        }
    }
}

