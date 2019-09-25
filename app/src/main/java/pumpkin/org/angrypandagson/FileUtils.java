package pumpkin.org.angrypandagson;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ProjectName: AngryPandaGSon
 * @ClassName: FileUtils
 * @Author: 刘志保
 * @CreateDate: 2019/9/11 9:50
 * @Description: java类作用描述
 */
public class FileUtils {

    public static String readAssetFile(Context context,String fileName){
        InputStream is = null;
        String result="";
        try {
            is = context.getAssets().open(fileName);
            int lenght = is.available();
            byte[]  buffer = new byte[lenght];
            is.read(buffer);
            result = new String(buffer, "utf8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
