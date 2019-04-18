package com.syd.oden.colorsphere;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * 项目名称：OdenDemo
 * 类描述：
 * 创建人：oden
 * 创建时间：2016/8/4 14:13
 */
public class ColorCloudView extends ViewGroup implements Runnable {
    private int FLINGABLE_VALUE = 1; // 当每秒移动角度达到该值时，认为是快速移动
    private boolean isFiling = false;
    private final float TOUCH_SCALE_FACTOR = .8f;
    private final float TRACKBALL_SCALE_FACTOR = 10;
    private Context context;
    private float tspeed = 50f;
    private ColorCloud mColorCloud;
    private float mAngleX = 0.5f;
    private float mAngleY = 0.5f;
    private float centerX, centerY;
    private float radius;
    private float radiusPercent = 0.9f;

    public static final int MODE_DISABLE = 0;
    public static final int MODE_DECELERATE = 1;
    public static final int MODE_UNIFORM = 2;
    public int mode;
    private int left, right, top, bottom;

    private MarginLayoutParams layoutParams;
    private int minSize;
    private ColorsAdapter colorsAdapter;
//    List<ViewContainer> viewContainers = new ArrayList<>();
    private boolean isOnTouch = false;
    private Handler handler = new Handler(Looper.getMainLooper());
    private float centerScreenX;
    private float centerScreenY;


    private float xDownInScreen;
    private float yDownInScreen;
    private float xInScreen;
    private float yInScreen;
    boolean isMove = false;

    public ColorCloudView(Context context) {
        this(context, null);
    }

