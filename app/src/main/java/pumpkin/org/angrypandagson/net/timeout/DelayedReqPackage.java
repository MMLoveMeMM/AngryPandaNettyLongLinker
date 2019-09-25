package pumpkin.org.angrypandagson.net.timeout;

import android.support.annotation.NonNull;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: bugfix_speech_v0.9.5
 * @ClassName: DelayedReqPackage
 * @Author: 刘志保
 * @CreateDate: 2019/9/9 17:10
 * @Description: 超时机制
 */
public class DelayedReqPackage implements Delayed {

    private int req_id;
    private long insertTime;//插入时间
    private int delayTime;//超时时间

    public DelayedReqPackage(int req_id, long insertTime, int delayTime) {
        this.req_id = req_id;
        this.insertTime = insertTime;
        this.delayTime = delayTime;
    }

    public int getReqId() {
        return req_id;
    }

    @Override
    public long getDelay(@NonNull TimeUnit unit) {
        return this.insertTime + delayTime - System.currentTimeMillis();
    }

    @Override
    public int compareTo(@NonNull Delayed o) {
        //比较 1是放入队尾  -1是放入队头
        DelayedReqPackage that = (DelayedReqPackage) o;
        if (this.insertTime + this.delayTime > that.insertTime + that.delayTime) {
            return 1;
        } else if (this.insertTime + this.delayTime == that.insertTime + that.delayTime) {
            return 0;
        } else {
            return -1;
        }
    }
}
