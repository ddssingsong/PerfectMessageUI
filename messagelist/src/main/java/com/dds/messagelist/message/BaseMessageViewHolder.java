package com.dds.messagelist.message;

import android.content.Context;
import android.view.View;

import com.dds.messagelist.model.IMessage;

import java.util.List;

/**
 * Created by dds on 2019/2/15.
 * android_shuai@163.com
 */
public abstract class BaseMessageViewHolder<MESSAGE extends IMessage>
        extends ViewHolder<MESSAGE> {

    protected Context mContext;
    protected int mPosition;
    protected boolean mIsSelected;
    protected List<MsgListAdapter.Wrapper> mData;
    private boolean mIsSender;
    protected MsgListAdapter.OnMsgLongClickListener<MESSAGE> mMsgLongClickListener;
    protected MsgListAdapter.OnMsgClickListener<MESSAGE> mMsgClickListener;
    protected MsgListAdapter.OnAvatarClickListener<MESSAGE> mAvatarClickListener;

    public BaseMessageViewHolder(View itemView, boolean isSender) {
        super(itemView);
        this.mIsSender = isSender;
    }
}
