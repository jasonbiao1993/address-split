package com.yonghui.address.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jasonbiao
 * @date 2020-08-03 14:03
 * description: <p>
 *  详细地址
 * </p>
 */
@Data
public class DetailAddress implements Serializable {
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
}
