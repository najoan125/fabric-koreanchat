package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {EditWorldScreen.class})
public class EditWorldScreenMixin extends Screen {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final Text KOREAN = Text.literal("\uD55C");
    private final Text ENGLISH = Text.literal("\uC601");
    protected EditWorldScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = {@At(value = "TAIL")}, method = {"render"})
    public void addCustomLabel(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci){
        if (KoreanPatchClient.KOREAN) {
            fill(matrices, this.width / 2 - 6 -110, 55 - 2 - 12, this.width / 2 + 6 -110, 55 + 10 - 12, -65536);
            fill(matrices, this.width / 2 - 5 -110, 55 - 1 - 12, this.width / 2 + 5 -110, 55 + 9 - 12, this.client.options.getTextBackgroundColor(-587202560));
            drawCenteredTextWithShadow(matrices, this.client.textRenderer, KOREAN, this.width / 2 -110, 55 - 12, 16777215);
        } else {
            fill(matrices, this.width / 2 - 6 -110, 55 - 2 - 12, this.width / 2 + 6 -110, 55 + 10 - 12, -16711936);
            fill(matrices, this.width / 2 - 5 -110, 55 - 1 - 12, this.width / 2 + 5 -110, 55 + 9 - 12, this.client.options.getTextBackgroundColor(-587202560));
            drawCenteredTextWithShadow(matrices, this.client.textRenderer, ENGLISH, this.width / 2 -110, 55 - 12, 16777215);
        }
    }
}
