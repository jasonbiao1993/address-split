package com.yonghui.arithmetic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.yonghui.util.GeographyUtils;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

/** * 基于距离的聚类 * * @author Judy.H, Nian.L * @param <T> */
public class WithinDistanceCluster<T extends Clusterable> extends Clusterer<T> {
    double theta;

    /** * * @param theta * distance in meters. */
    public WithinDistanceCluster(double theta) {
        super(new DistanceMeasure() {

            @Override
            public double compute(double[] pointA, double[] pointB) throws DimensionMismatchException {
                //经纬度坐标算法 略（此处有优化空间）
                return GeographyUtils.getDistance(pointA[0], pointA[1], pointB[0], pointB[1]);
            }
        });
        this.theta = theta;
    }

    /** * @param distanceMeasure * @param theta * distance in meters. */
    public WithinDistanceCluster(DistanceMeasure distanceMeasure, double theta) {
        super(distanceMeasure);
        this.theta = theta;
    }

    @Override
    public List<? extends Cluster<T>> cluster(Collection<T> points)
            throws MathIllegalArgumentException, ConvergenceException {
        T[] c = (T[]) points.toArray(new Clusterable[0]);

        List<CentroidCluster<T>> clusters = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            T point = c[i];
            boolean clustered = false;
            for (CentroidCluster<T> cluster : clusters) {
                if (distance(point, cluster.getCenter()) < theta) {
                    cluster.addPoint(point);
                    clustered = true;
                    break;
                }
            }
            if (!clustered) {
                CentroidCluster<Clusterable> cluster = new CentroidCluster<>(point);
                cluster.addPoint(point);
                clusters.add((CentroidCluster<T>) cluster);
            }
        }

        return clusters;
    }

}