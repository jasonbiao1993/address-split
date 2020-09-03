package com.yonghui.address.rule.impl;

import com.yonghui.address.AddressContext;
import com.yonghui.address.dto.DetailAddress;
import com.yonghui.address.rule.Rule;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jasonbiao
 * @date 2020-09-01 17:37
 * description: <p>
 * 二级地址只包含字母、数字和中划线
 * </p>
 */
@Component
public class SecondAddrOnlyContainAlphaAndNum implements Rule {
    private static final String regex = "^([a-zA-Z0-9]+)((-|一|—|–)+[a-zA-Z0-9]+){2,}$";

    private static final Pattern pattern = Pattern.compile(regex);

    @Override
    public Boolean condition(AddressContext addressContext) {
        String secondAddress = addressContext.getSecondAddress();
        return secondAddress.matches(regex);
    }

    @Override
    public DetailAddress process(AddressContext addressContext) {
        DetailAddress detailAddress = new DetailAddress();
        detailAddress.setFirstAddress(addressContext.getFirstAddress());
        Matcher matcher = pattern.matcher(addressContext.getSecondAddress());
        if (matcher.find()) {
            detailAddress.setSecondAddress(matcher.group(1));
        }
        return detailAddress;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 15;
    }
}
