package com.dds.messagelist;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;

import com.dds.messagelist.model.IMessage;

/**
 * Created by dds on 2019/2/15.
 * android_shuai@163.com
 */
public class MessageList extends RecyclerView {
    private MsgListAdapter mAdapter;
    private Context mContext;

    public MessageList(Context context) {
        this(context, null);
    }

    public MessageList(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageList(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseStyle(context, attrs);
    }

    private void parseStyle(Context context, AttributeSet attrs) {
        mContext = context;
    }

    public <MESSAGE extends IMessage> void setAdapter(MsgListAdapter<MESSAGE> adapter) {
        mAdapter = adapter;
        SimpleItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        itemAnimator.setAddDuration(0);
        itemAnimator.setChangeDuration(0);
        itemAnimator.setMoveDuration(0);
        itemAnimator.setRemoveDuration(0);
        setItemAnimator(itemAnimator);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        setLayoutManager(layoutManager);
        adapter.setLayoutManager(layoutManager);
        super.setAdapter(adapter);

    }
}
