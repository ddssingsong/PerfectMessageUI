package com.dds.messagelist.holder;

import android.content.Context;
import android.view.View;

import com.dds.messagelist.MsgListAdapter;
import com.dds.messagelist.model.IMessage;

import java.util.List;

/**
 * Created by dds on 2019/2/15.
 * android_shuai@163.com
 */
public abstract class BaseMessageViewHolder<MESSAGE extends IMessage>
        extends ViewHolder<MESSAGE> {

    public Context mContext;
    public int mPosition;
    public boolean mIsSelected;
    public List<MsgListAdapter.Wrapper> mData;
    public boolean mIsSender;
    public MsgListAdapter.OnMsgLongClickListener<MESSAGE> mMsgLongClickListener;
    public MsgListAdapter.OnMsgClickListener<MESSAGE> mMsgClickListener;
    public MsgListAdapter.OnAvatarClickListener<MESSAGE> mAvatarClickListener;

    public BaseMessageViewHolder(View itemView, boolean isSender) {
        super(itemView);
        this.mIsSender = isSender;
    }
}
