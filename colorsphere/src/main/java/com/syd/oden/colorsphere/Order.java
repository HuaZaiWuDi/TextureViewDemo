package com.syd.oden.colorsphere;

import java.util.Comparator;

/**
 * Created by Oden on 2016/8/3.
 */
public class Order implements Comparator<ViewContainer> {

    @Override
    public int compare(ViewContainer lhs, ViewContainer rhs) {
        // TODO Auto-generated method stub
        return (int)(rhs.getLocZ() - lhs.getLocZ());
    }

}
