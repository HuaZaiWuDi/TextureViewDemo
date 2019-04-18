package laboratory.dxy.jack.com.jackupdate.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 项目名称：TextureViewDome
 * 类描述：
 * 创建人：Jack
 * 创建时间：2017/7/25
 */
public class ShareUtlis {


    public ShareUtlis() {
         /* cannot be instantiated */
        throw new RuntimeException("cannot be instantiated");
    }



    /**
     * 简单的分享
     * **/
    public static void smpleShare(Context context, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "分享"));
    }



    /**
     * 简单的评分分享
     * **/
    public static void smpleEvaluate(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent2, "评价"));
    }


}
