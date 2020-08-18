package com.yonghui.arithmetic;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.yonghui.util.GeographyUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import java.util.List;


/**
 * @author jasonbiao
 * @date 2020-08-17 15:52
 * description: <p>
 *
 * </p>
 */
public class WithinDistanceClusterTest {

    public static void main(String[] args) {
        List<Point> points = Lists.newArrayList();
        points.add(new Point(116.455788,39.920767));
        points.add(new Point(116.456065,39.920965));
        points.add(new Point(116.452312,39.92304));
        points.add(new Point(116.421385,39.989539));
        points.add(new Point(116.455685,39.92069));
        points.add(new Point(116.455876,39.920845));
        points.add(new Point(116.455973,39.920902));
        points.add(new Point(116.455645,39.920657));
        points.add(new Point(116.456022,39.920934));
        points.add(new Point(116.455685,39.920691));
        points.add(new Point(116.456023,39.920671));
        points.add(new Point(116.45596,39.920864));
        points.add(new Point(116.455522,39.920856));
        points.add(new Point(116.455276,39.920407));
        points.add(new Point(116.455799,39.920867));
        points.add(new Point(116.455349,39.920425));
        points.add(new Point(116.45511,39.920377));
        points.add(new Point(116.455318,39.920442));
        points.add(new Point(116.455298,39.920474));
        points.add(new Point(116.455839,39.920636));
        points.add(new Point(116.455979,39.921168));
        points.add(new Point(116.454281,39.920006));
        points.add(new Point(116.45598,39.920612));
        points.add(new Point(116.45388,39.919584));
        points.add(new Point(116.455474,39.920737));
        points.add(new Point(116.456009,39.920641));
        points.add(new Point(116.455439,39.920574));
        points.add(new Point(116.455759,39.920841));
        points.add(new Point(116.455838,39.920644));
        points.add(new Point(116.455983,39.920847));
        points.add(new Point(116.459803,39.922041));
        points.add(new Point(116.456029,39.92088));
        points.add(new Point(116.455539,39.920603));
        points.add(new Point(116.455989,39.920851));
        points.add(new Point(116.455719,39.920789));
        points.add(new Point(116.45601,39.92082));
        points.add(new Point(116.456229,39.920564));
        points.add(new Point(116.455906,39.920771));
        points.add(new Point(116.456248,39.920868));
        points.add(new Point(116.455805,39.920544));
        points.add(new Point(116.455896,39.920758));
        points.add(new Point(116.43692,39.926767));
        points.add(new Point(116.454672,39.92024));
        points.add(new Point(116.454813,39.917848));
        points.add(new Point(116.381415,40.00875));
        points.add(new Point(116.422925,39.980757));
        points.add(new Point(116.422849,39.9808));
        points.add(new Point(116.38107,40.009217));
        points.add(new Point(116.456078,39.920747));
        points.add(new Point(116.455242,39.919515));
        points.add(new Point(116.455615,39.920533));
        points.add(new Point(116.422092,39.991104));
        points.add(new Point(116.454847,39.917724));
        points.add(new Point(116.456686,39.924316));
        points.add(new Point(116.45575,39.920642));
        points.add(new Point(116.456713,39.924413));
        points.add(new Point(116.455846,39.920828));
        points.add(new Point(116.422108,39.991098));
        points.add(new Point(116.422075,39.991139));
        points.add(new Point(118.775572,31.97337));
        points.add(new Point(118.776968,31.97392));
        points.add(new Point(118.778187,31.973121));
        points.add(new Point(118.775695,31.973254));
        points.add(new Point(118.775302,31.973807));
        points.add(new Point(118.776303,31.973692));
        points.add(new Point(118.777541,31.973439));
        points.add(new Point(118.776196,31.973489));
        points.add(new Point(116.448944,39.926799));
        points.add(new Point(116.45487,39.917804));
        points.add(new Point(116.455762,39.920645));
        points.add(new Point(116.456146,39.920441));
        points.add(new Point(116.455857,39.920043));
        points.add(new Point(116.455458,39.920826));
        points.add(new Point(116.455533,39.920791));
        points.add(new Point(116.455426,39.920896));
        points.add(new Point(116.45566,39.920811));
        points.add(new Point(116.455696,39.920621));
        points.add(new Point(116.453667,39.9259));
        points.add(new Point(116.466606,39.886322));
        points.add(new Point(116.455917,39.92062));
        WithinDistanceCluster cluster = new WithinDistanceCluster(300);
        List<Cluster<Point>> clusterPoints = cluster.cluster(points);

        KMeansPlusPlusClusterer kMeansPlusPlusClusterer = new KMeansPlusPlusClusterer(1, 1000, new DistanceMeasure() {

            @Override
            public double compute(double[] pointA, double[] pointB) throws DimensionMismatchException {
                //经纬度坐标算法 略（此处有优化空间）
                return GeographyUtils.getDistance(pointA[0], pointA[1], pointB[0], pointB[1]);
            }
        });


        DBSCANClusterer dbscanClusterer = new DBSCANClusterer(300, 5, new DistanceMeasure() {

            @Override
            public double compute(double[] pointA, double[] pointB) throws DimensionMismatchException {
                return GeographyUtils.getDistance(pointA[0], pointA[1], pointB[0], pointB[1]);
            }
        });

        clusterPoints.forEach(pointCluster -> {
            List<Point> points1 = pointCluster.getPoints();
            System.out.println(JSON.toJSONString(points1));
//
//
//
//
//            List<Cluster<Point>> cluster1 = kMeansPlusPlusClusterer.cluster(points1);
//            System.out.println(JSON.toJSONString(cluster1));
//            System.out.println();
        });


//        List<Cluster<Point>> cluster1 = kMeansPlusPlusClusterer.cluster(points);
//        System.out.println(JSON.toJSONString(cluster1));



        System.out.println();
        List<Cluster<Point>> cluster2 = dbscanClusterer.cluster(points);

        cluster2.forEach(pointCluster -> {
            List<Point> points1 = pointCluster.getPoints();
            System.out.println(JSON.toJSONString(points1));
        });
    }

}