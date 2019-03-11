package com.dds.chatinput.menu.collection;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.dds.chatinput.menu.utils.SimpleCommonUtils;

import java.util.HashMap;


public class MenuCollection extends HashMap<String, View> {

    public static final String TAG = SimpleCommonUtils.formatTag(MenuCollection.class.getSimpleName());
    protected Context mContext;
    protected LayoutInflater mInflater;


    public MenuCollection(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }


    protected void addMenu(String menuTag, View menu) {
        if (TextUtils.isEmpty(menuTag)) {
            Log.e(TAG, "Collection custom menu failed,tag is empty.");
            return;
        }

        menu.setTag(menuTag);
        if (listener != null) {
            listener.addMenu(menuTag, menu);
        }

        this.put(menuTag, menu);

    }

    private MenuCollectionChangedListener listener;

    public void setMenuCollectionChangedListener(MenuCollectionChangedListener listener) {
        this.listener = listener;
    }

    public interface MenuCollectionChangedListener {
        void addMenu(String menuTag, View menu);
    }


}
