package laboratory.dxy.jack.com.textureviewdome;

import android.app.Application;
import android.content.Context;

/**
 * 项目名称：Bracelet
 * 类描述：
 * 创建人：Jack
 * 创建时间：2017/5/13
 */
public class MyApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();


//        SkinCompatManager.withoutActivity(this)
//                .addInflater(new SkinMaterialViewInflater())
//                .addInflater(new SkinConstraintViewInflater())
//                .addInflater(new SkinCardViewInflater())
//                .loadSkin();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}