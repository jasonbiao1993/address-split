package com.yonghui.util;

import org.springframework.util.StringUtils;

/**
 * @author jasonbiao
 * @date 2020-08-14 14:19
 * description: <p>
 *
 * </p>
 */
public class AddressUtil {


    /**
     * 获取一级地址结尾字符串和二级地址开头字符串的交集
     * @param firstAddress
     * @param secondAddress
     * @return
     */
    public static String getIntersection(String firstAddress, String secondAddress) {
        if(StringUtils.isEmpty(firstAddress) || StringUtils.isEmpty(secondAddress)) {
            return null;
        }

        for (int i = secondAddress.length(); i > 0 ; i--) {
            String intersection = secondAddress.substring(0, i);
            if(firstAddress.endsWith(intersection)) {
                return intersection;
            }
        }
        return null;
    }
}
