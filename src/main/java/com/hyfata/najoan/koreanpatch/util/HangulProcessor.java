package com.hyfata.najoan.koreanpatch.util;

import com.google.common.base.Splitter;

import java.util.List;

public class HangulProcessor {
    static String chosungTable = "\u3131\u3132\u3134\u3137\u3138\u3139\u3141\u3142\u3143\u3145\u3146\u3147\u3148\u3149\u314a\u314b\u314c\u314d\u314e";
    static String jungsungTable = "\u314f\u3150\u3151\u3152\u3153\u3154\u3155\u3156\u3157\u3158\u3159\u315a\u315b\u315c\u315d\u315e\u315f\u3160\u3161\u3162\u3163";
    static String jongsungTable = "\u0000\u3131\u3132\u3133\u3134\u3135\u3136\u3137\u3139\u313a\u313b\u313c\u313d\u313e\u313f\u3140\u3141\u3142\u3144\u3145\u3146\u3147\u3148\u314a\u314b\u314c\u314d\u314e";
    static List<String> jungsungCombiTable = Splitter.on((String)",").splitToList((CharSequence)",,,,,,,,,\u3157\u314f,\u3157\u3150,\u3157\u3163,,,\u315c\u3153,\u315c\u3154,\u315c\u3163,,,\u3161\u3163,\u3163");
    static List<String> jongsungCombiTable = Splitter.on((String)",").splitToList((CharSequence)",,,\u3131\u3145,,\u3134\u3148,\u3134\u314e,,,\u3139\u3131,\u3139\u3141,\u3139\u3142,\u3139\u3145,\u3139\u314c,\u3139\u314d,\u3139\u314e,,,\u3142\u3145,,,,,,,,,");

    public static boolean isJaeum(char c) {
        return c >= 0x3131 && c <= 0x314E;
    }

    public static boolean isMoeum(char c) {
        return c >= 0x314F && c <= 0x3163;
    }

    public static boolean isChosung(char c) {
        return getChosung(c) != -1;
    }

    public static boolean isJungsung(char c) {
        return getJungsung(c) != -1;
    }

    public static boolean isJungsung(char p, char c) {
        return getJungsung(p, c) != -1;
    }

    public static boolean isJongsung(char c) {
        return getJongsung(c) != -1;
    }

    public static boolean isJongsung(char p, char c) {
        return getJongsung(p, c) != -1;
    }

    public static int getChosung(char c) {
        return chosungTable.indexOf(c);
    }

    public static int getJungsung(char c) {
        return jungsungTable.indexOf(c);
    }

    public static int getJungsung(char p, char c) {
        int jung = ((p - 0xAC00) % (21 * 28)) / 28;

        for (int i = 0; i < jungsungCombiTable.size(); i++) {
            char[] tbl = jungsungCombiTable.get(i).toCharArray();
            if (tbl.length == 2 && tbl[0] == jungsungTable.charAt(jung) && tbl[1] == c) {
                return i;
            }
        }
        return -1;
    }

    public static int getJongsung(char c) {
        return jongsungTable.indexOf(c);
    }

    public static int getJongsung(char p, char c) {
        int jong = ((p - 0xAC00) % (21 * 28)) % 28;

        for (int i = 0; i < jongsungCombiTable.size(); i++) {
            char[] tbl = jongsungCombiTable.get(i).toCharArray();
            if (tbl.length == 2 && tbl[0] == jongsungTable.charAt(jong) && tbl[1] == c) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isHangulSyllables(char c) {
        return c >= 0xAC00 && c <= 0xD7AF;
    }

    public static boolean isHangulCharacter(char c) {
        return isJaeum(c) || isMoeum(c) || isHangulSyllables(c);
    }

    public static char synthesizeHangulCharacter(int cho, int jung, int jong) {
        return (char)(44032 + cho * 28 * 21 + jung * 28 + jong);
    }
}
