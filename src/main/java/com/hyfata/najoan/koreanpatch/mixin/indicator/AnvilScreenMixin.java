package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.util.Indicator;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
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
    private void customLabel(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float x = nameField.getX() + nameField.getWidth() - Indicator.getIndicatorWidth();
        float y = nameField.getY() - Indicator.getIndicatorHeight() - 6;

        Indicator.showIndicator(matrices, x, y);
    }
}
