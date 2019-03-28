package com.dds.chatinput;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
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
import com.dds.chatinput.menu.utils.CommonUtils;
import com.dds.chatinput.sp.SpUtils;

/**
 * 聊天界面底部 菜单和输入框
 * Created by dds on 2019/3/4.
 * android_shuai@163.com
 */
public class ChatInputView extends LinearLayout implements ViewTreeObserver.OnPreDrawListener,
        View.OnClickListener, TextWatcher, ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = CommonUtils.formatTag(ChatInputView.class.getSimpleName());

    private LinearLayout mChatInputContainer; // 输入框
    private LinearLayout mMenuItem;           // 菜单Item
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
        mContext = context;
        inflate(context, R.layout.ci_view_chatinput, this);
        mChatInputContainer = findViewById(R.id.ci_input_container);
        mMenuItem = findViewById(R.id.ci_input_menu);
        mMenuContainer = findViewById(R.id.ci_menu_container);
        mChatInput = findViewById(R.id.aurora_et_chat_input);

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
            ViewGroup.LayoutParams params = mMenuContainer.getLayoutParams();
            softHeight = SpUtils.getSoftHeight(getContext());
            params.height = softHeight;
        }
        // 添加软键盘弹出的监听
        getRootView().getViewTreeObserver().addOnGlobalLayoutListener(this);
        getViewTreeObserver().addOnPreDrawListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        getRootView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
        getViewTreeObserver().removeOnPreDrawListener(this);
        super.onDetachedFromWindow();

    }

    @Override
    public boolean onPreDraw() {
        if (mPendingShowMenu) {
            if (isKeyboardVisible()) {
                int distance = softHeight;
                ViewGroup.LayoutParams params = mMenuContainer.getLayoutParams();
                Log.d(TAG, "设置菜单高度:" + distance);
                mMenuContainer.setLayoutParams(params);
                params.height = distance;
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


    private int rootViewVisibleHeight;  // 记录根视图的高度
    private int softHeight;             // 记录软键盘高度


    @Override
    public void onGlobalLayout() {
        Rect r = new Rect();
        View view = getRootView();
        view.getWindowVisibleDisplayFrame(r);
        int visibleHeight = r.height();

        if (rootViewVisibleHeight == 0) {
            rootViewVisibleHeight = visibleHeight;
            return;
        }
        if (rootViewVisibleHeight == visibleHeight) {
            return;
        }
        int diff = visibleHeight - rootViewVisibleHeight;
        Log.d(TAG, "diff:" + diff);
        // 软键盘弹出
        if (diff < -(mHeight / 5) && Math.abs(diff) < mHeight / 2) {
            if (softHeight != diff) {
                softHeight = -diff;
                Log.d(TAG, "save soft height:" + -diff);
                SpUtils.setSoftHeight(mContext, -diff);

            }
            if (mListener != null) {
                mListener.toggleSoftVisible();
            }
        }

        //软键盘隐藏
        if (diff > mHeight / 5) {
            if (mListener != null) {
                mListener.toggleSoftInVisible();
            }

        }
        rootViewVisibleHeight = visibleHeight;

    }

    @Override
    public void onClick(View v) {
        if (v == mSendBtn) {
            //发送消息
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


    //发送按钮显示动画
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


    //发送文字消息
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
