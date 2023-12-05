package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.util.Indicator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={BookEditScreen.class})
public abstract class BookEditScreenMixin extends Screen {
    @Unique
    private final MinecraftClient client = MinecraftClient.getInstance();
    protected BookEditScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = {@At(value="RETURN")}, method = {"render"})
    private void addCustomLabel(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        int x = (this.width - 192) / 2; // int i = (this.width - 192) / 2; in render() method
        Indicator.showIndicator(context, x + 10, 30, true);
    }

    @Inject(at={@At(value="HEAD")}, method={"keyPressed(III)Z"})
    private void init(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this.client.currentScreen != null && (keyCode == KoreanPatchClient.KEYCODE || scanCode == KoreanPatchClient.SCANCODE)) {
            KoreanPatchClient.KOREAN = !KoreanPatchClient.KOREAN;
        }
    }
}

