package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.util.Indicator;
import com.hyfata.najoan.koreanpatch.util.TextFieldWidgetUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {SelectWorldScreen.class})
public class SelectWorldScreenMixin extends Screen {

    @Shadow
    protected TextFieldWidget searchBox;

    @Unique
    private final AnimationUtil animationUtil = new AnimationUtil();

    protected SelectWorldScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = {@At(value = "TAIL")}, method = {"render"})
    private void addCustomLabel(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci){
        float x = TextFieldWidgetUtil.getCursorX(searchBox);
        float y = TextFieldWidgetUtil.calculateIndicatorY(searchBox);

        animationUtil.init((float) this.width / 2 - 105, 0);
        animationUtil.calculateAnimation(x, 0);

        matrices.translate(0.0F, 0.0F, 200.0F);
        Indicator.showIndicator(matrices, animationUtil.getResultX() + 4, y);
    }
}
