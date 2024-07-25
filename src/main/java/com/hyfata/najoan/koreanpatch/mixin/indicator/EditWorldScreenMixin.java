package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.util.Indicator;
import com.hyfata.najoan.koreanpatch.util.TextFieldWidgetUtil;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = {EditWorldScreen.class})
public class EditWorldScreenMixin extends Screen {
    protected EditWorldScreenMixin(Text title) {
        super(title);
    }

    @Unique
    @Mutable
    @Final
    private TextFieldWidget nameFieldWidget;

    @Shadow
    @Final
    private static Text ENTER_NAME_TEXT;

    @Unique
    AnimationUtil animationUtil = new AnimationUtil();

    @Inject(method = "<init>", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onInit(MinecraftClient client, LevelStorage.Session session, String levelName, BooleanConsumer callback, CallbackInfo ci, TextRenderer textRenderer, TextFieldWidget textFieldWidget, DirectionalLayoutWidget directionalLayoutWidget, ButtonWidget buttonWidget) {
        nameFieldWidget = textFieldWidget;
    }

    @Inject(at = {@At(value = "TAIL")}, method = {"render"})
    public void addCustomLabel(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float x = TextFieldWidgetUtil.getCursorXWithText(nameFieldWidget, ENTER_NAME_TEXT, nameFieldWidget.getX()) + 4;
        float y = TextFieldWidgetUtil.calculateIndicatorY(nameFieldWidget);

        animationUtil.init(x - 4, 0);
        animationUtil.calculateAnimation(x, 0);

        Indicator.showIndicator(context, animationUtil.getResultX(), y);
    }
}
