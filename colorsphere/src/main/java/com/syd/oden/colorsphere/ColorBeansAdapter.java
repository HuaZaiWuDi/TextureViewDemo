package com.syd.oden.colorsphere;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * 项目名称：OdenDemo
 * 类描述：
 * 创建人：oden
 * 创建时间：2016/8/4 15:09
 */
public class ColorBeansAdapter extends ColorsAdapter {
    private Context context;
    private int count;
    private int size;
    private int imag;

    private OnClickListener onClickListener;

    public interface OnClickListener {
        void click(ColorBean colorBean);
    }

    public ColorBeansAdapter(Context context, int count, int size, int imga) {
        this.context = context;
        this.count = count;
        this.imag = imga;
        this.size = dpToPx(context, size);
    }

    @Override
    public View getView(Context context, final int position, ViewGroup parent) {
//        CircleView view = new CircleView(context);
        ImageView view = new ImageView(context);
        view.setImageResource(imag);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(size, size);
        view.setLayoutParams(lp);
//        view.setBackgroundColor(Color.TRANSPARENT);

        return view;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public int getColorH(int position) {
        return position * (360 / count);
    }

    @Override
    public void onColorChanged(View view, int color) {
        ((ImageView)view).setColorFilter(color);
//        ((CircleView)view).setColor(color);
    }

    @Override
    public void viewBind(final View view, final ColorBean colorBean) {
        Log.d("syd", "viewBind");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("syd", "colorBean: " + colorBean.getColorH());
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale);
                view.startAnimation(animation);
                if (onClickListener != null)
                    onClickListener.click(colorBean);
            }
        });
    }

    public void setClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public int dpToPx(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

}
