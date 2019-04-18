package laboratory.dxy.jack.com.textureviewdome.appupdate;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.vector.update_app.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static laboratory.dxy.jack.com.jackupdate.core.net.UpdateURL.UPDATE_LODER_APP_URL;

/**
 * 项目名称：TextureViewDome
 * 类描述：
 * 创建人：Jack
 * 创建时间：2017/7/18
 */
public class UpdateManager {
    private Activity contextActivity;
    private String Url;

    /**
     * 强制更新
     */
    private boolean isForce = false;

    /**
     * 头部图片
     */
    private int TopPic;

    /**
     * 主题颜色
     */
    private int ThemeColor;

    /**
     * 后台更新
     */
    private boolean isSilent = false;

    /**
     * 请求参数
     * 默认含有：appkey（需要配置）
     * versionCode 跟build.gradle同步
     */
    private Map<String, String> params;


    public UpdateManager(Activity activity, String ChakeUrl, Map<String, String> params) {
        contextActivity = activity;
        this.Url = ChakeUrl;
        this.params = params;
        UpdateApp();
    }


    private void UpdateApp() {

        new UpdateAppManager
                .Builder()
                //当前Activity
                .setActivity(contextActivity)
                //实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                //设置请求方式 默认get,
                .setPost(true)
                //更新地址
                .setUpdateUrl(Url)
                //添加自定义参数
                .setParams(params)
                //设置点击升级后，消失对话框
                .hideDialogOnDownloading(isSilent)
                //设置头部
//                .setTopPic(R.mipmap.ic_launcher)
                //设置主题色
//                .setThemeColor(0xff034ea0)
                .build()
                //检测是否有新版本
                .checkNewApp(new UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        Log.d("", "json:" + json);
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            String i = jsonObject.getString("i");
                            JSONObject jsonObject1 = new JSONObject(i);
                            String versionCode = jsonObject1.getString("versionCode");
                            String version = jsonObject1.getString("android_build");
                            String fileName = jsonObject1.getString("android_filename");
                            String newAPPInfo = jsonObject1.getString("description");
                            String newAPPENInfo = jsonObject1.getString("description_en");
                            String android_file_size = jsonObject1.getString("android_file_size");


                            String isUpdate = "";
                            if (Integer.parseInt(version) > Utils.getVersionCode(contextActivity)) {
                                isUpdate = "Yes";
                            } else {
                                isUpdate = "No";
                            }
                            updateAppBean
                                    //是否更新Yes,No
                                    .setUpdate(isUpdate)
                                    //新版本号
                                    .setNewVersion(versionCode)
                                    //下载地址
                                    .setApkFileUrl(UPDATE_LODER_APP_URL + fileName)
                                    //大小
                                    .setTargetSize(android_file_size)
                                    //更新内容
                                    .setUpdateLog(newAPPInfo)
                                    //是否强制更新
                                    .setConstraint(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return updateAppBean;
                    }

                    /**
                     * 有新版本
                     *
                     * @param updateApp        新版本信息
                     * @param updateAppManager app更新管理器
                     */
                    @Override
                    public void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        updateAppManager.showDialog();
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
                        Log.d("Jack", "onBefore");
                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
                        Log.d("Jack", "onAfter");
                    }

                    /**
                     * 没有新版本
                     */
                    @Override
                    public void noNewApp() {
                        Toast.makeText(contextActivity, "没有新版本", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
