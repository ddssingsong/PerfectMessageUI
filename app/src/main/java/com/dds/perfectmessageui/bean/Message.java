package com.dds.perfectmessageui.bean;

import com.dds.messagelist.model.IMessage;
import com.dds.messagelist.model.IUser;
import com.dds.messagelist.model.MessageStatus;

import java.util.UUID;

/**
 * Created by dds on 2019/3/1.
 * android_shuai@163.com
 */
public class Message implements IMessage {
    private long msgId;
    private IUser user;
    private String timeString;
    private String text;
    private String mediaFilePath;
    private long duration;
    private String progress;
    private int type;
    private MessageStatus messageStatus = MessageStatus.CREATED;


    public Message() {

    }

    public Message(int type, String text) {
        this.msgId = UUID.randomUUID().getLeastSignificantBits();
        this.text = text;
        this.type = type;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public IUser getUser() {
        return user;
    }

    public void setUser(IUser user) {
        this.user = user;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setMediaFilePath(String mediaFilePath) {
        this.mediaFilePath = mediaFilePath;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }


    @Override
    public String getMsgId() {
        return String.valueOf(msgId);
    }

    @Override
    public IUser getFromUser() {
        return user;
    }

    public static int count = 0;

    @Override
    public String getTimeString() {
        //todo 测试
        if (count > 5) {
            count = 0;
            return "3月2日 晚上12:32";
        }
        count++;
        return timeString;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getMediaFilePath() {
        return mediaFilePath;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public String getProgress() {
        return progress;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public MessageStatus getMessageStatus() {
        return messageStatus;
    }
}
