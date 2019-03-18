package com.dds.messagelist;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dds.messagelist.holder.BaseMessageViewHolder;
import com.dds.messagelist.holder.EventViewHolder;
import com.dds.messagelist.holder.TextViewHolder;
import com.dds.messagelist.holder.ViewHolder;
import com.dds.messagelist.model.IMessage;
import com.dds.messagelist.model.MessageType;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dds on 2019/2/15.
 * android_shuai@163.com
 */
public class MsgListAdapter<MESSAGE extends IMessage> extends RecyclerView.Adapter<ViewHolder> {
    // Notice message
    private final int TYPE_EVENT = 0;

    // Text message
    private final int TYPE_SEND_TXT = 1;
    private final int TYPE_RECEIVE_TXT = 2;

    // Photo message
    private final int TYPE_SEND_IMAGE = 3;
    private final int TYPE_RECEIVER_IMAGE = 4;

    // Voice message
    private final int TYPE_SEND_VOICE = 5;
    private final int TYPE_RECEIVER_VOICE = 6;

    // Video message
    private final int TYPE_SEND_VIDEO = 7;
    private final int TYPE_RECEIVE_VIDEO = 8;
    // Location message
    private final int TYPE_SEND_LOCATION = 9;
    private final int TYPE_RECEIVER_LOCATION = 10;


    private OnMsgClickListener<MESSAGE> mMsgClickListener;
    private OnMsgLongClickListener<MESSAGE> mMsgLongClickListener;
    private OnAvatarClickListener<MESSAGE> mAvatarClickListener;

    private List<Wrapper> mItems;
    private Context mContext;

    private RecyclerView mRecyclerView;

    public MsgListAdapter(Context context, RecyclerView recyclerView) {
        this.mContext = context;
        this.mItems = new ArrayList<>();
        mRecyclerView = recyclerView;
    }

