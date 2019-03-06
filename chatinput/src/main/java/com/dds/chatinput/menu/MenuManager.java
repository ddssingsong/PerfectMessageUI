package com.dds.chatinput.menu;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.dds.chatinput.ChatInputView;
import com.dds.chatinput.menu.collection.MenuItemCollection;
import com.dds.chatinput.menu.utils.SimpleCommonUtils;

/**
 * 菜单管理类
 * Created by dds on 2019/3/5.
 * android_shuai@163.com
 */
public class MenuManager {
    public static final String TAG = SimpleCommonUtils.formatTag(MenuManager.class.getSimpleName());

    private ChatInputView mChatInputView;
    private LinearLayout mChatInputContainer;
    private LinearLayout mMenuItem;
    private FrameLayout mMenuContainer;

    private Context mContext;


    private MenuItemCollection mMenuItemCollection;

    public MenuManager(ChatInputView chatInputView) {
        mChatInputView = chatInputView;
        mChatInputContainer = chatInputView.getChatInputContainer();
        mMenuItem = chatInputView.getMenuItem();
        mMenuContainer = chatInputView.getMenuContainer();
        mContext = chatInputView.getContext();
        initCollection();
    }

    private void initCollection() {
        mMenuItemCollection = new MenuItemCollection(mContext);
    }

}