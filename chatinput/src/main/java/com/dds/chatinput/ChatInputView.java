package com.dds.chatinput;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.dds.chatinput.menu.Menu;
import com.dds.chatinput.menu.MenuEventListener;
import com.dds.chatinput.menu.MenuManager;
import com.dds.chatinput.menu.utils.EmoticonsKeyboardUtils;
import com.dds.chatinput.menu.utils.SimpleCommonUtils;
import com.dds.chatinput.menu.view.MenuFeature;
import com.dds.chatinput.menu.view.MenuItem;

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
    private InputMethodManager mImm;
    private Window mWindow;


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
        mMenuManager = new MenuManager(this);

        mMenuManager.setMenu(Menu.newBuilder().
                customize(true).
                setRight(Menu.TAG_SEND).
                setBottom(Menu.TAG_VOICE, Menu.TAG_GALLERY, Menu.TAG_CAMERA, Menu.TAG_EMOJI).
                build());

        mMenuManager.setCustomMenuClickListener(new MenuEventListener() {
            @Override
            public boolean onMenuItemClick(String tag, MenuItem menuItem) {

                return true;
            }

            @Override
            public void onMenuFeatureVisibilityChanged(int visibility, String tag, MenuFeature menuFeature) {

            }
        });


        mImm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = ((Activity) context).getWindow();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;

        // 添加软键盘弹出的监听
        getViewTreeObserver().addOnPreDrawListener(this);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        init(context);
    }
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
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

    private int mWidth;
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
        return mHeight - mRect.bottom;
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
