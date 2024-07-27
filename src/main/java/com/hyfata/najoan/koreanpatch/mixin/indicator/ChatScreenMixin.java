package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.mixin.accessor.ChatInputSuggestorAccessor;
import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.util.Indicator;
import com.hyfata.najoan.koreanpatch.util.TextFieldWidgetUtil;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {ChatScreen.class})
public abstract class ChatScreenMixin extends Screen {
    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    protected TextFieldWidget chatField;

    @Shadow
    CommandSuggestor commandSuggestor;

    @Unique
    private final AnimationUtil animationUtil = new AnimationUtil();


    @Inject(at = {@At(value = "TAIL")}, method = {"render"})
    private void addCustomLabel(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ChatInputSuggestorAccessor accessor = (ChatInputSuggestorAccessor) commandSuggestor;
        int suggestorHeight = 0;
        int messagesY = 0;
        // chatSuggestor
        if (accessor.getWindow() != null && accessor.getPendingSuggestions() != null && accessor.getPendingSuggestions().isDone()) {
            Suggestions suggestions = accessor.getPendingSuggestions().join();
            if (!suggestions.isEmpty())
                suggestorHeight = Math.min(accessor.getSortSuggestions(suggestions).size(), accessor.getMaxSuggestionSize()) * 12 + 1;
        }
        //messages
        else if (!accessor.getMessages().isEmpty()) {
            int i = accessor.getMessages().size() - 1;
            messagesY = accessor.isChatScreenSized() ? this.height - 14 - 13 - 12 * i : 72 + 12 * i;
            messagesY -= 12;
        }

        float indicatorX = TextFieldWidgetUtil.getCursorX(chatField);
        float indicatorY = messagesY == 0 ? this.height - 27 - suggestorHeight : messagesY;

        animationUtil.init(0, 0);
        animationUtil.calculateAnimation(indicatorX, 0);

        matrices.translate(0.0F, 0.0F, 200.0F);
        Indicator.showIndicator(matrices, animationUtil.getResultX(), indicatorY);
    }
}
