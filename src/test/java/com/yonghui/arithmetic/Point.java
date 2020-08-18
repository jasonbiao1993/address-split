package com.yonghui.arithmetic;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.math3.ml.clustering.Clusterable;

import java.beans.Transient;

/**
 * @author jasonbiao
 * @date 2020-08-17 15:52
 * description: <p>
 *
 * </p>
 */
@Data
@AllArgsConstructor
public class Point implements Clusterable {
    Double lng;
    Double lat;

    @JSONField(serialize = false)
    @Override
    public double[] getPoint() {
        return new double[] {lng, lat};
    }
}
