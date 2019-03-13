package com.dds.chatinput.listener;

import com.dds.chatinput.model.FileItem;

import java.util.List;

/**
 * Created by dds on 2019/3/13.
 * android_shuai@163.com
 */
public interface OnMenuClickListener {

    // 发送文字消息
    boolean onSendTextMessage(CharSequence input);

    // 发送文件
    void onSendFiles(List<FileItem> list);


    boolean switchToAudioMode();


    boolean switchToGalleryMode();


    boolean switchToCameraMode();

    boolean switchToEmojiMode();
}
