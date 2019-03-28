package com.dds.chatinput.menu;


import com.dds.chatinput.menu.view.MenuFeature;
import com.dds.chatinput.menu.view.MenuItem;

/**
 * Custom Menu' callbacks
 */
public interface MenuEventListener {

    boolean onMenuItemClick(String tag, MenuItem menuItem);

    void onMenuFeatureVisibilityChanged(int visibility, String tag, MenuFeature menuFeature);


}