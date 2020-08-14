package com.yonghui.address.rule.interceptor;


import com.yonghui.address.AddressContext;

import java.util.List;

/**
 * @author jasonbiao
 * @date 2020-08-14 17:01
 * description: <p>
 *
 * </p>
 */
public class AddressUnitFilterInterceptor implements AfterExtractInterceptor {
    @Override
    public void afterExtract(AddressContext addressContext) {
        List<String> firstUnits = addressContext.getFirstUnits();
        List<String> secondUnits = addressContext.getSecondUnits();
    }
}
