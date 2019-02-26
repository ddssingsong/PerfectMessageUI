package com.dds.messagelist.model;

/**
 * 消息状态
 * Created by dds on 2019/2/15.
 * android_shuai@163.com
 */
public enum MessageStatus {
    CREATED(0),
    SEND_ING(1),
    SEND_SUCCEED(2),
    SEND_FAILED(3),

    RECEIVE_ING(10),
    RECEIVE_SUCCEED(11),
    RECEIVE_FAILED(12);

    public final int value;

    MessageStatus(int value) {
        this.value = value;
    }

}
