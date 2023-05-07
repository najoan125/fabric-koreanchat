package com.hyfata.najoan.koreanpatch.mixin;

import java.util.Objects;
import java.util.function.Consumer;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.util.HangulProcessor;
import com.hyfata.najoan.koreanpatch.keyboard.KeyboardLayout;
import net.minecraft.client.MinecraftClient; //class_310
import net.minecraft.client.gui.widget.TextFieldWidget; //class_342
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen; //class_481
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={TextFieldWidget.class})
public abstract class TextFieldWidgetMixin {
    private Consumer<String> field_2088;
    private final MinecraftClient client = MinecraftClient.getInstance();

    @Shadow
    public abstract int getCursor();

    @Shadow
    public abstract void setCursor(int var1);

    @Shadow
    public abstract void eraseCharacters(int var1);

    @Shadow
    public abstract String getText();

    @Shadow
    public abstract void write(String var1);

    @Shadow
    protected abstract boolean isEditable();

    @Shadow
    public abstract void setCursorToEnd();

    @Shadow
    public abstract void onChanged(String var1);

    @Shadow
    public abstract void setText(String var1);

    @Inject(at={@At(value="HEAD")}, method={"charTyped(CI)Z"}, cancellable=true)
    public void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this.client.currentScreen != null && KoreanPatchClient.KOREAN && this.isEditable() && Character.charCount(chr) == 1) {
            char[] chars;
            cir.setReturnValue(Boolean.TRUE);
            for (char ch : chars = Character.toChars(chr)) {
                int qwertyIndex = this.getQwertyIndexCodePoint(ch);
                if (ch == ' ') {
                    this.writeText(String.valueOf(ch));
                    KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter(ch) ? this.getCursor() : -1;
                    continue;
                }
                if (qwertyIndex == -1) {
                    KeyboardLayout.INSTANCE.assemblePosition = -1;
                    continue;
                }
                Objects.requireNonNull(KeyboardLayout.INSTANCE);
                char curr = "`1234567890-=~!@#$%^&*()_+\u3142\u3148\u3137\u3131\u3145\u315b\u3155\u3151\u3150\u3154[]\\\u3143\u3149\u3138\u3132\u3146\u315b\u3155\u3151\u3152\u3156{}|\u3141\u3134\u3147\u3139\u314e\u3157\u3153\u314f\u3163;'\u3141\u3134\u3147\u3139\u314e\u3157\u3153\u314f\u3163:\"\u314b\u314c\u314a\u314d\u3160\u315c\u3161,./\u314b\u314c\u314a\u314d\u3160\u315c\u3161<>?".toCharArray()[qwertyIndex];
                int cursorPosition = this.getCursor();
                if (cursorPosition != 0 && HangulProcessor.isHangulCharacter(curr) && this.onHangulCharTyped(chr, modifiers)) continue;
                this.writeText(String.valueOf(curr));
                KeyboardLayout.INSTANCE.assemblePosition = HangulProcessor.isHangulCharacter(curr) ? this.getCursor() : -1;
            }
        }
    }

    public void writeText(String str) {
        this.write(str);
        this.sendTextChanged(str);
        this.onChanged(this.getText());
        this.updateScreen();
    }

    private void sendTextChanged(String str) {
        if (this.field_2088 != null) {
            this.field_2088.accept(str);
        }
    }

    private void updateScreen() {
        if (this.client.currentScreen == null) {
            return;
        }
        if (this.client.currentScreen instanceof CreativeInventoryScreen && !this.getText().isEmpty()) {
            ((CreativeInventoryScreenInvoker)this.client.currentScreen).updateCreativeSearch();
        }
    }

    public void modifyText(char ch) {
        int cursorPosition = this.getCursor();
        this.setCursor(cursorPosition - 1);
        this.eraseCharacters(1);
        this.writeText(String.valueOf(Character.toChars(ch)));
    }

    @Inject(at={@At(value="HEAD")}, method={"keyPressed(III)Z"}, cancellable=true)
    private void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfo) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen != null) {
            if (keyCode == KoreanPatchClient.KEYCODE || scanCode == KoreanPatchClient.SCANCODE) {
                boolean bl = KoreanPatchClient.KOREAN = !KoreanPatchClient.KOREAN;
            }
            if (keyCode == 259) {
                int cursorPosition = this.getCursor();
                if (cursorPosition == 0 || cursorPosition != KeyboardLayout.INSTANCE.assemblePosition) {
                    return;
                }
                String text = this.getText();
                char ch = text.toCharArray()[cursorPosition - 1];
                if (HangulProcessor.isHangulSyllables(ch)) {
                    int code = ch - 44032;
                    int cho = code / 588;
                    int jung = code % 588 / 28;
                    int jong = code % 588 % 28;
                    if (jong != 0) {
                        char[] ch_arr = KeyboardLayout.INSTANCE.jongsung_ref_table.get(jong).toCharArray();
                        if (ch_arr.length == 2) {
                            Objects.requireNonNull(KeyboardLayout.INSTANCE);
                            jong = "\u0000\u3131\u3132\u3133\u3134\u3135\u3136\u3137\u3139\u313a\u313b\u313c\u313d\u313e\u313f\u3140\u3141\u3142\u3144\u3145\u3146\u3147\u3148\u314a\u314b\u314c\u314d\u314e".indexOf(ch_arr[0]);
                        } else {
                            jong = 0;
                        }
                        char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, jong);
                        this.modifyText(c);
                    } else {
                        char[] ch_arr = KeyboardLayout.INSTANCE.jungsung_ref_table.get(jung).toCharArray();
                        if (ch_arr.length == 2) {
                            Objects.requireNonNull(KeyboardLayout.INSTANCE);
                            jung = "\u314f\u3150\u3151\u3152\u3153\u3154\u3155\u3156\u3157\u3158\u3159\u315a\u315b\u315c\u315d\u315e\u315f\u3160\u3161\u3162\u3163".indexOf(ch_arr[0]);
                            char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, 0);
                            this.modifyText(c);
                        } else {
                            Objects.requireNonNull(KeyboardLayout.INSTANCE);
                            char c = "\u3131\u3132\u3134\u3137\u3138\u3139\u3141\u3142\u3143\u3145\u3146\u3147\u3148\u3149\u314a\u314b\u314c\u314d\u314e".charAt(cho);
                            this.modifyText(c);
                        }
                    }
                    callbackInfo.setReturnValue(Boolean.TRUE);
                } else if (HangulProcessor.isHangulCharacter(ch)) {
                    KeyboardLayout.INSTANCE.assemblePosition = -1;
                }
            }
        }
    }

    private int getQwertyIndexCodePoint(char ch) {
        Objects.requireNonNull(KeyboardLayout.INSTANCE);
        return "`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?".indexOf(ch);
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
        int cursorPosition = this.getCursor();
        String text = this.getText();
        char prev = text.toCharArray()[cursorPosition - 1];
        Objects.requireNonNull(KeyboardLayout.INSTANCE);
        char curr = "`1234567890-=~!@#$%^&*()_+\u3142\u3148\u3137\u3131\u3145\u315b\u3155\u3151\u3150\u3154[]\\\u3143\u3149\u3138\u3132\u3146\u315b\u3155\u3151\u3152\u3156{}|\u3141\u3134\u3147\u3139\u314e\u3157\u3153\u314f\u3163;'\u3141\u3134\u3147\u3139\u314e\u3157\u3153\u314f\u3163:\"\u314b\u314c\u314a\u314d\u3160\u315c\u3161,./\u314b\u314c\u314a\u314d\u3160\u315c\u3161<>?".toCharArray()[idx];
        if (cursorPosition == 0) {
            if (!HangulProcessor.isHangulCharacter(curr)) {
                return false;
            }
            this.writeText(String.valueOf(curr));
            KeyboardLayout.INSTANCE.assemblePosition = this.getCursor();
        } else if (cursorPosition == KeyboardLayout.INSTANCE.assemblePosition) {
            if (HangulProcessor.isJaeum(prev) && HangulProcessor.isMoeum(curr)) {
                Objects.requireNonNull(KeyboardLayout.INSTANCE);
                int cho = "\u3131\u3132\u3134\u3137\u3138\u3139\u3141\u3142\u3143\u3145\u3146\u3147\u3148\u3149\u314a\u314b\u314c\u314d\u314e".indexOf(prev);
                Objects.requireNonNull(KeyboardLayout.INSTANCE);
                int jung = "\u314f\u3150\u3151\u3152\u3153\u3154\u3155\u3156\u3157\u3158\u3159\u315a\u315b\u315c\u315d\u315e\u315f\u3160\u3161\u3162\u3163".indexOf(curr);
                char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, 0);
                this.modifyText(c);
                KeyboardLayout.INSTANCE.assemblePosition = this.getCursor();
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
                    KeyboardLayout.INSTANCE.assemblePosition = this.getCursor();
                    return true;
                }
                if (jong == 0 && HangulProcessor.isJongsung(curr)) {
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, HangulProcessor.getJongsung(curr));
                    this.modifyText(c);
                    KeyboardLayout.INSTANCE.assemblePosition = this.getCursor();
                    return true;
                }
                if (jong != 0 && HangulProcessor.isJongsung(prev, curr)) {
                    jong = HangulProcessor.getJongsung(prev, curr);
                    char c = HangulProcessor.synthesizeHangulCharacter(cho, jung, jong);
                    this.modifyText(c);
                    KeyboardLayout.INSTANCE.assemblePosition = this.getCursor();
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
                    KeyboardLayout.INSTANCE.assemblePosition = this.getCursor();
                    return true;
                }
            }
        }
        this.writeText(String.valueOf(curr));
        KeyboardLayout.INSTANCE.assemblePosition = this.getCursor();
        return true;
    }
}

