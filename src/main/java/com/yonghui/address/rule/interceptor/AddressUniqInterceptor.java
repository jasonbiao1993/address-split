package com.yonghui.address.rule.interceptor;

import com.yonghui.address.AddressContext;
import com.yonghui.address.dto.DetailAddress;
import com.yonghui.util.AddressUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @author jasonbiao
 * @date 2020-08-14 15:31
 * description: <p>
 *  一级地址后缀和二级地址前缀相同，去掉二级地址前缀
 * </p>
 */
public class AddressUniqInterceptor implements AfterCompletionInterceptor {

    @Override
    public void afterCompletion(AddressContext context, DetailAddress detailAddress) {
        String firstAddress = detailAddress.getFirstAddress();
        String secondAddress = detailAddress.getSecondAddress();
        String intersection = AddressUtil.getIntersection(firstAddress, secondAddress);
        if(StringUtils.isNotBlank(intersection)) {
            detailAddress.setSecondAddress(secondAddress.substring(intersection.length()));
        }
    }
}
