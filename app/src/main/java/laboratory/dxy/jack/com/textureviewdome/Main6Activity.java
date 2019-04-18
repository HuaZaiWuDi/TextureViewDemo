package laboratory.dxy.jack.com.textureviewdome;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import laboratory.dxy.jack.com.jackupdate.util.ScreenUtil;

public class Main6Activity extends AppCompatActivity implements WaveSwipeRefreshLayout.OnRefreshListener {
    String[] data = new String[30];
    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);


        ListView mListView = (ListView) findViewById(R.id.mListView);
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.mWaveSwipeRefreshLayout);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
//        mWaveSwipeRefreshLayout.setColorSchemeResources(new int[]{Color.argb(100,255,0,0),Color.YELLOW});
        for (int i = 0; i < 30; i++) {
            data[i] = "华仔在此" + i;
        }

        mListView.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, data));
//        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d("","刷新");
//                        mWaveSwipeRefreshLayout.setRefreshing(false);
//                    }
//                }, 2000);
//            }
//        });
    }

    @Override
    public void onRefresh() {

        Log.d("", "开始刷新");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("", "刷新");
                mWaveSwipeRefreshLayout.setRefreshing(false);

            }
        }, 2000);
    }


    public void refreshing(View v) {
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        Bitmap bitmap = ScreenUtil.snapShotWithoutStatusBar(this);
        final ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setLayoutParams(mParams);
        imageView.setImageBitmap(bitmap);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_img);
        animation.setFillAfter(true);

        imageView.setAnimation(animation);

        this.addContentView(imageView, mParams);

//        startActivity(new Intent(this, Main7Activity.class));
//        mWaveSwipeRefreshLayout.setRefreshing(true);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mWaveSwipeRefreshLayout.setRefreshing(false);
//            }
//        }, 2000);
    }

}
