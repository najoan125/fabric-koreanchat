package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ChatScreen.class})
public abstract class ChatScreenMixin extends Screen {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final Text KOREAN = Text.literal("\uD55C");
    private final Text ENGLISH = Text.literal("\uC601");

    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = {@At(value="HEAD")}, method = {"render"})
    private void addCustomLabel(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci){
        if (KoreanPatchClient.KOREAN) {
            fill(matrices, 2, this.height - 42 + 3, 14, this.height - 30 + 3, -65536);
            fill(matrices, 3, this.height - 41 + 3, 13, this.height - 31 + 3, this.client.options.getTextBackgroundColor(-587202560));
            drawCenteredTextWithShadow(matrices, this.client.textRenderer, KOREAN, 8, this.height - 40 + 3, 16777215);
        } else {
            fill(matrices, 2, this.height - 42 + 3, 14, this.height - 30 + 3, -16711936);
            fill(matrices, 3, this.height - 41 + 3, 13, this.height - 31 + 3, this.client.options.getTextBackgroundColor(-587202560));
            drawCenteredTextWithShadow(matrices, this.client.textRenderer, ENGLISH, 8, this.height - 40 + 3, 16777215);
        }
    }
}
