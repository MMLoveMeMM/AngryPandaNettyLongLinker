package pumpkin.org.angrypandagson.net.base;

/**
 * @ProjectName: bugfix_speech_v0.9.5
 * @ClassName: IDataReceiver
 * @Author: 刘志保
 * @CreateDate: 2019/9/11 9:31
 * @Description: 数据回调
 */
public interface IDataReceiver {
    void onReceiver(String datas);
    // void onReceiver(String datas,int timeout);
}
