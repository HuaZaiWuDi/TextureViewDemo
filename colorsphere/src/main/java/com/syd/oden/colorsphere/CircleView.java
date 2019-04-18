package com.syd.oden.colorsphere;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.view.ViewGroup;

/**
 * 项目名称：OdenDemo
 * 类描述：
 * 创建人：oden
 * 创建时间：2016/8/4 11:01
 */
public class CircleView extends View {
    private int mCenterX;
    private int mCenterY;
    private int mRadius;
    private Paint mPaint;
    private int mColor = 0xFF378FC9;
    private int mTextColor = 0xFF378FC9;
    private String text = "";
    private int mWidth;
    private int mHeight;

    public CircleView(Context context) {
        super(context);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 取长和宽中的小值
        mWidth = mWidth < mHeight ? mWidth : mHeight;
        mRadius = mCenterX = mCenterY = mWidth / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);

        //画圆
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
        mPaint.setColor(mTextColor);
        canvas.drawText(text, mCenterX, mCenterY, mPaint);
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
//        invalidate();
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

}
