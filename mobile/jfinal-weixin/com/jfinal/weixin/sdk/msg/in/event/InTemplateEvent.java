package com.jfinal.weixin.sdk.msg.in.event;

import com.jfinal.weixin.sdk.msg.in.InMsg;

public class InTemplateEvent extends InMsg {
 
    // 模板消息：TEMPLATESENDJOBFINISH
    private String event;
    private String msgId;
    private String status;
 
    public InTemplateEvent(String toUserName, String fromUserName, Integer createTime, String msgType) {
        super(toUserName, fromUserName, createTime, msgType);
    }
     
    public String getEvent() {
        return event;
    }
     
    public void setEvent(String event) {
        this.event = event;
    }
 
    public String getMsgId()
    {
        return msgId;
    }
 
    public void setMsgId(String msgId)
    {
        this.msgId = msgId;
    }
 
    public String getStatus()
    {
        return status;
    }
 
    public void setStatus(String status)
    {
        this.status = status;
    }
}
