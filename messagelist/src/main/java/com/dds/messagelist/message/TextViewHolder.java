package com.dds.messagelist.message;

import android.view.View;
import android.widget.TextView;

import com.dds.messagelist.R;
import com.dds.messagelist.model.IMessage;
import com.dds.messagelist.model.IUser;
import com.dds.messagelist.widget.RoundImageView;

/**
 * Created by dds on 2019/2/18.
 * android_shuai@163.com
 */
public class TextViewHolder<MESSAGE extends IMessage> extends BaseMessageViewHolder<MESSAGE> {
    private boolean mIsSender;

    private TextView mTimeTv;
    private RoundImageView mAvatarTv;
    private TextView mNameTv;
    private TextView mMsgTv;


    public TextViewHolder(View itemView, boolean isSender) {
        super(itemView, isSender);
        mIsSender = isSender;
        mTimeTv = itemView.findViewById(R.id.message_tv_msg_item_date);
        mAvatarTv = itemView.findViewById(R.id.message_iv_msg_item_avatar);
        mNameTv = itemView.findViewById(R.id.message_tv_msg_item_display_name);
        mMsgTv = itemView.findViewById(R.id.message_tv_msg_item_message);

    }

    @Override
    public void onBind(MESSAGE message) {
        IUser fromUser = message.getFromUser();
        if (fromUser != null) {
            mNameTv.setText(fromUser.getDisplayName());
        }

        //  mTimeTv.setText(message.getTimeString());
        mMsgTv.setText(message.getText());
    }
}
