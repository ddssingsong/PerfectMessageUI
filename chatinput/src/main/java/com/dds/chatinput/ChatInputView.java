package com.dds.chatinput;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.dds.chatinput.listener.OnMenuClickListener;
import com.dds.chatinput.menu.Menu;
import com.dds.chatinput.menu.MenuEventListener;
import com.dds.chatinput.menu.MenuManager;
import com.dds.chatinput.menu.utils.EmoticonsKeyboardUtils;
import com.dds.chatinput.menu.utils.SimpleCommonUtils;
import com.dds.chatinput.sp.SpUtils;
import com.dds.chatinput.utils.OsUtil;

/**
 * 聊天底部菜单和输入框
 * Created by dds on 2019/3/4.
 * android_shuai@163.com
 */
public class ChatInputView extends LinearLayout implements ViewTreeObserver.OnPreDrawListener, View.OnClickListener, TextWatcher, View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = SimpleCommonUtils.formatTag(ChatInputView.class.getSimpleName());

    private LinearLayout mChatInputContainer; // 输入框
    private LinearLayout mMenuItem;           // 菜单
    private FrameLayout mMenuContainer;       // 菜单界面
    private EditText mChatInput;
    private CharSequence mInput;

    private MenuManager mMenuManager;


    private ImageButton mVoiceBtn;
    private ImageButton mPhotoBtn;
    private ImageButton mCameraBtn;
    private ImageButton mEmojiBtn;
    private ImageButton mSendBtn;

    private OnMenuClickListener mListener;
    private Context mContext;
    boolean isBarShow;

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

    private int mScreenHeight;
    private int mScreenHeight2;

    private void init(Context context) {
        mContext = context;
        inflate(context, R.layout.ci_view_chatinput, this);
        mChatInputContainer = findViewById(R.id.ci_input_container);
        mMenuItem = findViewById(R.id.ci_input_menu);
        mMenuContainer = findViewById(R.id.ci_menu_container);
        mChatInput = findViewById(R.id.aurora_et_chat_input);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenHeight = dm.heightPixels;
        Log.d(TAG, "mScreenHeight1:" + mScreenHeight);
//        DisplayMetrics dm1 = new DisplayMetrics();
//        ((Activity) context).getWindowManager().getDefaultDisplay().getRealMetrics(dm1);
//        mScreenHeight2 = dm1.heightPixels;
//        Log.d(TAG, "mScreenHeight2:" + mScreenHeight2);

        mMenuContainer.setVisibility(View.GONE);


        mMenuManager = new MenuManager(this);

        mMenuManager.setMenu(Menu.newBuilder().
                customize(true).
                setRight(Menu.TAG_SEND).
                setBottom(Menu.TAG_VOICE, Menu.TAG_GALLERY, Menu.TAG_CAMERA, Menu.TAG_EMOJI).
                build());


        mChatInput.addTextChangedListener(this);


        mSendBtn = findViewById(R.id.ci_menu_item_ib_send);
        mSendBtn.setOnClickListener(this);
        mSendBtn.setClickable(false);
        mSendBtn.setEnabled(false);

        mChatInput.setOnTouchListener(this);


    }

    private void initAttrs(Context context, AttributeSet attrs) {
        init(context);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        Log.d(TAG, "Window focus changed, hasWindowFocus: " + hasWindowFocus);
        super.onWindowFocusChanged(hasWindowFocus);
        this.getRootView().getGlobalVisibleRect(mRect);
        if (hasWindowFocus && mHeight <= 0) {
            this.getRootView().getGlobalVisibleRect(mRect);
            mHeight = mRect.bottom;
            Log.d(TAG, "Window focus changed, height: " + mHeight);
            ViewGroup.LayoutParams params = mMenuContainer.getLayoutParams();
            params.height = SpUtils.getSoftHeight(getContext());
        }
        // 添加软键盘弹出的监听
        getViewTreeObserver().addOnPreDrawListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnPreDrawListener(this);
    }

    @Override
    public boolean onPreDraw() {
        if (mPendingShowMenu) {
            if (isKeyboardVisible()) {
                ViewGroup.LayoutParams params = mMenuContainer.getLayoutParams();
                int distance = getDistanceFromInputToBottom();
                if (OsUtil.isMiui()) {
                    Log.e(TAG, "isBarShow:" + isBarShow);
                    isBarShow = Settings.Global.getInt(mContext.getContentResolver(), "force_fsg_nav_bar", 0) == 0;
                    if (isBarShow) {
                        // 显示虚拟键
                        distance = distance + (mScreenHeight - mHeight);
                    } else {
                        // 全面屏不显示虚拟键
                    }
                } else {
                    distance = distance + (mScreenHeight - mHeight);
                }

                Log.d(TAG, "Distance from bottom1: " + distance + ",params.height:" + params.height);
                if (distance < mHeight / 2 && distance > 300 && distance != params.height) {
                    mMenuContainer.setLayoutParams(params);
                    params.height = distance;

                    Log.d(TAG, "save softKeyboard height : " + params.height);
                    SpUtils.setSoftHeight(getContext(), params.height);
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

    @Override
    public void onGlobalLayout() {
        if (isKeyboardVisible()) {
            ViewGroup.LayoutParams params = mMenuContainer.getLayoutParams();
            int distance = getDistanceFromInputToBottom();
            Log.d(TAG, "Distance from bottom1: " + distance + ",params.height:" + params.height);
            if (distance < mHeight / 2 && distance > 300 && distance != params.height) {
                params.height = distance + (mScreenHeight - mHeight);
                mMenuContainer.setLayoutParams(params);
                Log.d(TAG, "save softKeyboard height : " + distance);
                SpUtils.setSoftHeight(getContext(), distance);
            }
        } else {
            if (mPendingShowMenu) {
                showMenuLayout();
            }

        }

    }

    @Override
    public void onClick(View v) {
        if (v == mSendBtn) {
            if (onSubmit()) {
                mChatInput.setText("");
            }

        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mInput = s;
        if (s.length() >= 1 && start == 0 && before == 0) { // Starting input
            triggerSendButtonAnimation(mSendBtn, true);
        } else if (s.length() == 0 && before >= 1) { // Clear content
            triggerSendButtonAnimation(mSendBtn, false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mListener != null) {
            mListener.editViewOnTouch();

        }
        return false;
    }

    private void triggerSendButtonAnimation(final ImageButton sendBtn, final boolean hasContent) {
        mSendBtn.setEnabled(hasContent);
        mSendBtn.setClickable(hasContent);
        float[] shrinkValues = new float[]{0.6f};
        AnimatorSet shrinkAnimatorSet = new AnimatorSet();
        shrinkAnimatorSet.playTogether(ObjectAnimator.ofFloat(sendBtn, "scaleX", shrinkValues),
                ObjectAnimator.ofFloat(sendBtn, "scaleY", shrinkValues));
        shrinkAnimatorSet.setDuration(100);

        float[] restoreValues = new float[]{1.0f};
        final AnimatorSet restoreAnimatorSet = new AnimatorSet();
        restoreAnimatorSet.playTogether(ObjectAnimator.ofFloat(sendBtn, "scaleX", restoreValues),
                ObjectAnimator.ofFloat(sendBtn, "scaleY", restoreValues));
        restoreAnimatorSet.setDuration(100);

        restoreAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    requestLayout();
                    invalidate();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        shrinkAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (hasContent) {
                    mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ci_menu_item_send_pres));
                } else {
                    mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ci_menu_item_send));
                }
                restoreAnimatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        shrinkAnimatorSet.start();
    }

    private boolean onSubmit() {
        return mListener != null && mListener.onSendTextMessage(mInput);
    }


    //==============================================================================================

    private int mHeight;
    private Rect mRect = new Rect();
    private boolean showBottomMenu = true;

    public boolean isKeyboardVisible() {
        ViewGroup view = (ViewGroup) mMenuItem.getParent();
        view.getGlobalVisibleRect(mRect);
        int result = mHeight - (mRect.bottom);
        return result > 250;

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
    private boolean mPendingShowMenu = false;

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

    public boolean isMenuFeatureVisible() {
        return mMenuManager.isMenuFeatureVisible();
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


    //=============================================================================================
    public void setMenuClickListener(OnMenuClickListener listener) {
        mListener = listener;
    }


}
