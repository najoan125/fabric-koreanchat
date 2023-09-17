package com.hyfata.najoan.koreanpatch.mixin;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.keyboard.KeyboardLayout;
import com.hyfata.najoan.koreanpatch.keyboard.QwertyLayout;
import com.hyfata.najoan.koreanpatch.util.HangulProcessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.SelectionManager;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={SelectionManager.class})
public abstract class SelectionManagerMixin {
    @Shadow
    private int selectionStart;
    @Shadow
    private int selectionEnd;
    @Shadow
    @Final
    private Supplier<String> stringGetter;
    @Shadow
    @Final
    private Predicate<String> stringFilter;
    @Shadow
    @Final
    private Consumer<String> stringSetter;
    private final MinecraftClient client = MinecraftClient.getInstance();

    @Inject(at={@At(value="HEAD")}, method={"insert(C)Z"}, cancellable=true)
    public void insertChar(char chr, CallbackInfoReturnable<Boolean> cir) {
        if (this.client.currentScreen != null && KoreanPatchClient.KOREAN) {
            cir.setReturnValue(Boolean.TRUE);
            if (chr == ' ') {
                this.writeText(String.valueOf(chr));
                KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter(chr) ? this.selectionEnd : -1;
                return;
            }
            int qwertyIndex = KeyboardLayout.INSTANCE.getQwertyIndexCodePoint(chr);
            if (qwertyIndex == -1) {
                KeyboardLayout.INSTANCE.assemblePosition = -1;
                return;
            }
            Objects.requireNonNull(KeyboardLayout.INSTANCE);
            char curr = KeyboardLayout.INSTANCE.layout.toCharArray()[qwertyIndex];
            int cursorPosition = this.selectionEnd;
            int modifiers = this.getModifiers();
            if (cursorPosition == 0 || !HangulProcessor.isHangulCharacter(curr) || !this.onHangulCharTyped(chr, modifiers)) {
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
                KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter(curr) ? this.selectionEnd : -1;
            }
        }
    }

    @Inject(at={@At(value="HEAD")}, method={"insert(Ljava/lang/String;)V"}, cancellable=true)
    public void insertString(String string, CallbackInfo ci) {
        for (char chr : string.toCharArray()) {
            if (this.client.currentScreen == null || !KoreanPatchClient.KOREAN) continue;
            ci.cancel();
            if (chr == ' ') {
                this.writeText(String.valueOf(chr));
                KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter(chr) ? this.selectionEnd : -1;
                continue;
            }
            int qwertyIndex = KeyboardLayout.INSTANCE.getQwertyIndexCodePoint(chr);
            if (qwertyIndex == -1) {
                KeyboardLayout.INSTANCE.assemblePosition = -1;
                continue;
            }
            Objects.requireNonNull(KeyboardLayout.INSTANCE);
            char curr = KeyboardLayout.INSTANCE.layout.toCharArray()[qwertyIndex];
            int cursorPosition = this.selectionEnd;
            int modifiers = this.getModifiers();
            if (cursorPosition != 0 && HangulProcessor.isHangulCharacter(curr) && this.onHangulCharTyped(chr, modifiers)) continue;
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
            KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter(curr) ? this.selectionEnd : -1;
        }
    }

    private int getModifiers() {
        boolean shift = InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) ||
                InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT);
        if (shift) {
            return 1;
        }
        return 0;
    }

    public String getText() {
        return this.stringGetter.get();
    }

    public void writeText(String str) {
        int cursorPosition = this.selectionEnd;
        String s = this.getText();
        String res = cursorPosition > 0 ? s.substring(0, cursorPosition) + str + s.substring(cursorPosition) : str + s;
        boolean textCommitted = this.setText(res);
        if (textCommitted && this.getText().length() == res.length()) {
            this.selectionEnd = cursorPosition + 1;
            this.selectionStart = cursorPosition + 1;
        }
    }

    public boolean setText(String str) {
        if (this.stringFilter.test(str)) {
            this.stringSetter.accept(str);
            return true;
        }
        return false;
    }

    public void modifyText(char ch) {
        int cursorPosition = this.selectionEnd;
        char[] arr = this.getText().toCharArray();
        if (cursorPosition > 0 && cursorPosition <= arr.length) {
            arr[cursorPosition - 1] = ch;
            this.setText(String.valueOf(arr));
        }
    }

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

        int cursorPosition = this.selectionEnd;
        String text = this.getText();

        char prev = text.toCharArray()[cursorPosition - 1];
        char curr = KeyboardLayout.INSTANCE.layout.toCharArray()[idx];

        if (cursorPosition == 0) {
            if (!HangulProcessor.isHangulCharacter(curr)) return false;

            this.writeText(String.valueOf(curr));
            KeyboardLayout.INSTANCE.assemblePosition = this.selectionEnd;
        }
        else if (cursorPosition == KeyboardLayout.INSTANCE.assemblePosition) {

            // 자음 + 모음
            if (HangulProcessor.isJaeum(prev) && HangulProcessor.isMoeum(curr)) {
                int cho = KeyboardLayout.INSTANCE.chosung_table.indexOf(prev);
                int jung = KeyboardLayout.INSTANCE.jungsung_table.indexOf(curr);
                char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, 0);
                this.modifyText(c);
                KeyboardLayout.INSTANCE.assemblePosition = this.selectionEnd;
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
                    KeyboardLayout.INSTANCE.assemblePosition = this.selectionEnd;
                    return true;
                }

                // 종성 추가
                if (jong == 0 && HangulProcessor.isJongsung(curr)) {
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, HangulProcessor.getJongsung(curr));
                    this.modifyText(c);
                    KeyboardLayout.INSTANCE.assemblePosition = this.selectionEnd;
                    return true;
                }

                // 종성 받침 추가
                if (jong != 0 && HangulProcessor.isJongsung(prev, curr)) {
                    jong = HangulProcessor.getJongsung(prev, curr);
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, jong);
                    this.modifyText(c);
                    KeyboardLayout.INSTANCE.assemblePosition = this.selectionEnd;
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
                    KeyboardLayout.INSTANCE.assemblePosition = this.selectionEnd;
                    return true;
                }
            }
        }

        this.writeText(String.valueOf(curr));
        KeyboardLayout.INSTANCE.assemblePosition = this.selectionEnd;
        return true;
    }
}

