package com.dds.chatinput;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.dds.chatinput.menu.Menu;
import com.dds.chatinput.menu.MenuEventListener;
import com.dds.chatinput.menu.MenuManager;
import com.dds.chatinput.menu.utils.EmoticonsKeyboardUtils;
import com.dds.chatinput.menu.utils.SimpleCommonUtils;
import com.dds.chatinput.sp.SpUtils;

/**
 * 聊天底部菜单和输入框
 * Created by dds on 2019/3/4.
 * android_shuai@163.com
 */
public class ChatInputView extends LinearLayout implements ViewTreeObserver.OnPreDrawListener {

    private static final String TAG = SimpleCommonUtils.formatTag(ChatInputView.class.getSimpleName());

    private LinearLayout mChatInputContainer; // 输入框
    private LinearLayout mMenuItem;           // 菜单
    private FrameLayout mMenuContainer;       // 菜单界面
    private EditText mChatInput;

    private MenuManager mMenuManager;
    private int mScreenHeight;


    public ChatInputView(Context context) {
        super(context);
        init(context);
    }

    public ChatInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public ChatInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void init(Context context) {
        inflate(context, R.layout.ci_view_chatinput, this);
        mChatInputContainer = findViewById(R.id.ci_input_container);
        mMenuItem = findViewById(R.id.ci_input_menu);
        mMenuContainer = findViewById(R.id.ci_menu_container);
        mChatInput = findViewById(R.id.aurora_et_chat_input);

        // 设置菜单界面初始显示高度
        ViewGroup.LayoutParams params = mMenuContainer.getLayoutParams();
        params.height = SpUtils.getSoftHeight(context);
        mMenuContainer.setLayoutParams(params);
        mMenuContainer.setVisibility(View.GONE);


        mMenuManager = new MenuManager(this);

        mMenuManager.setMenu(Menu.newBuilder().
                customize(true).
                setRight(Menu.TAG_SEND).
                setBottom(Menu.TAG_VOICE, Menu.TAG_GALLERY, Menu.TAG_CAMERA, Menu.TAG_EMOJI).
                build());

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenHeight = dm.heightPixels;
        Log.d(TAG, "mScreenHeight:" + mScreenHeight);

        // 添加软键盘弹出的监听
        getViewTreeObserver().addOnPreDrawListener(this);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        init(context);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        this.getRootView().getGlobalVisibleRect(mRect);
        if (hasWindowFocus && mHeight <= 0) {
            this.getRootView().getGlobalVisibleRect(mRect);
            mHeight = mRect.bottom;
            Log.d(TAG, "Window focus changed, height: " + mHeight);
        }
    }

    @Override
    public boolean onPreDraw() {
        if (mPendingShowMenu) {
            if (isKeyboardVisible()) {
                ViewGroup.LayoutParams params = mMenuContainer.getLayoutParams();
                int distance = getDistanceFromInputToBottom();
                Log.d(TAG, "Distance from bottom: " + distance);
                if (distance < mHeight / 2 && distance > 300 && distance != params.height) {
                    params.height = distance;
                    mMenuContainer.setLayoutParams(params);
                    SpUtils.setSoftHeight(getContext(), distance);
                }
                return false;
            } else {
                showMenuLayout();
                mPendingShowMenu = false;
                return false;
            }
        } else {
            if (mMenuContainer.getVisibility() == VISIBLE && isKeyboardVisible()) {
                dismissMenuLayout();
                return false;
            }
        }
        return true;
    }
    //==============================================================================================

    private int mHeight;
    private Rect mRect = new Rect();
    private boolean showBottomMenu = true;

    public boolean isKeyboardVisible() {
        return (getDistanceFromInputToBottom() > 300 && mMenuContainer.getVisibility() == GONE)
                || (getDistanceFromInputToBottom() > (mMenuContainer.getHeight() + 300)
                && mMenuContainer.getVisibility() == VISIBLE);
    }

    public int getDistanceFromInputToBottom() {
        if (isShowBottomMenu()) {
            mMenuItem.getGlobalVisibleRect(mRect);
        } else {
            mChatInputContainer.getGlobalVisibleRect(mRect);
        }
        int result = mHeight - (mRect.bottom);
        Log.d(TAG, "屏幕底部-菜单项底部=" + result);
        return result;
    }


    public void setShowBottomMenu(Boolean showBottomMenu) {
        this.showBottomMenu = showBottomMenu;
        mMenuItem.setVisibility(showBottomMenu ? View.VISIBLE : View.GONE);
    }

    public boolean isShowBottomMenu() {
        return showBottomMenu;
    }

    // 是否显示菜单内容
    private boolean mPendingShowMenu;

    public void setPendingShowMenu(boolean flag) {
        this.mPendingShowMenu = flag;
    }


    public void setCustomMenuClickListener(MenuEventListener listener) {
        mMenuManager.setCustomMenuClickListener(listener);
    }


    //==============================================================================================
    public void dismissMenuLayout() {
        mMenuManager.hideCustomMenu();
        mMenuContainer.setVisibility(GONE);
    }

    public void showMenuLayout() {
        EmoticonsKeyboardUtils.closeSoftKeyboard(mChatInput);
        mMenuContainer.setVisibility(VISIBLE);
    }

    public void hideDefaultMenuLayout() {
        int count = mMenuContainer.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = mMenuContainer.getChildAt(i);
            view.setVisibility(View.GONE);
        }
        invalidate();
    }

    //==============================================================================================
    public LinearLayout getChatInputContainer() {
        return this.mChatInputContainer;
    }

    public LinearLayout getMenuItem() {
        return this.mMenuItem;
    }

    public FrameLayout getMenuContainer() {
        return this.mMenuContainer;
    }

    public EditText getInputView() {
        return mChatInput;
    }


}
