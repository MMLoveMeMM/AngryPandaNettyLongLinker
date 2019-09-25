package pumpkin.org.angrypandagson.net.timeout;

import pumpkin.org.angrypandagson.net.base.IDataReceiver;

/**
 * @ProjectName: bugfix_speech_v0.9.5
 * @ClassName: SendPack
 * @Author: 刘志保
 * @CreateDate: 2019/9/9 20:12
 * @Description: 发送包基础类
 */
public class SendPack {

    private int reqId;
    private String data;
    // 发送该包到收到返回的ack时间间隔限制
    private int timeout;
    // 发送该包的时间戳
    private long timestamp;
    private IDataReceiver receiver;

    public SendPack(int reqId, String data, int timeout) {
        this.reqId = reqId;
        this.data = data;
        this.timeout = timeout;
    }

    public SendPack(int reqId, String data, int timeout, long timestamp,IDataReceiver receiver) {
        this.reqId = reqId;
        this.data = data;
        this.timeout = timeout;
        this.receiver = receiver;
        this.timestamp = timestamp;
    }

    public int getReqId() {
        return reqId;
    }

    public void setReqId(int reqId) {
        this.reqId = reqId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public IDataReceiver getReceiver() {
        return receiver;
    }

    public void setReceiver(IDataReceiver receiver) {
        this.receiver = receiver;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
