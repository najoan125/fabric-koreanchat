package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.geom.AffineTransform;

@Mixin(value= CreativeInventoryScreen.class)
public class CreativeInventoryScreenMixin extends Screen {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final Text KOREAN = Text.literal("\uD55C");
    private final Text ENGLISH = Text.literal("\uC601");

    protected CreativeInventoryScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = {"render"}, at = @At(value="TAIL", shift = At.Shift.BY, by = -3))
    private void addCustomLabel(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci){
        if(KoreanPatchClient.SEARCH) {
            if (KoreanPatchClient.KOREAN) {
                fill(matrices, (this.width - 176) / 2+162-6+9, (this.height - 166) / 2+6-2+14, (this.width - 176) / 2+162+6+9, (this.height - 166) / 2+6+10+14, -65536);
                fill(matrices, (this.width - 176) / 2+162-5+9, (this.height - 166) / 2+6-1+14, (this.width - 176) / 2+162+5+9, (this.height - 166) / 2+6+9+14, this.client.options.getTextBackgroundColor(-587202560));
                drawCenteredTextWithShadow(matrices, this.client.textRenderer, KOREAN, (this.width - 176) / 2+162+9, (this.height - 166) / 2+6+14, 16777215);
            } else {
                fill(matrices, (this.width - 176) / 2+162-6+9, (this.height - 166) / 2+6-2+14, (this.width - 176) / 2+162+6+9, (this.height - 166) / 2+6+10+14, -16711936);
                fill(matrices, (this.width - 176) / 2+162-5+9, (this.height - 166) / 2+6-1+14, (this.width - 176) / 2+162+5+9, (this.height - 166) / 2+6+9+14, this.client.options.getTextBackgroundColor(-587202560));
                drawCenteredTextWithShadow(matrices, this.client.textRenderer, ENGLISH, (this.width - 176) / 2+162+9, (this.height - 166) / 2+6+14, 16777215);
            }
        }
    }

    @Inject(at = {@At(value="HEAD")}, method = {"setSelectedTab"})
    private void check(ItemGroup group, CallbackInfo callbackInfo){
        if(group == ItemGroups.SEARCH){
            KoreanPatchClient.SEARCH = true;
        }
        else{
            KoreanPatchClient.SEARCH = false;
        }
    }
}
