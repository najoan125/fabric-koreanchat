package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.keyboard.KeyboardLayout;
import com.hyfata.najoan.koreanpatch.keyboard.QwertyLayout;
import com.hyfata.najoan.koreanpatch.util.HangulProcessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.JigsawBlockScreen;
import net.minecraft.client.gui.screen.ingame.StructureBlockScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(value={TextFieldWidget.class})
public abstract class TextFieldWidgetMixin {
    @Shadow
    private Consumer<String> changedListener;
    @Unique
    private final MinecraftClient client = MinecraftClient.getInstance();

    @Shadow
    public abstract int getCursor();

    @Shadow
    public abstract void setCursor(int var1, boolean shift);

    @Shadow
    public abstract void eraseCharacters(int var1);

    @Shadow
    public abstract String getText();

    @Shadow
    public abstract void write(String var1);

    @Shadow
    protected abstract boolean isEditable();

    @Shadow
    protected abstract void onChanged(String var1);

    @Shadow
    public abstract void setText(String var1);

    @Unique
    public void writeText(String str) {
        this.write(str);
        this.sendTextChanged(str);
        this.onChanged(this.getText());
        this.updateScreen();
    }

    @Unique
    private void sendTextChanged(String str) {
        if (this.changedListener != null) {
            this.changedListener.accept(str);
        }
    }

    @Unique
    private void updateScreen() {
        if (this.client.currentScreen == null) {
            return;
        }
        if (this.client.currentScreen instanceof CreativeInventoryScreen && !this.getText().isEmpty()) {
            ((CreativeInventoryScreenInvoker)this.client.currentScreen).updateCreativeSearch();
        }
    }

    @Unique
    public void modifyText(char ch) {
        int cursorPosition = this.getCursor();
        this.setCursor(cursorPosition - 1, false);
        this.eraseCharacters(1);
        this.writeText(String.valueOf(Character.toChars(ch)));
    }

