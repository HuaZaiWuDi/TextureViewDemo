package laboratory.dxy.jack.com.textureviewdome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.Hashtable;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import laboratory.dxy.jack.com.textureviewdome.text.Titanic;
import laboratory.dxy.jack.com.textureviewdome.text.TitanicTextView;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


public class Main5Activity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_RIGHT);
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        TitanicTextView mTitanicTextView = (TitanicTextView) findViewById(R.id.mTitanicTextView);
        Titanic titanic = new Titanic();

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Satisfy-Regular.ttf");
        mTitanicTextView.setTypeface(typeface);
        titanic.start(mTitanicTextView);


        ShimmerTextView mShimmerTextView= (ShimmerTextView) findViewById(R.id.mShimmerTextView);
        mShimmerTextView.setTypeface(typeface);
        Shimmer shimmer = new Shimmer();
        shimmer.setDirection(Shimmer.ANIMATION_DIRECTION_RTL);
        shimmer.setRepeatCount(10);
        shimmer.setDuration(1000);
        shimmer.start(mShimmerTextView);


//        CircularBarPager mCircularBarPager= (CircularBarPager) findViewById(R.id.mCircularBarPager);
        FeatureCoverFlow mFeatureCoverFlow= (FeatureCoverFlow) findViewById(R.id.mFeatureCoverFlow);

        mFeatureCoverFlow.setAdapter(new CoverFlowAdapter(this));

//        RubberView mRubberView= (RubberView) findViewById(R.id.mRubberView);
//        mRubberView.setMaskImage(R.drawable.image_1);



    }


    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    public static Typeface get(Context c, String assetPath) {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(), assetPath);
                    cache.put(assetPath, t);
                } catch (Exception e) {
                    Log.e("", "Could not get typeface '" + assetPath + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }

    public void click(View v){
        startActivity(new Intent(this,Main6Activity.class));
    }
}
