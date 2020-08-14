package com.yonghui.address;

import com.yonghui.jieba.SegToken;
import lombok.Data;

import java.util.List;

/**
 * @author jasonbiao
 * @date 2020-08-03 18:16
 * description: <p>
 *
 * </p>
 */
@Data
public class AddressContext {
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;

    /**
     * 区/县
     */
    private String district;

    /**
     * 一级详细地址
     */
    private String firstAddress;

    /**
     * 二级详细地址
     */
    private String secondAddress;

    /**
     * 一级地址拆分列表，没有经过过滤
     */
    private List<SegToken> firstSegToken;

    /**
     * 二级地址拆分列表，没有经过过滤
     */
    private List<SegToken> secondSegToken;

    /**
     * 提取的一级地址单位集合
     */
    private List<SegToken> firstUnits;

    /**
     * 提取的二级地址单位集合
     */
    private List<SegToken> secondUnits;
}
