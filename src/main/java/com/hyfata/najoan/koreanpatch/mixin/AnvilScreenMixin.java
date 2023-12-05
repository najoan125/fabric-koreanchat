package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.util.Indicator;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {AnvilScreen.class})
public class AnvilScreenMixin extends Screen {
    @Shadow
    private TextFieldWidget nameField;

    protected AnvilScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = {@At(value = "TAIL")}, method = {"renderForeground"})
    private void customLabel(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        int x = nameField.getX();
        int y = nameField.getY();
        Indicator.showIndicator(context, x - 10, y, true);
    }
}
