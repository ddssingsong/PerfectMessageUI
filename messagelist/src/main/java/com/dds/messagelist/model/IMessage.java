package com.dds.messagelist.model;

/**
 * 消息实体接口类
 * Created by dds on 2019/2/15.
 * android_shuai@163.com
 */
public interface IMessage {

    String getMsgId();

    IUser getFromUser();

    String getTimeString();

    String getText();

    String getMediaFilePath();

    //语音或者视频时长
    long getDuration();

    String getProgress();

    /**
     * {@link MessageType}
     *
     * @return 消息类型
     */
    int getType();

    /**
     * {@link MessageStatus}
     *
     * @return 消息状态
     */
    MessageStatus getMessageStatus();


}
