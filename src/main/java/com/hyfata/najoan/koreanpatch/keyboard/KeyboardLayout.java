package com.hyfata.najoan.koreanpatch.keyboard;

import com.google.common.base.Splitter;
import java.util.List;

public class KeyboardLayout {
    public static final KeyboardLayout INSTANCE = new KeyboardLayout();
    public final String qwerty_layout = "`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?";
    public final String layout = "`1234567890-=~!@#$%^&*()_+\u3142\u3148\u3137\u3131\u3145\u315b\u3155\u3151\u3150\u3154[]\\\u3143\u3149\u3138\u3132\u3146\u315b\u3155\u3151\u3152\u3156{}|\u3141\u3134\u3147\u3139\u314e\u3157\u3153\u314f\u3163;'\u3141\u3134\u3147\u3139\u314e\u3157\u3153\u314f\u3163:\"\u314b\u314c\u314a\u314d\u3160\u315c\u3161,./\u314b\u314c\u314a\u314d\u3160\u315c\u3161<>?";
    public final String chosung_table = "\u3131\u3132\u3134\u3137\u3138\u3139\u3141\u3142\u3143\u3145\u3146\u3147\u3148\u3149\u314a\u314b\u314c\u314d\u314e";
    public final String jungsung_table = "\u314f\u3150\u3151\u3152\u3153\u3154\u3155\u3156\u3157\u3158\u3159\u315a\u315b\u315c\u315d\u315e\u315f\u3160\u3161\u3162\u3163";
    public final String jongsung_table = "\u0000\u3131\u3132\u3133\u3134\u3135\u3136\u3137\u3139\u313a\u313b\u313c\u313d\u313e\u313f\u3140\u3141\u3142\u3144\u3145\u3146\u3147\u3148\u314a\u314b\u314c\u314d\u314e";
    public final List<String> jungsung_ref_table = Splitter.on((String)",").splitToList((CharSequence)",,,,,,,,,\u3157\u314f,\u3157\u3150,\u3157\u3163,,,\u315c\u3153,\u315c\u3154,\u315c\u3163,,,\u3161\u3163,\u3163");
    public final List<String> jongsung_ref_table = Splitter.on((String)",").splitToList((CharSequence)",,,\u3131\u3145,,\u3134\u3148,\u3134\u314e,,,\u3139\u3131,\u3139\u3141,\u3139\u3142,\u3139\u3145,\u3139\u314c,\u3139\u314d,\u3139\u314e,,,\u3142\u3145,,,,,,,,,");
    public final String chosung_modifiers_table = "\u3132\u3138\u3143\u3146\u3149";
    public int assemblePosition = -1;
}

