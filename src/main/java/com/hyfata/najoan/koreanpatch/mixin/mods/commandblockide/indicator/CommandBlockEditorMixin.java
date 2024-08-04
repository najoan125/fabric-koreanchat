package com.hyfata.najoan.koreanpatch.mixin.mods.commandblockide.indicator;

import arm32x.minecraft.commandblockide.client.gui.editor.CommandBlockEditor;
import arm32x.minecraft.commandblockide.client.gui.editor.CommandEditor;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandBlockEditor.class)
public abstract class CommandBlockEditorMixin extends CommandEditor {
    @Shadow
    @Final
    private TextFieldWidget lastOutputField;

    public CommandBlockEditorMixin(Screen screen, TextRenderer textRenderer, int x, int y, int width, int height, int leftPadding, int rightPadding, int index) {
        super(screen, textRenderer, x, y, width, height, leftPadding, rightPadding, index);
    }

    @Inject(method = "renderCommandField", at = @At("HEAD"))
    private void renderCommandField(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        lastOutputField.setX(this.commandField.getX());
        lastOutputField.setWidth(this.commandField.getWidth());
    }
}
