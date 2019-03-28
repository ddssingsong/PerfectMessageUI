package com.dds.chatinput.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.dds.chatinput.menu.utils.ViewUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by dds on 2019/3/13.
 * android_shuai@163.com
 */
public class SpUtils {

    public static final String SHARE_PREFERENCE_NAME = "ChatInputKeyboard";
    public static final String SHARE_PREFERENCE_SOFT_INPUT_HEIGHT = "soft_input_height";

    public static void setSoftHeight(Context context, int version) {
        SharedPreferences preferences = context.getSharedPreferences(SHARE_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, version);
        editor.apply();
    }

    public static int getSoftHeight(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARE_PREFERENCE_NAME, MODE_PRIVATE);
        return preferences.getInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, ViewUtil.dpToPx(350));

    }

}
