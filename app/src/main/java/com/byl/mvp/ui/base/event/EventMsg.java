package com.byl.mvp.ui.base.event;

public class EventMsg {
    public MsgCode code;
    public Object obj;

    public EventMsg(MsgCode code) {
        this.code = code;
    }

    public EventMsg(MsgCode code, Object obj) {
        this.code = code;
        this.obj = obj;
    }

}
