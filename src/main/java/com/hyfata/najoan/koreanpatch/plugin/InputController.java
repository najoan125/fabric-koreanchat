package com.hyfata.najoan.koreanpatch.plugin;

import com.hyfata.najoan.koreanpatch.arch.win.WinController;
import com.sun.jna.Platform;

public interface InputController {
    void setFocus(final boolean focus);
    void toggleFocus();

    static InputController newController() {
        if (Platform.isWindows()) {
            return new WinController();
        }
        return null;
    }
}
