package com.syd.oden.colorsphere;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 项目名称：OdenDemo
 * 类描述：
 * 创建人：oden
 * 创建时间：2016/8/4 14:20
 */
public abstract class ColorsAdapter {
    public abstract int getCount();
    public abstract View getView(Context context, int position, ViewGroup parent);
    public abstract int getColorH(int position);
    public abstract void onColorChanged(View view,int Color);
    public abstract void viewBind(View view, ColorBean colorBean);
}
