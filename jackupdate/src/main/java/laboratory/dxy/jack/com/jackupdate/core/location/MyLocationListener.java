package laboratory.dxy.jack.com.jackupdate.core.location;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;

import java.util.List;

import laboratory.dxy.jack.com.jackupdate.util.L;

/**
 * Created by Oden on 2015/9/13.
 */
public class MyLocationListener implements BDLocationListener {

    private String mLocationDescribe = null;
    private Context mContext;
    private onGetLocationListener mOnGetLocationListener;

    public static double mLatitude = 0;
    public static double mLongitude = 0;
    public static String mCountry = "";
    public static String mCity = "";


    public MyLocationListener(Context c) {
        mContext = c;
    }

    public interface onGetLocationListener {
        void onGetLocationFinish();
    }

    public void setOnGetLocationListener(onGetLocationListener l) {
        this.mOnGetLocationListener = l;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        boolean successFlag = false;
        //Receive Location
        StringBuffer sb = new StringBuffer(256);
        sb.append("time : ");
        sb.append(location.getTime());
        sb.append("\nerror code : ");
        sb.append(location.getLocType());
        sb.append("\nlatitude : ");
        sb.append(location.getLatitude());//经度
        sb.append("\nlontitude : ");
        sb.append(location.getLongitude());//纬度
        sb.append("\nradius : ");
        sb.append(location.getRadius());
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
            sb.append("\nspeed : ");
            sb.append(location.getSpeed());// 单位：公里每小时
            sb.append("\nsatellite : ");
            sb.append(location.getSatelliteNumber());
            sb.append("\nheight : ");
            sb.append(location.getAltitude());// 单位：米
            sb.append("\ndirection : ");
            sb.append(location.getDirection());// 单位度
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
            sb.append("\ndescribe : ");
            sb.append("gps定位成功");
            successFlag = true;
        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
            //运营商信息
            sb.append("\noperationers : ");
            sb.append(location.getOperators());
            sb.append("\ndescribe : ");
            sb.append("网络定位成功");
            successFlag = true;
        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
            sb.append("\ndescribe : ");
            sb.append("离线定位成功，离线定位结果也是有效的");
            successFlag = true;
        } else if (location.getLocType() == BDLocation.TypeServerError) {
            sb.append("\ndescribe : ");
            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
            sb.append("\ndescribe : ");
            sb.append("网络不同导致定位失败，请检查网络是否通畅");
        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
            sb.append("\ndescribe : ");
            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
        }
        sb.append("\nlocationdescribe : ");
        sb.append(location.getLocationDescribe());// 位置语义化信息
        List<Poi> list = location.getPoiList();// POI数据
        if (list != null) {
            sb.append("\npoilist size = : ");
            sb.append(list.size());
            for (Poi p : list) {
                sb.append("\npoi= : ");
                sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
            }
        }

        L.d("BaiduLocatioApiDem:" + sb.toString());
        L.d("location.getCountry():" + location.getCountry());
        L.d("location.getProvince():" + location.getProvince());
        L.d("location.getCity():" + location.getCity());
        L.d("location.getDistrict():" + location.getDistrict());
        L.d("location.getAddrStr():" + location.getAddrStr());

        mOnGetLocationListener.onGetLocationFinish();
        mCountry = location.getCountry();
        mCity = location.getCity();
//        IotClass.mProvince = location.getProvince();
//        IotClass.mDistrict = location.getDistrict();
//        IotClass.mFormattedAddress = location.getAddrStr();
    }
}


