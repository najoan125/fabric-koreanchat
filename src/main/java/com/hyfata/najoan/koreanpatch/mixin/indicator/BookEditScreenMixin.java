package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.mixin.accessor.BookEditScreenPageContentAccessor;
import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.util.Indicator;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {BookEditScreen.class})
public abstract class BookEditScreenMixin extends Screen {

    protected BookEditScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    protected abstract BookEditScreen.PageContent getPageContent();

    @Shadow
    private boolean signing;

    @Unique
    AnimationUtil animationUtil = new AnimationUtil();

    @Inject(at = {@At(value = "RETURN")}, method = {"render"})
    private void addCustomLabel(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float x = (this.width - 192) / 2f; // int i = (this.width - 192) / 2; in render() method
        float y;
        if (signing) {
            y = 50 + 4.5f;
        } else {
            BookEditScreenPageContentAccessor pageContent = (BookEditScreenPageContentAccessor) getPageContent();
            y = pageContent.getPosition().y + 32 + 4.5f; //absolutePositionToScreenPosition() + (fontHeight(9) / 2)
        }

        animationUtil.init(0, y - 4);
        animationUtil.calculateAnimation(0, y);

        Indicator.showCenteredIndicator(matrices, x + 10, animationUtil.getResultY());
    }
}

