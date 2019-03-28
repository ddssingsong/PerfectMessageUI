package com.dds.chatinput.menu.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * use XhsEmotionsKeyboard(https://github.com/w446108264/XhsEmoticonsKeyboard)
 * author: sj
 */
public class EmoticonsKeyboardUtils {


    /**
     * 关闭软键盘
     * 当使用全屏主题的时候,XhsEmoticonsKeyBoard屏蔽了焦点.关闭软键盘时,直接指定 closeSoftKeyboard(EditView)
     *
     * @param view
     */
    public static void closeSoftKeyboard(View view) {
        if (view == null || view.getWindowToken() == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
