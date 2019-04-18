package com.syd.oden.colorsphere;

import android.graphics.Color;
import android.view.View;

/**
 * 项目名称：OdenDemo
 * 类描述：
 * 创建人：oden
 * 创建时间：2016/8/4 11:03
 */
public class ColorBean {
    public static int colorS = 100;
    public static int colorV = 100;
    public  int colorH = 100;
    private View mView;

    private float scale = 1.0f;
    private float alpha = 1.0f;

    //the center of the 3D
    private float locX = 0;
    private float locY = 0;
    private float locZ = 0;

    private float loc2DX = 0;
    private float loc2DY = 0;


    public ColorBean(int colorH) {
        this.colorH = colorH;
    }

    public float getLocX() {
        return locX;
    }

    public void setLocX(float locX) {
        this.locX = locX;
    }

    public float getLocY() {
        return locY;
    }

    public void setLocY(float locY) {
        this.locY = locY;
    }

    public float getLocZ() {
        return locZ;
    }

    public void setLocZ(float locZ) {
        this.locZ = locZ;
    }

    public float getLoc2DX() {
        return loc2DX;
    }

    public void setLoc2DX(float loc2DX) {
        this.loc2DX = loc2DX;
    }

    public float getLoc2DY() {
        return loc2DY;
    }

    public void setLoc2DY(float loc2DY) {
        this.loc2DY = loc2DY;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getAlpha() {
        return alpha;
    }

    public int getColorH() {
        return colorH;
    }

    public static int getColorV() {
        return colorV;
    }

    public static int getColorS() {
        return colorS;
    }

    public int getColor() {
        return Color.HSVToColor((int) (alpha* 0xff), new float[]{colorH, colorS / 100f, colorV / 100f});
    }

    public View getView() {
        return mView;
    }

    public void setView(View mView) {
        this.mView = mView;
    }
}

