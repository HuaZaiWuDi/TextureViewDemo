package laboratory.dxy.jack.com.textureviewdome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yasic.library.particletextview.View.ParticleTextView;

import laboratory.dxy.jack.com.jackupdate.util.T;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import rx.functions.Action1;

public class MainActivity extends SwipeBackActivity {
    ParticleTextView particleTextView;
    ZYDownloading mZYDownloading;
    private Handler handler = new Handler();
    RxBus rxBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        //滑动退出 1.extends SwipeBackActivity 2.主题添加 <item name="android:windowIsTranslucent">true</item>
//        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
//        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_RIGHT);
//
////        particleTextView = (ParticleTextView) findViewById(R.id.particleTextView);
////        RandomMovingStrategy randomMovingStrategy = new RandomMovingStrategy();
////        CornerStrategy cornerStrategy = new CornerStrategy();
////        ParticleTextViewConfig config = new ParticleTextViewConfig.Builder()
////                .setRowStep(8)
////                .setColumnStep(8)
////                .setTargetText("Jack")
////                .setReleasing(0.2)
////                .setParticleRadius(4)
////                .setMiniDistance(0.1)
////                .setTextSize(150)
////                .setMovingStrategy(cornerStrategy)
////                .instance();
////        particleTextView.setConfig(config);
////        particleTextView.startAnimation();
//
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "LoveYaLikeASister.ttf");
//        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "Reckoner.ttf");
//        RotatingTextWrapper mRotatingTextWrapper = (RotatingTextWrapper) findViewById(R.id.mRotatingTextWrapper);
//        mRotatingTextWrapper.setSize(35);
//        mRotatingTextWrapper.setTypeface(typeface);
//
//        Rotatable rotatable = new Rotatable(Color.parseColor("#FFA036"), 1000, "Word", "Word01", "Word02");
//        rotatable.setSize(35);
//        rotatable.setTypeface(typeface2);
//        rotatable.setAnimationDuration(500);
//
//        mRotatingTextWrapper.setContent("This is ?", rotatable);


//        ColorCloudView colorCloudView = (ColorCloudView) findViewById(R.id.colorCloudView);
//        ColorBean.colorS = 100;
//        ColorBean.colorV = 100;
//        ColorBeansAdapter colorBeansAdapter = new ColorBeansAdapter(this, 85, 32, R.mipmap.circle_white);
//        colorBeansAdapter.setClickListener(new ColorBeansAdapter.OnClickListener() {
//            @Override
//            public void click(ColorBean colorBean) {
////                int selectedColor = Color.HSVToColor(new float[]{colorBean.getColorH(), ColorBean.getColorS() / 100f, ColorBean.getColorV() / 100f});
////                img_color.setColorFilter(selectedColor);
////                byte red = (byte) ((selectedColor & 0xff0000) >> 16);
////                byte green = (byte) ((selectedColor & 0x00ff00) >> 8);
////                byte blue = (byte) (selectedColor & 0x0000ff);
////                mPresenter.sendRgb(red, green, blue);
//
//            }
//        });
//        colorCloudView.setAdapter(this, colorBeansAdapter);


//        CstStoreHouseAnimView mCstStoreHouseAnimView = (CstStoreHouseAnimView) findViewById(R.id.mCstStoreHouseAnimView);
//        mCstStoreHouseAnimView.startAnim();
//
//
//        LoadingPathAnimView mLoadingPathAnimView = (LoadingPathAnimView) findViewById(R.id.mLoadingPathAnimView);
//        mLoadingPathAnimView.startAnim();
//
//        //下载进度条
//        mZYDownloading = (ZYDownloading) findViewById(R.id.mZYDownloading);
//        mZYDownloading.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "正在下载", Toast.LENGTH_SHORT).show();
//
//                if (mZYDownloading.isDownloading()) {
//                    mZYDownloading.stopDownloading();
//                    handler.removeCallbacks(progresssRunnable);
//                } else {
//                    progress = 0;
//                    mZYDownloading.startDownload();
//                    handler.postDelayed(progresssRunnable, 1000);
//                }
//            }
//        });



//        final WaveView mWaveView2= (WaveView) findViewById(R.id.mWaveView2);
//        mWaveView2.startPaint(60*1000,1000);
////        mWaveView2.onEvalutor();
//        mWaveView2.setOnPainterTrickListener(new WaveView.OnPainterTrickListener() {
//            @Override
//            public int getPainterValue() {
//                return -1;
//            }
//
//            @Override
//            public void onPainterTrick(int value) {
//                L.d("wave："+value);
//            }
//
//            @Override
//            public void onPainterFinished() {
//                L.d("结束");
//                int average = mWaveView2.getAverage();
//            }
//        });

        getSupportFragmentManager().beginTransaction().replace(R.id.test, PlaceholderFragment.newInstance(1)).commit();


    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.controls, container, false);

//            RxBus.getInstance().toObserverable()
//                    .subscribe(new Action1<Object>() {
//                        @Override
//                        public void call(Object o) {
//                            testBean bean = (testBean) o;
//                            T.showCenter(getContext(), "RXBUS" + bean.name);
//                            T.showCenter(getContext(), "RXBUS" + bean.age);
//                            T.showCenter(getContext(), "RXBUS" + bean.sex);
//                        }
//                    });


            RxBus.getInstance().doSubscribe(testBean.class, new Action1<testBean>() {
                @Override
                public void call(testBean testBean) {
                    T.showCenter(getContext(), "RXBUS" + testBean.name);
                    T.showCenter(getContext(), "RXBUS" + testBean.age);
                    T.showCenter(getContext(), "RXBUS" + testBean.sex);
                }
            });


            return rootView;
        }
    }


    int progress = 0;

    public void start(View v) {
        Log.d("点击了", "start");
//        particleTextView.stopAnimation();

        //requestCode不能<0
//        startActivityForResult(new Intent(this, CaptureActivity.class), 5);


//        if (mZYDownloading.isDownloading()) {
//            mZYDownloading.stopDownloading();
//        } else {
//            mZYDownloading.startDownload();
//        }
//
//
//        handler.postDelayed(progresssRunnable, 1000);


//        startActivity(new Intent(this, Main6Activity.class));

        testBean bean = new testBean();
        bean.name = "晏万华";
        bean.age = 20;
        bean.sex = "男";


        RxBus.getInstance().post(bean);

    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unSubscribe(testBean.class);
    }

    public void end(View v) {
        startActivity(new Intent(this, Main2Activity.class));
    }


    Runnable progresssRunnable = new Runnable() {
        @Override
        public void run() {
            progress++;
            mZYDownloading.setProgress(progress);
            handler.postDelayed(progresssRunnable, 200);
            if (progress == 100) {
                handler.removeCallbacks(progresssRunnable);
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 5) {
            String result = data.getExtras().getString("result");
            Log.d("", "onActivityResult result: " + result);
        }
        if (data != null) {
            Toast.makeText(this, data.getExtras().getString("result"), Toast.LENGTH_SHORT).show();
        }
    }


}
