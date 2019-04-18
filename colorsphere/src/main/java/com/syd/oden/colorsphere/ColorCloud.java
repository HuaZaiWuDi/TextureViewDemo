package com.syd.oden.colorsphere;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 项目名称：OdenDemo
 * 类描述：
 * 创建人：oden
 * 创建时间：2016/8/4 11:30
 */
public class ColorCloud {
    private List<ColorBean> colorBeans;
    private int radius = 3;
    private float sin_mAngleX, cos_mAngleX, sin_mAngleY, cos_mAngleY, sin_mAngleZ, cos_mAngleZ;
    private float mAngleZ = 0;
    private float mAngleX = 0;
    private float mAngleY = 0;
    private boolean distrEven = true; //default is to distribute tags evenly on the Cloud

    public ColorCloud() {
        colorBeans = new ArrayList<>();
    }

    //create method calculates the correct initial location of each bean
    public void create(boolean distrEven) {
        this.distrEven = distrEven;
        //calculate and set the location of each bean
        positionAll(distrEven);
        sineCosine(mAngleX, mAngleY, mAngleZ);
        updateAll();
    }

    private void positionAll(boolean distrEven) {
        double phi = 0;
        double theta = 0;
        int max = colorBeans.size();
        //distribute: (disrtEven is used to specify whether distribute random or even
        for (int i = 1; i < max + 1; i++) {
            if (distrEven) {
                phi = Math.acos(-1.0 + (2.0 * i - 1.0) / max);
                theta = Math.sqrt(max * Math.PI) * phi;
            } else {
                phi = Math.random() * (Math.PI);
                theta = Math.random() * (2 * Math.PI);
            }

            //coordinate conversion:
            colorBeans.get(i - 1).setLocX((int) ((radius * Math.cos(theta) * Math.sin(phi))
            ));
            colorBeans.get(i - 1).setLocY((int) (radius * Math.sin(theta) * Math.sin(phi)));
            colorBeans.get(i - 1).setLocZ((int) (radius * Math.cos(phi)));
        }
    }

    private void sineCosine(float mAngleX, float mAngleY, float mAngleZ) {
        double degToRad = (Math.PI / 180);
        sin_mAngleX = (float) Math.sin(mAngleX * degToRad);
        cos_mAngleX = (float) Math.cos(mAngleX * degToRad);
        sin_mAngleY = (float) Math.sin(mAngleY * degToRad);
        cos_mAngleY = (float) Math.cos(mAngleY * degToRad);
        sin_mAngleZ = (float) Math.sin(mAngleZ * degToRad);
        cos_mAngleZ = (float) Math.cos(mAngleZ * degToRad);
    }

    private void updateAll() {
        //update transparency/scale for all beans:
        int max = colorBeans.size();
        for (int j = 0; j < max; j++) {
            //There exists two options for this part:
            // multiply positions by a x-rotation matrix
            float rx1 = (colorBeans.get(j).getLocX());
            float ry1 = (colorBeans.get(j).getLocY()) * cos_mAngleX +
                    colorBeans.get(j).getLocZ() * -sin_mAngleX;
            float rz1 = (colorBeans.get(j).getLocY()) * sin_mAngleX +
                    colorBeans.get(j).getLocZ() * cos_mAngleX;
            // multiply new positions by a y-rotation matrix
            float rx2 = rx1 * cos_mAngleY + rz1 * sin_mAngleY;
            float ry2 = ry1;
            float rz2 = rx1 * -sin_mAngleY + rz1 * cos_mAngleY;
            // multiply new positions by a z-rotation matrix
            float rx3 = rx2 * cos_mAngleZ + ry2 * -sin_mAngleZ;
            float ry3 = rx2 * sin_mAngleZ + ry2 * cos_mAngleZ;
            float rz3 = rz2;
            // set arrays to new positions
            colorBeans.get(j).setLocX(rx3);
            colorBeans.get(j).setLocY(ry3);
            colorBeans.get(j).setLocZ(rz3);

            // add perspective
            int diameter = 2 * radius;
            float per = diameter / 1.0f / (diameter + rz3);
            // let's set position, scale, alpha for the bean;
            colorBeans.get(j).setLoc2DX((int) (rx3 * per));
            colorBeans.get(j).setLoc2DY((int) (ry3 * per));
            colorBeans.get(j).setScale(per);
            colorBeans.get(j).setAlpha(per/2);
//            Log.d("syd", "per/2: " + per/2);
        }
        sortTagByScale();
    }

    //if a single bean needed to be added
    public void add(ColorBean colorBean) {
        position(distrEven, colorBean);
        //now add the new tag to the tagCloud
        colorBeans.add(colorBean);
        updateAll();
    }

    //updates the transparency/scale of all elements
    public void update() {
        // if mAngleX and mAngleY under threshold, skip motion calculations for performance
        if (Math.abs(mAngleX) > .1 || Math.abs(mAngleY) > .1) {
            sineCosine(mAngleX, mAngleY, mAngleZ);
            updateAll();
        }
    }

    private void position(boolean distrEven, ColorBean colorBean) {
        double phi = 0;
        double theta = 0;
        int max = colorBeans.size();
        //when adding a new tag, just place it at some random location
        //this is in fact why adding too many elements make TagCloud ugly
        //after many add, do one reset to rearrange all tags
        phi = Math.random() * (Math.PI);
        theta = Math.random() * (2 * Math.PI);
        //coordinate conversion:
        colorBean.setLocX((int) (radius * Math.cos(theta) * Math.sin(phi)));
        colorBean.setLocY((int) (radius * Math.sin(theta) * Math.sin(phi)));
        colorBean.setLocZ((int) (radius * Math.cos(phi)));
    }

    public void clear() {
        colorBeans.clear();
    }

    public List<ColorBean> getColorBeanList() {
        return colorBeans;
    }

    public ColorBean getColorBean(int position) {
        return colorBeans.get(position);
    }


    public void reset() {
        create(distrEven);
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setAngleX(float mAngleX) {
        this.mAngleX = mAngleX;
    }

    public void setAngleY(float mAngleY) {
        this.mAngleY = mAngleY;
    }

    public void sortTagByScale() {
        Collections.sort(colorBeans, new ColorBeanComparator());
    }

    private static class ColorBeanComparator implements Comparator<ColorBean> {

        @Override
        public int compare(ColorBean o1, ColorBean o2) {
            return o1.getScale() > o2.getScale() ? 1 : -1;
        }
    }
}
