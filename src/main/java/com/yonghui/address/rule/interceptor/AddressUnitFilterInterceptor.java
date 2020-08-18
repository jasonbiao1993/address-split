package com.yonghui.address.rule.interceptor;


import com.yonghui.address.AddressContext;
import com.yonghui.jieba.SegToken;

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
        List<SegToken> firstUnits = addressContext.getFirstUnits();
        List<SegToken> secondUnits = addressContext.getSecondUnits();
    }
}
