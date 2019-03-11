package com.dds.chatinput.menu.collection;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.dds.chatinput.menu.view.MenuFeature;


public class MenuFeatureCollection extends MenuCollection {


    public MenuFeatureCollection(Context context) {
        super(context);
    }

    public void addMenuFeature(String tag, int resource) {
        if (resource == -1) {
            Log.i(TAG, "Menu feature with tag" + tag + " will not be added.");
            return;
        }
        View view = mInflater.inflate(resource, null);
        addMenuFeature(tag, view);
    }

    public void addMenuFeature(String tag, View menuFeature) {
        if (menuFeature == null) {
            Log.i(TAG, "Menu feature with tag" + tag + " will not be added.");
            return;
        }

        if (menuFeature instanceof MenuFeature) {
            menuFeature.setVisibility(View.GONE);
            if (this.containsKey(tag)) {
                Log.e(TAG, "Collection custom menu failed,Tag " + tag + " has been used already！");
                return;
            }
            addMenu(tag, menuFeature);
        } else {
            Log.e(TAG, "Collection menu feature failed exception!");
        }
    }

}
