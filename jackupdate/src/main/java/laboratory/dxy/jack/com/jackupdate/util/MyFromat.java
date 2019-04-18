package laboratory.dxy.jack.com.jackupdate.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 项目名称：TextureViewDome
 * 类描述：
 * 创建人：oden
 * 创建时间：2017/7/18
 */
public class MyFromat {

    public MyFromat() {
        throw new RuntimeException("cannot be instantiated");
    }

    public static int DATE = 0;
    public static int NUMBER = 1;

    public static String setFormat(long value, String format, int Type) {
        if (Type == DATE) {
            return new SimpleDateFormat(format, Locale.getDefault()).format(value);
        } else if (Type == NUMBER) {
            return new DecimalFormat(format).format(value);
        }
        return null;
    }
}
