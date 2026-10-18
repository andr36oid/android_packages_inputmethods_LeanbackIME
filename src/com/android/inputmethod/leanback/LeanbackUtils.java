/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.android.inputmethod.leanback;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.EditorInfo;

/**
 * This class contains common methods used by LeanbackImeService classes
 */
public class LeanbackUtils {

    private static final int ACCESSIBILITY_DELAY_MS = 250;
    private static final Handler sAccessibilityHandler = new Handler();

    /**
     * checks if the keyCode represents an alphabet char
     *
     * @return true if the keyCode represents an alphabet char
     */
    public static boolean isAlphabet(int keyCode) {
        if (Character.isLetter(keyCode)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * get the IME action of the current {@link EditText}
     */
    public static int getImeAction(EditorInfo attribute) {
        return attribute.imeOptions
                & (EditorInfo.IME_MASK_ACTION | EditorInfo.IME_FLAG_NO_ENTER_ACTION);
    }

    /**
     * get the input type class of the current {@link EditText}
     */
    public static int getInputTypeClass(EditorInfo attribute) {
        return attribute.inputType & EditorInfo.TYPE_MASK_CLASS;
    }

    /**
     * get the input type variation of the current {@link EditText}
     */
    public static int getInputTypeVariation(EditorInfo attribute) {
        return attribute.inputType & EditorInfo.TYPE_MASK_VARIATION;
    }

    /**
     * The system accent colour as the dark system theme uses it, so the keyboard follows the
     * accent picked in Settings > Personalization.
     */
    public static int getAccentColor(Context context) {
        final TypedArray a = new ContextThemeWrapper(context,
                android.R.style.Theme_DeviceDefault)
                .obtainStyledAttributes(new int[] { android.R.attr.colorAccent });
        try {
            return a.getColor(0, context.getColor(R.color.ime_accent_fallback));
        } finally {
            a.recycle();
        }
    }

    /** Black or white, whichever reads better on the given colour. */
    public static int getContrastColor(int color) {
        // Above this luminance black text has more contrast than white (WCAG ratio).
        return Color.luminance(color) > 0.179f ? Color.BLACK : Color.WHITE;
    }

    public static void sendAccessibilityEvent(final View view, boolean focusGained) {
        if (view != null && focusGained) {
            sAccessibilityHandler.removeCallbacksAndMessages(null);
            sAccessibilityHandler.postDelayed(new Runnable() {
                public void run() {
                    view.sendAccessibilityEvent(AccessibilityEvent.TYPE_ANNOUNCEMENT);
                }
            }, ACCESSIBILITY_DELAY_MS);
        }
    }
}