    @Unique
    boolean onBackspaceKeyPressed() {
        int cursorPosition = this.getCursor();
        if (cursorPosition == 0 || cursorPosition != KeyboardLayout.INSTANCE.assemblePosition) return false;

        String text = this.getText();

        char ch = text.toCharArray()[cursorPosition - 1];

        if (HangulProcessor.isHangulSyllables(ch)) {
            int code = ch - 0xAC00;
            int cho = code / (21 * 28);
            int jung = (code % (21 * 28)) / 28;
            int jong = (code % (21 * 28)) % 28;

            if (jong != 0) {
                char[] ch_arr = KeyboardLayout.INSTANCE.jongsung_ref_table.get(jong).toCharArray();
                if (ch_arr.length == 2) {
                    jong = KeyboardLayout.INSTANCE.jongsung_table.indexOf(ch_arr[0]);
                } else {
                    jong = 0;
                }
                char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, jong);
                this.modifyText(c);
                return true;
            } else {
                char[] ch_arr = KeyboardLayout.INSTANCE.jungsung_ref_table.get(jung).toCharArray();
                if (ch_arr.length == 2) {
                    jung = KeyboardLayout.INSTANCE.jungsung_table.indexOf(ch_arr[0]);
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, 0);
                    this.modifyText(c);
                    return true;
                } else {
                    char c = KeyboardLayout.INSTANCE.chosung_table.charAt(cho);
                    this.modifyText(c);
                    return true;
                }
            }
        } else if (HangulProcessor.isHangulCharacter(ch)) {
            KeyboardLayout.INSTANCE.assemblePosition = -1;
            return false;
        }
        return false;
    }

    @Unique
    boolean onHangulCharTyped(int keyCode, int modifiers) {
        boolean shift = (modifiers & 0x01) == 1;

        int codePoint = keyCode;

        if (codePoint >= 65 && codePoint <= 90) {
            codePoint += 32;
        }

        if (codePoint >= 97 && codePoint <= 122) {
            if (shift) {
                codePoint -= 32;
            }
        }

        int idx = QwertyLayout.getInstance().getLayoutString().indexOf(codePoint);
        // System.out.println(String.format("idx: %d", idx));
        if (idx == -1) {
            KeyboardLayout.INSTANCE.assemblePosition = -1;
            return false;
        }

        int cursorPosition = this.getCursor();
        String text = this.getText();

        char prev = text.toCharArray()[cursorPosition - 1];
        char curr = KeyboardLayout.INSTANCE.layout.toCharArray()[idx];

        if (cursorPosition == 0) {
            if (!HangulProcessor.isHangulCharacter(curr)) return false;

            this.writeText(String.valueOf(curr));
            KeyboardLayout.INSTANCE.assemblePosition = this.getCursor();
        }
        else if (cursorPosition == KeyboardLayout.INSTANCE.assemblePosition) {

            // 자음 + 모음
            if (HangulProcessor.isJaeum(prev) && HangulProcessor.isMoeum(curr)) {
                int cho = KeyboardLayout.INSTANCE.chosung_table.indexOf(prev);
                int jung = KeyboardLayout.INSTANCE.jungsung_table.indexOf(curr);
                char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, 0);
                this.modifyText(c);
                KeyboardLayout.INSTANCE.assemblePosition = this.getCursor();
                return true;
            }

            if (HangulProcessor.isHangulSyllables(prev)) {
                int code = prev - 0xAC00;
                int cho = code / (21 * 28);
                int jung = (code % (21 * 28)) / 28;
                int jong = (code % (21 * 28)) % 28;

                // 중성 합성 (ㅘ, ㅙ)..
                if (jong == 0 && HangulProcessor.isJungsung(prev, curr)) {
                    jung = HangulProcessor.getJungsung(prev, curr);
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, 0);
                    this.modifyText(c);
                    KeyboardLayout.INSTANCE.assemblePosition = this.getCursor();
                    return true;
                }

                // 종성 추가
                if (jong == 0 && HangulProcessor.isJongsung(curr)) {
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, HangulProcessor.getJongsung(curr));
                    this.modifyText(c);
                    KeyboardLayout.INSTANCE.assemblePosition = this.getCursor();
                    return true;
                }

                // 종성 받침 추가
                if (jong != 0 && HangulProcessor.isJongsung(prev, curr)) {
                    jong = HangulProcessor.getJongsung(prev, curr);
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, jong);
                    this.modifyText(c);
                    KeyboardLayout.INSTANCE.assemblePosition = this.getCursor();
                    return true;
                }

                // 종성에서 받침 하나 빼고 글자 만들기
                if (jong != 0 && HangulProcessor.isJungsung(curr)) {
                    char[] tbl = KeyboardLayout.INSTANCE.jongsung_ref_table.get(jong).toCharArray();
                    int newCho = 0;
                    if (tbl.length == 2) {
                        newCho = KeyboardLayout.INSTANCE.chosung_table.indexOf(tbl[1]);
                        jong = KeyboardLayout.INSTANCE.jongsung_table.indexOf(tbl[0]);
                    } else {
                        newCho = KeyboardLayout.INSTANCE.chosung_table.indexOf(KeyboardLayout.INSTANCE.jongsung_table.charAt(jong));
                        jong = 0;
                    }

                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, jong);
                    this.modifyText(c);

                    cho = newCho;
                    jung = KeyboardLayout.INSTANCE.jungsung_table.indexOf(curr);
                    code = HangulProcessor.synthesizeHangulCharacter(cho, jung, 0);
                    this.writeText(String.valueOf(Character.toChars(code)));
                    KeyboardLayout.INSTANCE.assemblePosition = this.getCursor();
                    return true;
                }
            }
        }

        this.writeText(String.valueOf(curr));
        KeyboardLayout.INSTANCE.assemblePosition = this.getCursor();
        return true;
    }

    @Unique
    public void typedTextField(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        int qwertyIndex = KeyboardLayout.INSTANCE.getQwertyIndexCodePoint(chr);
        if (qwertyIndex == -1) {
            KeyboardLayout.INSTANCE.assemblePosition = -1;
            return;
        }

        cir.setReturnValue(Boolean.TRUE);


        char curr = KeyboardLayout.INSTANCE.layout.toCharArray()[qwertyIndex];
        if (this.getCursor() == 0 || !HangulProcessor.isHangulCharacter(curr) || !onHangulCharTyped(chr, modifiers)) {
            //Caps Lock/한글 상태면 쌍자음으로 입력되는 문제 수정
            if (HangulProcessor.isHangulCharacter(curr)) {
                boolean shift = (modifiers & 0x01) == 1;
                int codePoint = chr;

                if (codePoint >= 65 && codePoint <= 90) {
                    codePoint += 32;
                }

                if (codePoint >= 97 && codePoint <= 122) {
                    if (shift) {
                        codePoint -= 32;
                    }
                }
                int idx = QwertyLayout.getInstance().getLayoutString().indexOf(codePoint);
                if (idx != -1) {
                    curr = KeyboardLayout.INSTANCE.layout.toCharArray()[idx];
                }
            } //모두 소문자로 돌린 다음 shift modifier에 따라서 적절하게 처리

            this.writeText(String.valueOf(curr));
            KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter((curr)) ? this.getCursor() : -1;
        }
    }

    @Inject(at={@At(value="HEAD")}, method={"charTyped(CI)Z"}, cancellable=true)
    public void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this.client.currentScreen != null &&
                !(this.client.currentScreen instanceof JigsawBlockScreen) &&
                !(this.client.currentScreen instanceof StructureBlockScreen) &&
                KoreanPatchClient.KOREAN && this.isEditable() && Character.charCount(chr) == 1)
        {
            typedTextField(chr, modifiers, cir);
        }
    }

    @Inject(at={@At(value="HEAD")}, method={"keyPressed(III)Z"}, cancellable=true)
    private void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfo) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen != null &&
                !(client.currentScreen instanceof JigsawBlockScreen) &&
                !(client.currentScreen instanceof StructureBlockScreen))
        {
            if (keyCode == KoreanPatchClient.KEYCODE || scanCode == KoreanPatchClient.SCANCODE) {
                KoreanPatchClient.KOREAN = !KoreanPatchClient.KOREAN;
            }
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (onBackspaceKeyPressed()) {
                    callbackInfo.setReturnValue(Boolean.TRUE);
                }
            }
        }
    }
}

