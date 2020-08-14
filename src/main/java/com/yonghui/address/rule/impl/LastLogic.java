package com.yonghui.address.rule.impl;

import com.yonghui.address.AddressContext;
import com.yonghui.address.dto.DetailAddress;
import com.yonghui.address.rule.Rule;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @author jasonbiao
 * @date 2020-08-06 14:01
 * description: <p>
 *  兜底逻辑, 没有匹配规则，直接取一级地址
 * </p>
 */
@Component
public class LastLogic implements Rule {

    @Override
    public Boolean condition(AddressContext addressContext) {
        return true;
    }

    @Override
    public DetailAddress process(AddressContext addressContext) {
        DetailAddress detailAddress = new DetailAddress();
        detailAddress.setFirstAddress(addressContext.getFirstAddress());
        return detailAddress;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
