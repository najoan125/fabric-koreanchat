package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.util.Indicator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {SignEditScreen.class})
public abstract class SignEditScreenMixin extends Screen {
    @Shadow
    private int currentRow;

    @Unique
    public final MinecraftClient client = MinecraftClient.getInstance();

    @Unique
    AnimationUtil animationUtil = new AnimationUtil();

    protected SignEditScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/render/DiffuseLighting;enableGuiDepthLighting()V", shift = At.Shift.BY, by = -3)}, method = {"render"})
    public void addCustomLabel(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float x = -(90 / 2f) - Indicator.getIndicatorWidth() / 2 - 5;
        int l = 4 * 10 / 2;
        float y = currentRow * 10 - l + client.textRenderer.fontHeight / 2f;

        animationUtil.init(0, y - 4);
        animationUtil.calculateAnimation(0, y);

        Indicator.showCenteredIndicator(matrices, x, animationUtil.getResultY());
    }
}

