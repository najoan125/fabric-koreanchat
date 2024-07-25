package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.util.Indicator;
import com.hyfata.najoan.koreanpatch.util.TextFieldWidgetUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {AddServerScreen.class})
public class AddServerScreenMixin extends Screen {
    protected AddServerScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    private TextFieldWidget addressField;

    @Shadow
    private TextFieldWidget serverNameField;

    @Shadow
    @Final
    private static Text ENTER_NAME_TEXT;

    @Shadow
    @Final
    private static Text ENTER_IP_TEXT;

    @Unique
    private final AnimationUtil animationUtil = new AnimationUtil();

    @Inject(at = {@At(value = "TAIL")}, method = {"render"})
    private void addCustomLabel(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float x;
        float y;
        int textX = this.width / 2 - 100 + 1;

        if (serverNameField.isFocused()) {
            x = TextFieldWidgetUtil.getCursorXWithText(serverNameField, ENTER_NAME_TEXT, textX);
            y = TextFieldWidgetUtil.calculateIndicatorY(serverNameField);
        } else if (addressField.isFocused()) {
            x = TextFieldWidgetUtil.getCursorXWithText(addressField, ENTER_IP_TEXT, textX);
            y = TextFieldWidgetUtil.calculateIndicatorY(addressField);
        } else {
            return;
        }

        animationUtil.init(x - 4, 0);
        animationUtil.calculateAnimation(x, 0);

        context.getMatrices().translate(0.0F, 0.0F, 200.0F);
        Indicator.showIndicator(context, animationUtil.getResultX() + 4, y);
    }
}
