package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(value = {EditWorldScreen.class})
public class EditWorldScreenMixin extends Screen {
    protected EditWorldScreenMixin(Text title) {
        super(title);
    }
    @Final
    @Shadow
    private DirectionalLayoutWidget layout;

    @Inject(at = {@At(value = "TAIL")}, method = {"render"})
    public void addCustomLabel(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        showKEIndicator(context, layout.getX()-16, layout.getY()+43);
    }

    @Unique
    private void showKEIndicator(DrawContext context, int x, int y) {
        final MinecraftClient client = MinecraftClient.getInstance();
        final Text KOREAN = Text.literal("한");
        final Text ENGLISH = Text.literal("영");
        if (KoreanPatchClient.KOREAN) {
            context.fill(x, y, x+12, y+12, -65536);
            context.fill(x+1, y+1, x+11, y+11, Objects.requireNonNull(client).options.getTextBackgroundColor(-587202560));
            context.drawCenteredTextWithShadow(client.textRenderer, KOREAN, x+6, y+2, 16777215);
        }
        else{
            context.fill(x, y, x+12, y+12, -16711936);
            context.fill(x+1, y+1, x+11, y+11, Objects.requireNonNull(client).options.getTextBackgroundColor(-587202560));
            context.drawCenteredTextWithShadow(client.textRenderer, ENGLISH, x+6, y+2, 16777215);
        }
    }
}
