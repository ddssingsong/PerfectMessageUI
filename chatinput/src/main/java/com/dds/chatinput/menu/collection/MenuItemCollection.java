package com.dds.chatinput.menu.collection;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.dds.chatinput.R;
import com.dds.chatinput.menu.Menu;
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
        initDefaultMenu();
    }

    private void initDefaultMenu() {
        this.put(Menu.TAG_VOICE, inflaterMenu(R.layout.ci_menu_item_voice));
        this.put(Menu.TAG_GALLERY, inflaterMenu(R.layout.ci_menu_item_photo));
        this.put(Menu.TAG_CAMERA, inflaterMenu(R.layout.ci_menu_item_camera));
        this.put(Menu.TAG_EMOJI, inflaterMenu(R.layout.ci_menu_item_emoji));
        this.put(Menu.TAG_SEND, inflaterMenu(R.layout.ci_menu_item_send));
    }

    private View inflaterMenu(int resource) {
        View view = mInflater.inflate(resource, null);
        //设置weight = 1
        view = ViewUtil.formatViewWeight(view, 1);
        return view;

    }

    public void addCustomMenuItem(String tag, int resource) {
        View view = mInflater.inflate(resource, null);
        addCustomMenuItem(tag, view);
    }


    public void addCustomMenuItem(String tag, View menu) {
        if (menu instanceof MenuItem) {
            menu.setClickable(true);
            menu = ViewUtil.formatViewWeight(menu, 1);
            addMenu(tag, menu);
        } else {
            Log.e(TAG, "Collection menu item failed !");
        }
    }

}
