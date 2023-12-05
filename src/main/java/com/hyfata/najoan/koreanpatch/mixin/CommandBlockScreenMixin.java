package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.util.Indicator;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractCommandBlockScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AbstractCommandBlockScreen.class)
public class CommandBlockScreenMixin extends Screen {
    protected CommandBlockScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = {@At(value="TAIL")}, method = {"render"})
    private void addCustomLabel(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        Indicator.showIndicator(context, this.width / 2, 35, true);
    }
}
