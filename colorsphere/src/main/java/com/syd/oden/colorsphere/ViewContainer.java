package com.syd.oden.colorsphere;

import android.view.View;

/**
 * Created by Oden on 2016/8/3.
 */
public class ViewContainer {
    private int viewId;
    private View view;
    private float locZ;

    public ViewContainer(View view, int viewId) {
        this.viewId = viewId;
        this.view = view;
    }

    public int getViewId() {
        return viewId;
    }

    public View getView() {
        return view;
    }

    public float getLocZ() {
        return locZ;
    }

    public void setLocZ(float locZ) {
        this.locZ = locZ;
    }

    @Override
    public String toString() {
        return "[viewId:" + viewId + ",locz:" + locZ +  "]";
    }


}
