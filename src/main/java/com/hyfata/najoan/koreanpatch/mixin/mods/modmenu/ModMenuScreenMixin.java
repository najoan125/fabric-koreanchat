package com.hyfata.najoan.koreanpatch.mixin.mods.modmenu;

import com.hyfata.najoan.koreanpatch.util.Indicator;
import com.hyfata.najoan.koreanpatch.util.TextFieldWidgetUtil;
import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.terraformersmc.modmenu.gui.ModsScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModsScreen.class, remap = false)
public class ModMenuScreenMixin {
    @Shadow
    private TextFieldWidget searchBox;

    @Unique
    AnimationUtil animationUtil = new AnimationUtil();

    @Inject(at = @At("TAIL"), method = "render")
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float cursorX = TextFieldWidgetUtil.getCursorX(searchBox) + 4;
        float y = TextFieldWidgetUtil.calculateIndicatorY(searchBox);

        animationUtil.init(cursorX - 4, 0);
        animationUtil.calculateAnimation(cursorX, 0);

        context.getMatrices().translate(0.0F, 0.0F, 200.0F);
        Indicator.showIndicator(context, animationUtil.getResultX(), y);
    }
}
