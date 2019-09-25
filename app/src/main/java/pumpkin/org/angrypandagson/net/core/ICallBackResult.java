package pumpkin.org.angrypandagson.net.core;

/**
 * @ProjectName: bugfix_speech_v0.9.5
 * @ClassName: ISendResult
 * @Author: 刘志保
 * @CreateDate: 2019/9/9 15:50
 * @Description: 长连接发送数据包成功与否的回调接口
 */
public interface ICallBackResult {
    void callBackResult(int state);
}
