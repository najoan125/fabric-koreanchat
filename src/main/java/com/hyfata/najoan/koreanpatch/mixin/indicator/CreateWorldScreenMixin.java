package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.util.Indicator;
import com.hyfata.najoan.koreanpatch.util.TextFieldWidgetUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {CreateWorldScreen.class})
public class CreateWorldScreenMixin extends Screen {
    @Shadow private TextFieldWidget levelNameField;

    @Shadow private boolean moreOptionsOpen;

    protected CreateWorldScreenMixin(Text title) {
        super(title);
    }

    @Unique
    AnimationUtil animationUtil = new AnimationUtil();

    @Inject(at = {@At(value = "RETURN")}, method = {"render"})
    private void addCustomLabel(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!moreOptionsOpen) {
            KoreanPatchClient.bypassInjection = false;
            Text text = new TranslatableText("selectWorld.enterName");

            float x = TextFieldWidgetUtil.getCursorXWithText(levelNameField, text, levelNameField.x) + 4;
            float y = TextFieldWidgetUtil.calculateIndicatorY(levelNameField);

            animationUtil.init(x - 4, 0);
            animationUtil.calculateAnimation(x, 0);

            Indicator.showIndicator(matrices, animationUtil.getResultX(), y);
        } else {
            KoreanPatchClient.bypassInjection = true;
        }
    }
}
