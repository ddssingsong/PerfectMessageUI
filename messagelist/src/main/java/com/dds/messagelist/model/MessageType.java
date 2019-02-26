package com.dds.messagelist.model;

/**
 * 消息类型
 * Created by dds on 2019/2/15.
 * android_shuai@163.com
 */
public enum MessageType {
    EVENT(0),

    SEND_TEXT(1),
    RECEIVE_TEXT(2),

    SEND_IMAGE(3),
    RECEIVE_IMAGE(4),

    SEND_VOICE(5),
    RECEIVE_VOICE(6),

    SEND_VIDEO(7),
    RECEIVE_VIDEO(8),

    SEND_LOCATION(9),
    RECEIVE_LOCATION(10),

    SEND_FILE(11),
    RECEIVE_FILE(12);


    public final int value;

    MessageType(int value) {
        this.value = value;
    }




}
