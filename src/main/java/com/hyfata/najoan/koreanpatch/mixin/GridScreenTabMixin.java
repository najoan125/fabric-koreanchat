package com.hyfata.najoan.koreanpatch.mixin;

import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.tab.GridScreenTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//import net.minecraft.client.gui.widget.GridWidget;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = {GridScreenTab.class})
public class GridScreenTabMixin {
//     @Final
//     @Shadow protected GridWidget grid;

    @Inject(at = {@At(value="RETURN")}, method = {"refreshGrid"})
    public void refreshGrid(ScreenRect tabArea, CallbackInfo ci) {
        //System.out.println(grid.getHeight());
    }
}
