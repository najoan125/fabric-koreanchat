package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.util.language.LanguageUtil;
import com.hyfata.najoan.koreanpatch.util.mixin.textfieldwidget.ITextFieldWidgetAccessor;
import com.hyfata.najoan.koreanpatch.util.mixin.textfieldwidget.TextFieldWidgetHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(value = {TextFieldWidget.class})
public abstract class TextFieldWidgetMixin implements ITextFieldWidgetAccessor {
    @Shadow
    private Consumer<String> changedListener;
    @Unique
    private final MinecraftClient client = MinecraftClient.getInstance();

    @Shadow
    public abstract int getCursor();

    @Shadow
    public abstract void setCursor(int var1);

    @Shadow
    public abstract void eraseCharacters(int var1);

    @Shadow
    public abstract String getText();

    @Shadow
    public abstract void write(String var1);

    @Shadow
    protected abstract boolean isEditable();

    @Shadow
    protected abstract void onChanged(String var1);

    @Shadow
    public abstract void setText(String var1);

    @Shadow
    public abstract boolean isActive();

    @Shadow
    public abstract String getSelectedText();

    @Override
    public Consumer<String> fabric_koreanchat$getChangedListener() {
        return this.changedListener;
    }

    @Override
    public void fabric_koreanchat$changed(String var1) {
        onChanged(var1);
    }

    @Unique
    private final TextFieldWidgetHandler handler = new TextFieldWidgetHandler(this);

    @Inject(at = {@At(value = "HEAD")}, method = {"charTyped(CI)Z"}, cancellable = true)
    public void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this.client.currentScreen != null && !KoreanPatchClient.bypassInjection &&
                LanguageUtil.isKorean() && this.isEditable() && Character.charCount(chr) == 1) {
            handler.typedTextField(chr, modifiers, cir);
        }
    }

    @Inject(at = {@At(value = "HEAD")}, method = {"keyPressed(III)Z"}, cancellable = true)
    private void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfo) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen != null && !KoreanPatchClient.bypassInjection) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (handler.onBackspaceKeyPressed()) {
                    callbackInfo.setReturnValue(Boolean.TRUE);
                }
            }
        }
    }
}

