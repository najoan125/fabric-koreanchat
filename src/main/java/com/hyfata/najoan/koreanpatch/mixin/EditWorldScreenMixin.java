package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.util.Indicator;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
        Indicator.showIndicator(context, layout.getX()-16, layout.getY()+43, false);
    }
}