    public ColorCloudView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        setFocusableInTouchMode(true);
        mColorCloud = new ColorCloud();
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorCloudView);
            mode = Integer.valueOf(typedArray.getString(R.styleable.ColorCloudView_autoScrollMode));
            radiusPercent = typedArray.getFloat(R.styleable.ColorCloudView_radiusPercent, radiusPercent);
            tspeed = typedArray.getFloat(R.styleable.ColorCloudView_scrollSpeed, 2f);
            typedArray.recycle();
        }

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            wm.getDefaultDisplay().getSize(point);
        } else {
            point.x = wm.getDefaultDisplay().getWidth();
            point.y = wm.getDefaultDisplay().getHeight();
        }
        int screenWidth = point.x;
        int screenHeight = point.y;
        minSize = screenHeight < screenWidth ? screenHeight : screenWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int contentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int contentHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        measureChildren(widthMode, heightMode);

        if (layoutParams == null) {
            layoutParams = (MarginLayoutParams) getLayoutParams();
        }

        int dimensionX = widthMode == MeasureSpec.EXACTLY ? contentWidth : minSize - layoutParams.leftMargin - layoutParams.rightMargin;
        int dimensionY = heightMode == MeasureSpec.EXACTLY ? contentHeight : minSize - layoutParams.leftMargin - layoutParams.rightMargin;
        setMeasuredDimension(dimensionX, dimensionY);
    }

    public final void setAdapter(Context context, ColorsAdapter adapter) {
        this.context = context;
        colorsAdapter = adapter;
        initFromAdapter();
        getCenterOfScreen();
    }

    private void initFromAdapter() {
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView circleView;
                ColorBean colorBean;

                Log.d("syd", "initFromAdapter");
                centerX = (getRight() - getLeft()) / 2;
                centerY = (getBottom() - getTop()) / 2;
                radius = Math.min(centerX * radiusPercent, centerY * radiusPercent);
                mColorCloud.setRadius((int) radius);
                mColorCloud.clear();
                removeAllViews();
                for (int i = 0; i < colorsAdapter.getCount(); i++) {
                    colorBean = new ColorBean(colorsAdapter.getColorH(i));
                    circleView = (ImageView) colorsAdapter.getView(getContext(), i, ColorCloudView.this);
                    colorBean.setView(circleView);
                    ColorCloudView.this.mColorCloud.add(colorBean);
                    colorsAdapter.viewBind(circleView, colorBean);
                }

                mColorCloud.create(true);
                mColorCloud.setAngleX(mAngleX);
                mColorCloud.setAngleY(mAngleY);
                mColorCloud.update();

                resetChildren();
            }
        }, 0);
    }

    private void resetChildren() {
        removeAllViews();
        //必须保证getChildAt(i) == mColorCloud.getColorBeanList().get(i)
        for (ColorBean colorBean : mColorCloud.getColorBeanList()) {
            addView(colorBean.getView());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d("SYD", "onLayout");
        left = l;
        right = r;
        top = t;
        bottom = b;
        for (int i = 0; i < getChildCount(); i++) {
//            Log.d("SYD", "onLayout getChildCount： " + getChildCount());
            View child = getChildAt(i);
            ColorBean colorBean = mColorCloud.getColorBean(i);
            if (child != null && child.getVisibility() != GONE ) {
                int left, top;

                colorsAdapter.onColorChanged(child, colorBean.getColor());
                colorsAdapter.viewBind(child, colorBean);

                child.setScaleX(colorBean.getScale());
                child.setScaleY(colorBean.getScale());
                child.setAlpha((float) (colorBean.getAlpha() * 1.1));
                left = (int) (centerX + colorBean.getLoc2DX()) - child.getMeasuredWidth() / 2;
                top = (int) (centerY + colorBean.getLoc2DY()) - child.getMeasuredHeight() / 2;
                child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("syd", "onInterceptTouchEvent");
        handleTouchEvent(ev);
        return false;
    }

    //在小球上的触摸事件处理
//    @Override
//    public boolean onTouch(View view, MotionEvent e) {
//        Log.d("syd", "onTouch");
////        switch (e.getAction()) {
////            case MotionEvent.ACTION_DOWN:
////                touchDown(e);
////                break;
////            case MotionEvent.ACTION_MOVE:
//////                Log.d("syd", "onTouch ACTION_MOVE");
////                xInScreen = e.getRawX();
////                yInScreen = e.getRawY();
////
////                if (Math.abs(xDownInScreen - xInScreen) > 15 || Math.abs(yDownInScreen - yInScreen) > 15) {
////                    float dx = xInScreen - xDownInScreen;
////                    float dy = yInScreen - yDownInScreen;
////                    mAngleX = (dy / radius) * tspeed * TOUCH_SCALE_FACTOR;
////                    mAngleY = (-dx / radius) * tspeed * TOUCH_SCALE_FACTOR;
////                    processTouch();
////                    isMove = true;
////                }
////                //rotate elements depending on how far the selection point is from center of cloud
////                break;
////            case MotionEvent.ACTION_UP:
////            case MotionEvent.ACTION_CANCEL:
////                isOnTouch = false;
////                return isMove;
////        }
//        onTouchEvent(e);
//        return false;
//    }

    long mDownTime;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        Log.d("syd", "onTouchEvent");
        handleTouchEvent(e);
        return false;
    }

    private void handleTouchEvent(MotionEvent e) {

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(e);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(e);
                break;
            case MotionEvent.ACTION_UP:
                touchUp(e);
            case MotionEvent.ACTION_CANCEL:
                isOnTouch = false;
                break;
        }
    }

    private void touchDown(MotionEvent e) {
        xDownInScreen = e.getRawX();
        yDownInScreen = e.getRawY();
        isOnTouch = true;
        isMove = false;
        isFiling = false;
        xAngleTemp = 0;
        yAngleTemp = 0;
        mDownTime = System.currentTimeMillis();
    }

    private void touchMove( MotionEvent e) {
        xInScreen = e.getRawX();
        yInScreen = e.getRawY();

        if (Math.abs(xDownInScreen - xInScreen) < 15 && Math.abs(yDownInScreen - yInScreen) < 15) {
            return;
        }

        float dx = xInScreen - xDownInScreen;
        float dy = yInScreen - yDownInScreen;
        mAngleX = (dy / radius) * tspeed * TOUCH_SCALE_FACTOR;
        mAngleY = (-dx / radius) * tspeed * TOUCH_SCALE_FACTOR;
        isMove = true;
        processTouch();
    }

    private void touchUp(MotionEvent e) {
        float x = e.getRawX();
        float y = e.getRawY();
        float speed = (float) (Math.sqrt((xDownInScreen - x) * (xDownInScreen - x) + (yDownInScreen - y) * (yDownInScreen - y)) / (System.currentTimeMillis() - mDownTime));
        if (speed > FLINGABLE_VALUE) {
            if (mAngleX >= 0) {
                mAngleX = mAngleX + speed;
            } else {
                mAngleX = mAngleX - speed;
            }

            if (mAngleY >= 0) {
                mAngleY = mAngleY + speed;
            } else {
                mAngleY = mAngleY - speed;
            }

            isFiling = true;
        }
//        Log.d("syd", "speed: " + speed);
    }

    private void processTouch() {
        if (mColorCloud != null) {
            mColorCloud.setAngleX(mAngleX);
            mColorCloud.setAngleY(mAngleY);
            mColorCloud.update();
        }
        resetChildren();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d("syd", "onAttachedToWindow");
        handler.post(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d("syd", "onDetachedFromWindow");
        handler.removeCallbacksAndMessages(null);
    }

    @SuppressLint("WrongCall")
    private void updateChild() {
        onLayout(false, left, top, right, bottom);
    }

    float xAngleTemp = 0;
    float yAngleTemp = 0;

    @Override
    public void run() {
        if (!isOnTouch && mode != MODE_DISABLE && isFiling) {
            if (mode == MODE_DECELERATE) {
                if (mAngleX > 0.4f) {
                    mAngleX -= 0.2f;
                }
                if (mAngleY > 0.4f) {
                    mAngleY -= 0.2f;
                }
                if (mAngleX < -0.4f) {
                    mAngleX += 0.2f;
                }
                if (mAngleY < 0.4f) {
                    mAngleY += 0.2f;
                }
            }
//            Log.d("syd", "mAngleX : " + mAngleX + ",mAngleY: " + mAngleY);

            if (mAngleX == xAngleTemp && mAngleY == yAngleTemp) {
                isFiling = false;
            } else {
                processTouch();
                xAngleTemp = mAngleX;
                yAngleTemp = mAngleY;
            }
        }
        handler.postDelayed(this, 20);
    }

    public void getCenterOfScreen() {
        //获取屏幕宽高
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //获取状态栏高度
        Rect frame = new Rect();
        getWindowVisibleDisplayFrame(frame);
        int stateHeight = frame.top;

        centerScreenX = width / 2;
        centerScreenY = (height - stateHeight) / 2;
//        Log.d("SYD", "centerScreenX: " + centerScreenX + ",centerScreenY: " + centerScreenY);
    }

}
