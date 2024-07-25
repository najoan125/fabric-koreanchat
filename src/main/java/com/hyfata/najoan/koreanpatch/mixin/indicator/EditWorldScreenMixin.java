package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.util.Indicator;
import com.hyfata.najoan.koreanpatch.util.TextFieldWidgetUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {EditWorldScreen.class})
public class EditWorldScreenMixin extends Screen {
    protected EditWorldScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    private TextFieldWidget levelNameTextField;

    @Shadow
    @Final
    private static Text ENTER_NAME_TEXT;

    @Unique
    AnimationUtil animationUtil = new AnimationUtil();

    @Inject(at = {@At(value = "TAIL")}, method = {"render"})
    public void addCustomLabel(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float x = TextFieldWidgetUtil.getCursorXWithText(levelNameTextField, ENTER_NAME_TEXT, levelNameTextField.getX()) + 4;
        float y = TextFieldWidgetUtil.calculateIndicatorY(levelNameTextField);

        animationUtil.init(x - 4, 0);
        animationUtil.calculateAnimation(x, 0);

        Indicator.showIndicator(context, animationUtil.getResultX(), y);
    }
}
