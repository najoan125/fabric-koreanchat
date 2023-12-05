package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.util.Indicator;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CreativeInventoryScreen.class)
public class CreativeInventoryScreenMixin extends Screen {
    protected CreativeInventoryScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = {"render"}, at = @At(value = "TAIL", shift = At.Shift.BY, by = -3))
    private void addCustomLabel(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (KoreanPatchClient.SEARCH) {
            int x = (this.width - 176) / 2 + 82; // int var10004 = this.x + 82; in init()
            int y = (this.height - 166) / 2 + 6; // int var10005 = this.y + 6; in init()
            int searchBoxWidth = 80;

            Indicator.showIndicator(context, x + searchBoxWidth + 9, y + 14, true);
        }
    }

    @Inject(at = {@At(value = "HEAD")}, method = {"setSelectedTab"})
    private void check(ItemGroup group, CallbackInfo callbackInfo) {
        KoreanPatchClient.SEARCH = group.getType() == ItemGroup.Type.SEARCH;
    }
}
