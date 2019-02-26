package com.dds.messagelist.message;

import android.view.View;

import com.dds.messagelist.R;
import com.dds.messagelist.model.IMessage;
import com.dds.messagelist.widget.RoundTextView;

/**
 * Created by dds on 2019/2/18.
 * android_shuai@163.com
 */
public class EventViewHolder<MESSAGE extends IMessage> extends BaseMessageViewHolder<MESSAGE> {

    private RoundTextView textView;

    public EventViewHolder(View itemView, boolean isSender) {
        super(itemView, isSender);
        textView = itemView.findViewById(R.id.message_tv_msg_item_event);
    }

    @Override
    public void onBind(MESSAGE message) {
        textView.setText(message.getText());
    }
}
