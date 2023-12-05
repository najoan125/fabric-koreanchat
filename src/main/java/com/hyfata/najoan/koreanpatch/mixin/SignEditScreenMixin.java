package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.util.Indicator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={AbstractSignEditScreen.class})
public abstract class SignEditScreenMixin extends Screen {
    @Unique
    public final MinecraftClient client = MinecraftClient.getInstance();

    protected SignEditScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = {@At(value="RETURN")}, method = {"render"})
    public void addCustomLabel(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        Indicator.showIndicator(context, this.width / 2, 54, true);
    }

    @Inject(at={@At(value="HEAD")}, method={"keyPressed(III)Z"})
    public void init(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this.client.currentScreen != null && (keyCode == KoreanPatchClient.KEYCODE || scanCode == KoreanPatchClient.SCANCODE)) {
            KoreanPatchClient.KOREAN = !KoreanPatchClient.KOREAN;
        }
    }
}

