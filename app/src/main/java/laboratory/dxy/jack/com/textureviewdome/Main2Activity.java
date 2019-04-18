package laboratory.dxy.jack.com.textureviewdome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import ezy.boost.update.ICheckAgent;
import ezy.boost.update.IDownloadAgent;
import ezy.boost.update.IUpdateChecker;
import ezy.boost.update.IUpdateDownloader;
import ezy.boost.update.IUpdateParser;
import ezy.boost.update.UpdateInfo;
import ezy.boost.update.UpdateManager;
import ezy.boost.update.UpdateUtil;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class Main2Activity extends SwipeBackActivity implements View.OnClickListener {

//    public class UpdateInfo {
//        // 是否有新版本
//        public boolean hasUpdate = false;
//        // 是否静默下载：有新版本时不提示直接下载
//        public boolean isSilent = false;
//        // 是否强制安装：不安装无法使用app
//        public boolean isForce = false;
//        // 是否下载完成后自动安装
//        public boolean isAutoInstall = true;
//        // 是否可忽略该版本
//        public boolean isIgnorable = true;
//
//        public int versionCode;
//        public String versionName;
//        public String updateContent;
//
//        public String url;
//        public String md5;
//        public long size;
//    }


    String mCheckUrl = "http://client.waimai.baidu.com/message/updatetag";

    //    String mUpdateUrl = "http://mobile.ac.qq.com/qqcomic_android.apk";
    String mUpdateUrl = "http://d.dxycloud.com/statistics/getDownLoadAddr/passivityDownAndroidApp?file_name=WiexNwdPzVv6Hl2K1499931798.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_RIGHT);

        UpdateManager.setDebuggable(true);
        UpdateManager.setWifiOnly(false);
        UpdateManager.setUrl(mCheckUrl, "yyb");
        UpdateManager.check(this);
        check(false, true, false, false, true, 998);

        findViewById(R.id.check_update).setOnClickListener(this);
        findViewById(R.id.check_update_cant_ignore).setOnClickListener(this);
        findViewById(R.id.check_update_force).setOnClickListener(this);
        findViewById(R.id.check_update_no_newer).setOnClickListener(this);
        findViewById(R.id.check_update_silent).setOnClickListener(this);
        findViewById(R.id.clean).setOnClickListener(this);
    }


    void check(boolean isManual, final boolean hasUpdate, final boolean isForce, final boolean isSilent, final boolean isIgnorable, final int
            notifyId) {
        UpdateManager.create(this).setChecker(new IUpdateChecker() {
            @Override
            public void check(ICheckAgent agent, String url) {
                Log.d("ezy.check", "checking" + "URL:" + url);
                agent.setInfo("");
            }
        }).setWifiOnly(true).setDownloader(new IUpdateDownloader() {
            @Override
            public void download(IDownloadAgent agent, String url, File temp) {
                Log.e("ezy.download",  "URL:" + url);
                Log.e("ezy.download",  "hasUpdate:" + agent.getInfo().hasUpdate);
                Log.e("ezy.download",  "updateContent:" + agent.getInfo().updateContent);
                Log.e("ezy.download",  "url:" + agent.getInfo().url);
                Log.e("ezy.download",  "versionName:" + agent.getInfo().versionName);
                Log.e("ezy.download",  "isAutoInstall:" + agent.getInfo().isAutoInstall);
                Log.e("ezy.download",  "isForce:" + agent.getInfo().isForce);
                Log.e("ezy.download",  "isIgnorable:" + agent.getInfo().isIgnorable);
                Log.e("ezy.download",  "isIgnorable:" + agent.getInfo().versionCode);
                Log.e("ezy.download",  "isIgnorable:" + agent.getInfo().size);
                Log.e("ezy.download",  "isIgnorable:" + agent.getInfo().maxTimes);
                Log.e("ezy.download",  "getAbsolutePath:" +  temp.getAbsolutePath());
                Log.e("ezy.download",  "Path:" +  temp.getPath());

            }
        }).setUrl(mCheckUrl).setManual(isManual).setNotifyId(notifyId).setParser(new IUpdateParser() {
            @Override
            public UpdateInfo parse(String source) throws Exception {
                UpdateInfo info = new UpdateInfo();
                info.hasUpdate = hasUpdate;
                info.updateContent = "• 支持文字、贴纸、背景音乐，尽情展现欢乐气氛；\n• 两人视频通话支持实时滤镜，丰富滤镜，多彩心情；\n• 图片编辑新增艺术滤镜，一键打造文艺画风；\n• 资料卡新增点赞排行榜，看好友里谁是魅力之王。";
                info.versionCode = 587;
                info.versionName = "v5.8.7";
                info.url = mUpdateUrl;
                info.md5 = "56cf48f10e4cf6043fbf53bbbc4009e3";
                info.size = 10149314;
                info.isForce = isForce;
                info.isIgnorable = isIgnorable;
                info.isSilent = isSilent;
                return info;
            }
        }).check();
    }


    void checkUpdate() {
        UpdateManager.create(this).setUrl(mCheckUrl).setChecker(new IUpdateChecker() {
            @Override
            public void check(ICheckAgent agent, String url) {

            }
        }).check();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_update:
                check(true, true, false, false, true, 998);
                break;
            case R.id.check_update_cant_ignore:
                check(true, true, false, false, false, 998);
                break;
            case R.id.check_update_force:
                check(true, true, true, false, true, 998);
                break;
            case R.id.check_update_no_newer:
                check(true, false, false, false, true, 998);
                break;
            case R.id.check_update_silent:
                check(true, true, false, true, true, 998);
                break;
            case R.id.clean:
                UpdateUtil.clean(this);
                Toast.makeText(this, "cleared", Toast.LENGTH_LONG).show();
                break;
        }
    }


    public void click(View v){
        startActivity(new Intent(this,Main3Activity.class));
    }

}
