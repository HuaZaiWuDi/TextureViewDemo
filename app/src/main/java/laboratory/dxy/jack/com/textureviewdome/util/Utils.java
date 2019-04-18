package laboratory.dxy.jack.com.textureviewdome.util;

import android.content.Context;
import android.content.pm.PackageManager;

import laboratory.dxy.jack.com.jackupdate.util.L;

/**
 * 项目名称：TextureViewDome
 * 类描述：
 * 创建人：oden
 * 创建时间：2017/7/18
 */
public class Utils {

    public static String postInfo = "OhuqY";



    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            L.d("appVersionInfo:" + versionName + "code:" + versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

}
