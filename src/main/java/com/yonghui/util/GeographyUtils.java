package com.yonghui.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GeographyUtils {
    /**
     * 计算百度坐标的直线距离，以米为单位
     *
     * @param lngA
     * @param latA
     * @param lngB
     * @param latB
     * @return
     */
    public static double getDistance(Double lngA, Double latA, Double lngB, Double latB) {
        double pk = 180 / 3.14169;
        double a1 = latA / pk;
        double a2 = lngA / pk;
        double b1 = latB / pk;
        double b2 = lngB / pk;
        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);
        return 6371000 * tt;
    }

}