package com.dds.chatinput.menu.collection;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.dds.chatinput.menu.utils.ViewUtil;
import com.dds.chatinput.menu.view.MenuItem;

/**
 * 菜单项
 * Created by dds on 2019/3/5.
 * android_shuai@163.com
 */
public class MenuItemCollection extends MenuCollection {

    public MenuItemCollection(Context context) {
        super(context);
    }

    public void addCustomMenuItem(String tag, int resource) {
        View view = mInflater.inflate(resource, null);
        addCustomMenuItem(tag, view);
    }


    public void addCustomMenuItem(String tag, View menu) {
        if (menu instanceof MenuItem) {
            menu.setClickable(true);
            menu = ViewUtil.formatViewWeight(menu, 1);
            if (containsKey(tag)) {
                Log.e(TAG, "Collection custom menu failed,Tag " + tag + " has been used already！");
                return;
            }
            addMenu(tag, menu);
        } else {
            Log.e(TAG, "Collection menu item failed !");
        }
    }

}