    // 消息最新的在下方，新增一条消息
    public void addToEnd(MESSAGE message, boolean scrollToBottom) {
        final int oldSize = mItems.size();
        Wrapper<MESSAGE> element = new Wrapper<>(message);
        mItems.add(oldSize, element);
        notifyItemRangeInserted(oldSize, 1);
        if (mLayoutManager != null && scrollToBottom) {
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.requestLayout();
                    mLayoutManager.smoothScrollToPosition(mRecyclerView, null, oldSize + 1);
                }
            }, 100);

        }
    }

    // 第一次进入插入消息，或者在结尾插入多条消息
    public void addToStartChronologically(List<MESSAGE> messages) {
        int oldSize = mItems.size();
        int size = messages.size();
        for (int i = 0; i < size; i++) {
            MESSAGE message = messages.get(i);
            mItems.add(new Wrapper<>(message));
        }
        notifyItemRangeInserted(oldSize, mItems.size() - oldSize);
    }

    public boolean remove(int position) {
        if (position >= mItems.size() || position < 0) {
            return false;
        }
        notifyItemRemoved(position);
        mItems.remove(position);
        notifyItemRangeChanged(position, mItems.size() - position - 1);
        return true;
    }

    @Override
    public int getItemViewType(int position) {
        Wrapper wrapper = mItems.get(position);
        if (wrapper.item instanceof IMessage) {
            IMessage message = (IMessage) wrapper.item;
            if (message.getType() == MessageType.EVENT.value) {
                return TYPE_EVENT;
            } else if (message.getType() == MessageType.SEND_TEXT.value) {
                return TYPE_SEND_TXT;
            } else if (message.getType() == MessageType.RECEIVE_TEXT.value) {
                return TYPE_RECEIVE_TXT;
            } else if (message.getType() == MessageType.SEND_IMAGE.value) {
                return TYPE_SEND_IMAGE;
            } else if (message.getType() == MessageType.RECEIVE_IMAGE.value) {
                return TYPE_RECEIVER_IMAGE;
            } else if (message.getType() == MessageType.SEND_VOICE.value) {
                return TYPE_SEND_VOICE;
            } else if (message.getType() == MessageType.RECEIVE_VOICE.value) {
                return TYPE_RECEIVER_VOICE;
            } else if (message.getType() == MessageType.SEND_VIDEO.value) {
                return TYPE_SEND_VIDEO;
            } else if (message.getType() == MessageType.RECEIVE_VIDEO.value) {
                return TYPE_RECEIVE_VIDEO;
            } else if (message.getType() == MessageType.SEND_LOCATION.value) {
                return TYPE_SEND_LOCATION;
            } else if (message.getType() == MessageType.RECEIVE_LOCATION.value) {
                return TYPE_RECEIVER_LOCATION;
            } else {
                // 暂不支持
                return 1111;
            }
        }
        return MessageType.SEND_TEXT.value;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            // 通知消息
            case TYPE_EVENT:
                // getHolder(parent, R.layout.message_item_event, null, true);
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_event, parent, false);
                return new EventViewHolder(v, true);
            case TYPE_SEND_TXT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_text_send, parent, false);
                return new TextViewHolder(v, true);
            case TYPE_RECEIVE_TXT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_text_receive, parent, false);
                return new TextViewHolder(v, false);
            case TYPE_SEND_IMAGE:
                break;
            case TYPE_RECEIVER_IMAGE:
                break;
            case TYPE_SEND_VOICE:
                break;
            case TYPE_RECEIVER_VOICE:
                break;
            case TYPE_SEND_VIDEO:
                break;
            case TYPE_RECEIVE_VIDEO:
                break;
            case TYPE_SEND_LOCATION:
                break;
            case TYPE_RECEIVER_LOCATION:
                break;

        }
        return null;
    }

    private <HOLDER extends ViewHolder> ViewHolder getHolder(ViewGroup parent, @LayoutRes int layout,
                                                             Class<HOLDER> holderClass, boolean isSender) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        try {
            Constructor<HOLDER> constructor = holderClass.getDeclaredConstructor(View.class, boolean.class);
            constructor.setAccessible(true);
            return constructor.newInstance(v, isSender);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Wrapper wrapper = mItems.get(holder.getAdapterPosition());
        if (wrapper.item instanceof IMessage) {
            ((BaseMessageViewHolder) holder).mPosition = holder.getAdapterPosition();
            ((BaseMessageViewHolder) holder).mContext = this.mContext;
            ((BaseMessageViewHolder) holder).mIsSelected = wrapper.isSelected;
            ((BaseMessageViewHolder) holder).mMsgLongClickListener = this.mMsgLongClickListener;
            ((BaseMessageViewHolder) holder).mMsgClickListener = this.mMsgClickListener;
            ((BaseMessageViewHolder) holder).mAvatarClickListener = this.mAvatarClickListener;
            ((BaseMessageViewHolder) holder).mData = this.mItems;
        }
        holder.onBind(wrapper.item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public class Wrapper<DATA> {
        private DATA item;
        boolean isSelected;

        Wrapper(DATA item) {
            this.item = item;
        }

        public DATA getItem() {
            return this.item;
        }

        public boolean getIsSelected() {
            return this.isSelected;
        }

        public void setItem(DATA item) {
            this.item = item;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }
    }


    //=========================================================================================
    private LinearLayoutManager mLayoutManager;

    public void setLayoutManager(LinearLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    //==========================================================================================
    // 消息点击
    public void setOnMsgClickListener(OnMsgClickListener<MESSAGE> listener) {
        mMsgClickListener = listener;
    }

    // 消息长按
    public void setMsgLongClickListener(OnMsgLongClickListener<MESSAGE> listener) {
        mMsgLongClickListener = listener;
    }

    // 点击头像
    public void setOnAvatarClickListener(OnAvatarClickListener<MESSAGE> listener) {
        mAvatarClickListener = listener;
    }


    public interface OnMsgClickListener<MESSAGE extends IMessage> {
        void onMessageClick(MESSAGE message);
    }

    public interface OnMsgLongClickListener<MESSAGE extends IMessage> {
        void onMessageLongClick(View view, MESSAGE message);
    }

    public interface OnAvatarClickListener<MESSAGE extends IMessage> {
        void onAvatarClick(MESSAGE message);
    }
}
