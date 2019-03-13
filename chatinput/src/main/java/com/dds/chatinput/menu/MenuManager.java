package com.dds.chatinput.menu;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.dds.chatinput.ChatInputView;
import com.dds.chatinput.R;
import com.dds.chatinput.menu.collection.MenuCollection;
import com.dds.chatinput.menu.collection.MenuFeatureCollection;
import com.dds.chatinput.menu.collection.MenuItemCollection;
import com.dds.chatinput.menu.utils.EmoticonsKeyboardUtils;
import com.dds.chatinput.menu.utils.SimpleCommonUtils;
import com.dds.chatinput.menu.view.MenuFeature;
import com.dds.chatinput.menu.view.MenuItem;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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
    private MenuFeatureCollection mMenuFeatureCollection;

    private MenuEventListener mMenuEventListener;


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
        mMenuItemCollection.setMenuCollectionChangedListener(new MenuCollection.MenuCollectionChangedListener() {
            @Override
            public void addMenu(String menuTag, View menu) {
                // 菜单点击事件
                menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mChatInputView.getInputView().clearFocus();
                        String tag = (String) v.getTag();
                        Log.d(TAG, "菜单点击:" + tag);
                        if (mMenuEventListener != null && mMenuEventListener.onMenuItemClick(tag, (MenuItem) v)) {
                            showMenuFeatureByTag(tag);
                        }


                    }
                });
            }
        });
        mMenuItemCollection.addCustomMenuItem(Menu.TAG_VOICE, R.layout.ci_menu_item_voice);
        mMenuItemCollection.addCustomMenuItem(Menu.TAG_GALLERY, R.layout.ci_menu_item_photo);
        mMenuItemCollection.addCustomMenuItem(Menu.TAG_CAMERA, R.layout.ci_menu_item_camera);
        mMenuItemCollection.addCustomMenuItem(Menu.TAG_EMOJI, R.layout.ci_menu_item_emoji);
        mMenuItemCollection.addCustomMenuItem(Menu.TAG_SEND, R.layout.ci_menu_item_send);


        mMenuFeatureCollection = new MenuFeatureCollection(mContext);
        mMenuFeatureCollection.setMenuCollectionChangedListener(new MenuCollection.MenuCollectionChangedListener() {
            @Override
            public void addMenu(String menuTag, View menu) {
                menu.setVisibility(View.GONE);
                mMenuContainer.addView(menu);
            }
        });
        mMenuFeatureCollection.addMenuFeature(Menu.TAG_VOICE, R.layout.ci_menu_item_voice_feature);
        mMenuFeatureCollection.addMenuFeature(Menu.TAG_GALLERY, R.layout.ci_menu_item_photo_feature);
        mMenuFeatureCollection.addMenuFeature(Menu.TAG_CAMERA, R.layout.ci_menu_item_camera_feature);
        mMenuFeatureCollection.addMenuFeature(Menu.TAG_EMOJI, R.layout.ci_menu_item_emoji_feature);

    }


    private void showMenuFeatureByTag(String tag) {
        View menuFeature = mMenuFeatureCollection.get(tag);
        if (menuFeature == null) {
            Log.i(TAG, "Can't find MenuFeature to show by tag:" + tag);
            return;
        }
        // 如果该菜单已经显示出来则隐藏
        if (menuFeature.getVisibility() == VISIBLE && mMenuContainer.getVisibility() == VISIBLE) {
            mChatInputView.dismissMenuLayout();
            return;
        }

        if (mChatInputView.isKeyboardVisible()) {
            // 如果软件开启状态则关闭软件盘
            mChatInputView.setPendingShowMenu(true);
            EmoticonsKeyboardUtils.closeSoftKeyboard(mChatInputView.getInputView());
        } else {
            mChatInputView.showMenuLayout();

        }

        // 隐藏其他菜单项
        mChatInputView.hideDefaultMenuLayout();
        // 隐藏上一个menu
        hideCustomMenu();
        // 显示改菜单项
        menuFeature.setVisibility(VISIBLE);

        if (mMenuEventListener != null)
            mMenuEventListener.onMenuFeatureVisibilityChanged(VISIBLE, tag, (MenuFeature) menuFeature);

        // 赋值记录菜单项
        lastMenuFeature = menuFeature;

    }

    private View lastMenuFeature;

    public void hideCustomMenu() {
        if (lastMenuFeature != null && lastMenuFeature.getVisibility() != GONE) {
            lastMenuFeature.setVisibility(View.GONE);
            if (mMenuEventListener != null)
                mMenuEventListener.onMenuFeatureVisibilityChanged(GONE, (String) lastMenuFeature.getTag(), (MenuFeature) lastMenuFeature);
        }

    }

    // ==========================================================================================
    // 设置菜单位置
    public void setMenu(Menu menu) {
        if (menu.isCustomize()) {
            mMenuItem.removeAllViews();
            addViews(mChatInputContainer, 1, menu.getLeft());
            addViews(mChatInputContainer, mChatInputContainer.getChildCount() - 1, menu.getRight());
            addBottomByTag(menu.getBottom());
        }
    }

    private void addBottomByTag(String... tags) {
        if (tags == null || tags.length == 0) {
            mChatInputView.setShowBottomMenu(false);
            return;
        }
        mChatInputView.setShowBottomMenu(true);
        addViews(mMenuItem, -1, tags);
    }

    private void addViews(LinearLayout parent, int index, String... tags) {
        if (parent == null || tags == null)
            return;
        for (String tag : tags) {
            View child = mMenuItemCollection.get(tag);
            if (child == null) {
                Log.e(TAG, "Can't find view by tag " + tag + ".");
                continue;
            }
            child.setTag(tag);
            parent.addView(child, index);
        }
    }


    // ==========================================================================================


    public void setCustomMenuClickListener(MenuEventListener listener) {
        this.mMenuEventListener = listener;
    }
}