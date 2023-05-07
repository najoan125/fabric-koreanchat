package com.hyfata.najoan.koreanpatch.mixin;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.keyboard.KeyboardLayout;
import com.hyfata.najoan.koreanpatch.util.HangulProcessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.SelectionManager;
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
            int qwertyIndex = this.getQwertyIndexCodePoint(chr);
            if (qwertyIndex == -1) {
                KeyboardLayout.INSTANCE.assemblePosition = -1;
                return;
            }
            Objects.requireNonNull(KeyboardLayout.INSTANCE);
            char curr = "`1234567890-=~!@#$%^&*()_+\u3142\u3148\u3137\u3131\u3145\u315b\u3155\u3151\u3150\u3154[]\\\u3143\u3149\u3138\u3132\u3146\u315b\u3155\u3151\u3152\u3156{}|\u3141\u3134\u3147\u3139\u314e\u3157\u3153\u314f\u3163;'\u3141\u3134\u3147\u3139\u314e\u3157\u3153\u314f\u3163:\"\u314b\u314c\u314a\u314d\u3160\u315c\u3161,./\u314b\u314c\u314a\u314d\u3160\u315c\u3161<>?".toCharArray()[qwertyIndex];
            int cursorPosition = this.selectionEnd;
            int modifiers = this.getChosungModifiersIndexCodePoint(curr);
            if (cursorPosition == 0 || !HangulProcessor.isHangulCharacter(curr) || !this.onHangulCharTyped(chr, modifiers)) {
                this.writeText(String.valueOf(curr));
                KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter(curr) ? this.selectionEnd : -1;
            }
        }
    }

    @Inject(at={@At(value="HEAD")}, method={"insert(Ljava/lang/String;)V"}, cancellable=true)
    public void insertString(String string, CallbackInfo ci) {
        char[] chrs;
        for (char chr : chrs = string.toCharArray()) {
            if (this.client.currentScreen == null || !KoreanPatchClient.KOREAN) continue;
            ci.cancel();
            if (chr == ' ') {
                this.writeText(String.valueOf(chr));
                KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter(chr) ? this.selectionEnd : -1;
                continue;
            }
            int qwertyIndex = this.getQwertyIndexCodePoint(chr);
            if (qwertyIndex == -1) {
                KeyboardLayout.INSTANCE.assemblePosition = -1;
                continue;
            }
            Objects.requireNonNull(KeyboardLayout.INSTANCE);
            char curr = "`1234567890-=~!@#$%^&*()_+\u3142\u3148\u3137\u3131\u3145\u315b\u3155\u3151\u3150\u3154[]\\\u3143\u3149\u3138\u3132\u3146\u315b\u3155\u3151\u3152\u3156{}|\u3141\u3134\u3147\u3139\u314e\u3157\u3153\u314f\u3163;'\u3141\u3134\u3147\u3139\u314e\u3157\u3153\u314f\u3163:\"\u314b\u314c\u314a\u314d\u3160\u315c\u3161,./\u314b\u314c\u314a\u314d\u3160\u315c\u3161<>?".toCharArray()[qwertyIndex];
            int cursorPosition = this.selectionEnd;
            int modifiers = this.getChosungModifiersIndexCodePoint(curr);
            if (cursorPosition != 0 && HangulProcessor.isHangulCharacter(curr) && this.onHangulCharTyped(chr, modifiers)) continue;
            this.writeText(String.valueOf(curr));
            KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter(curr) ? this.selectionEnd : -1;
        }
    }

    private int getQwertyIndexCodePoint(char ch) {
        Objects.requireNonNull(KeyboardLayout.INSTANCE);
        return "`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?".indexOf(ch);
    }

    private int getChosungModifiersIndexCodePoint(char curr) {
        Objects.requireNonNull(KeyboardLayout.INSTANCE);
        int idx = "\u3132\u3138\u3143\u3146\u3149".indexOf(curr);
        if (idx > -1) {
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
        boolean shift = (modifiers & 1) == 1;
        int codePoint = keyCode;
        if (codePoint >= 65 && codePoint <= 90) {
            codePoint += 32;
        }
        if (codePoint >= 97 && codePoint <= 122 && shift) {
            codePoint -= 32;
        }
        Objects.requireNonNull(KeyboardLayout.INSTANCE);
        int idx = "`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?".indexOf(codePoint);
        if (idx == -1) {
            KeyboardLayout.INSTANCE.assemblePosition = -1;
            return false;
        }
        int cursorPosition = this.selectionEnd;
        String text = this.getText();
        char prev = text.toCharArray()[cursorPosition - 1];
        Objects.requireNonNull(KeyboardLayout.INSTANCE);
        char curr = "`1234567890-=~!@#$%^&*()_+\u3142\u3148\u3137\u3131\u3145\u315b\u3155\u3151\u3150\u3154[]\\\u3143\u3149\u3138\u3132\u3146\u315b\u3155\u3151\u3152\u3156{}|\u3141\u3134\u3147\u3139\u314e\u3157\u3153\u314f\u3163;'\u3141\u3134\u3147\u3139\u314e\u3157\u3153\u314f\u3163:\"\u314b\u314c\u314a\u314d\u3160\u315c\u3161,./\u314b\u314c\u314a\u314d\u3160\u315c\u3161<>?".toCharArray()[idx];
        if (cursorPosition == 0) {
            if (!HangulProcessor.isHangulCharacter(curr)) {
                return false;
            }
            this.writeText(String.valueOf(curr));
            KeyboardLayout.INSTANCE.assemblePosition = this.selectionEnd;
        } else if (cursorPosition == KeyboardLayout.INSTANCE.assemblePosition) {
            if (HangulProcessor.isJaeum(prev) && HangulProcessor.isMoeum(curr)) {
                Objects.requireNonNull(KeyboardLayout.INSTANCE);
                int cho = "\u3131\u3132\u3134\u3137\u3138\u3139\u3141\u3142\u3143\u3145\u3146\u3147\u3148\u3149\u314a\u314b\u314c\u314d\u314e".indexOf(prev);
                Objects.requireNonNull(KeyboardLayout.INSTANCE);
                int jung = "\u314f\u3150\u3151\u3152\u3153\u3154\u3155\u3156\u3157\u3158\u3159\u315a\u315b\u315c\u315d\u315e\u315f\u3160\u3161\u3162\u3163".indexOf(curr);
                char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, 0);
                this.modifyText(c);
                KeyboardLayout.INSTANCE.assemblePosition = this.selectionEnd;
                return true;
            }
            if (HangulProcessor.isHangulSyllables(prev)) {
                int code = prev - 44032;
                int cho = code / 588;
                int jung = code % 588 / 28;
                int jong = code % 588 % 28;
                if (jong == 0 && HangulProcessor.isJungsung(prev, curr)) {
                    jung = HangulProcessor.getJungsung(prev, curr);
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, 0);
                    this.modifyText(c);
                    KeyboardLayout.INSTANCE.assemblePosition = this.selectionEnd;
                    return true;
                }
                if (jong == 0 && HangulProcessor.isJongsung(curr)) {
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, HangulProcessor.getJongsung(curr));
                    this.modifyText(c);
                    KeyboardLayout.INSTANCE.assemblePosition = this.selectionEnd;
                    return true;
                }
                if (jong != 0 && HangulProcessor.isJongsung(prev, curr)) {
                    jong = HangulProcessor.getJongsung(prev, curr);
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, jong);
                    this.modifyText(c);
                    KeyboardLayout.INSTANCE.assemblePosition = this.selectionEnd;
                    return true;
                }
                if (jong != 0 && HangulProcessor.isJungsung(curr)) {
                    int newCho;
                    char[] tbl = KeyboardLayout.INSTANCE.jongsung_ref_table.get(jong).toCharArray();
                    if (tbl.length == 2) {
                        Objects.requireNonNull(KeyboardLayout.INSTANCE);
                        newCho = "\u3131\u3132\u3134\u3137\u3138\u3139\u3141\u3142\u3143\u3145\u3146\u3147\u3148\u3149\u314a\u314b\u314c\u314d\u314e".indexOf(tbl[1]);
                        Objects.requireNonNull(KeyboardLayout.INSTANCE);
                        jong = "\u0000\u3131\u3132\u3133\u3134\u3135\u3136\u3137\u3139\u313a\u313b\u313c\u313d\u313e\u313f\u3140\u3141\u3142\u3144\u3145\u3146\u3147\u3148\u314a\u314b\u314c\u314d\u314e".indexOf(tbl[0]);
                    } else {
                        Objects.requireNonNull(KeyboardLayout.INSTANCE);
                        Objects.requireNonNull(KeyboardLayout.INSTANCE);
                        newCho = "\u3131\u3132\u3134\u3137\u3138\u3139\u3141\u3142\u3143\u3145\u3146\u3147\u3148\u3149\u314a\u314b\u314c\u314d\u314e".indexOf("\u0000\u3131\u3132\u3133\u3134\u3135\u3136\u3137\u3139\u313a\u313b\u313c\u313d\u313e\u313f\u3140\u3141\u3142\u3144\u3145\u3146\u3147\u3148\u314a\u314b\u314c\u314d\u314e".charAt(jong));
                        jong = 0;
                    }
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, jong);
                    this.modifyText(c);
                    cho = newCho;
                    Objects.requireNonNull(KeyboardLayout.INSTANCE);
                    jung = "\u314f\u3150\u3151\u3152\u3153\u3154\u3155\u3156\u3157\u3158\u3159\u315a\u315b\u315c\u315d\u315e\u315f\u3160\u3161\u3162\u3163".indexOf(curr);
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

