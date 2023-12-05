package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.util.Indicator;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ChatScreen.class})
public abstract class ChatScreenMixin extends Screen {
    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = {@At(value="HEAD")}, method = {"render"})
    private void addCustomLabel(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        Indicator.showIndicator(context, 2, this.height - 39, false);
    }
}
