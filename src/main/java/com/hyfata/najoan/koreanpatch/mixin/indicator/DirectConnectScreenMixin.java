package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.util.Indicator;
import com.hyfata.najoan.koreanpatch.util.TextFieldWidgetUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.DirectConnectScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {DirectConnectScreen.class})
public class DirectConnectScreenMixin extends Screen {
    @Shadow
    private TextFieldWidget addressField;

    @Unique
    AnimationUtil animationUtil = new AnimationUtil();

    protected DirectConnectScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = {@At(value = "TAIL")}, method = {"render"})
    private void addCustomLabel(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float x = TextFieldWidgetUtil.getCursorX(addressField) + 4;
        float y = TextFieldWidgetUtil.calculateIndicatorY(addressField);

        animationUtil.init(x - 4, 0);
        animationUtil.calculateAnimation(x, 0);

        Indicator.showIndicator(context, animationUtil.getResultX(), y);
    }
}
