package com.yonghui.address.rule.interceptor;

import com.yonghui.address.AddressContext;
import com.yonghui.address.dto.DetailAddress;
import com.yonghui.util.AddressUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @author jasonbiao
 * @date 2020-08-14 15:31
 * description: <p>
 *  1. 一级地址后缀和二级地址前缀相同，去掉二级地址前缀
 *  AAAYYYY YYYYBBB -> AAAAYYYY BBB
 *  2. 二级地址包含一级地址，将二级地址包含一级地址之前的字符去除
 *  AAAA  XXXAAAABBB -> AAAA BBB
 * </p>
 */
public class AddressUniqInterceptor implements AfterCompletionInterceptor {

    @Override
    public void afterCompletion(AddressContext context, DetailAddress detailAddress) {
        String firstAddress = detailAddress.getFirstAddress();
        String secondAddress = detailAddress.getSecondAddress();
        // 1. 一级地址后缀和二级地址前缀相同，去掉二级地址前缀
        String intersection = AddressUtil.getFirstAddrPrefixAndSecondAddrSuffixIntersection(firstAddress, secondAddress);
        if(StringUtils.isNotBlank(intersection)) {
            detailAddress.setSecondAddress(secondAddress.substring(intersection.length()));
        }


        // 2. 二级地址包含一级地址，将二级地址包含一级地址之前的字符去除
        Integer index = AddressUtil.getIndexOfFirstAddrInSecondAddr(firstAddress, secondAddress);
        if(index >= 0) {
            detailAddress.setSecondAddress(secondAddress.substring(index + firstAddress.length()));
        }
    }
}
