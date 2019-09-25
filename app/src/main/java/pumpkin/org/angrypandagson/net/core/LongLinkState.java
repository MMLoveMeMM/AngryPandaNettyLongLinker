package pumpkin.org.angrypandagson.net.core;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static pumpkin.org.angrypandagson.net.core.LongLinkState.SEND_FAIL;
import static pumpkin.org.angrypandagson.net.core.LongLinkState.SEND_OK;

/**
 * @ProjectName: bugfix_speech_v0.9.5
 * @ClassName: LongLinkState
 * @Author: 刘志保
 * @CreateDate: 2019/9/10 14:19
 * @Description: java类作用描述
 */
@IntDef({SEND_OK,SEND_FAIL})
@Retention(RetentionPolicy.SOURCE)
public @interface LongLinkState {
    int SEND_OK = 0;
    int SEND_FAIL = -1;
}

