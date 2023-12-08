package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.util.Indicator;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {CreateWorldScreen.class})
public class CreateWorldScreenMixin extends Screen {
    protected CreateWorldScreenMixin(Text title) {
        super(title);
    }
    @Shadow
    private GridWidget grid;
    @Shadow
    private TabNavigationWidget tabNavigation;

    @Inject(at = {@At(value="RETURN")}, method = {"render"})
    private void addCustomLabel(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        if (KoreanPatchClient.currentIndex == 0) {
            int gridHeight = 117; //GridScreenTabMixin.java

            // from GameTab class in CreateWorldScreen
            int width = 208;
            int space = 8;
            int worldNameFieldHeight = 20;

            // initTabNavigation()
            int i = tabNavigation.getNavigationFocus().getBottom();
            ScreenRect screenRect = new ScreenRect(0, i, this.width, this.grid.getY() - i);

            // SimplePositioningWidget.class (Found GameTab extends GridScreenTab)
            int pos = (int) MathHelper.lerp(0.16666667F, 0.0F, (float)(screenRect.height() - gridHeight));
            Indicator.showIndicator(context, this.width / 2 - width / 2 - 10, pos + screenRect.getTop() + space + worldNameFieldHeight / 2 + 1, true);
        }
    }
}
