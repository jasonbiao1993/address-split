package com.yonghui.address.rule.impl;

import com.yonghui.address.AddressContext;
import com.yonghui.address.dto.DetailAddress;
import com.yonghui.address.enums.AddressUnit;
import com.yonghui.address.rule.Rule;
import com.yonghui.jieba.SegToken;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author jasonbiao
 * @date 2020-08-06 13:52
 * description: <p>
 * 二级地址包含以下，取一级地址+二级地址最长的A：
 * A1: 弄， 巷
 * A2: 号
 * A3: 号院
 * （A1>A2>A3)
 * </p>
 */
@Component
public class SecondContainPrefixUnit implements Rule {

    @Override
    public Boolean condition(AddressContext addressContext) {
        List<SegToken> secondUnits = addressContext.getSecondUnits();
        List<String> prefixUnits = AddressUnit.getPrefixUnit();

        Boolean secondPrefixExist = false;
        for (SegToken secondToken : secondUnits) {
            Optional<String> unit = prefixUnits.stream().filter(prefixUnit -> secondToken.getWord().endsWith(prefixUnit)).findAny();
            if(unit.isPresent() && secondToken.getWord().length() > 1) {
                secondPrefixExist = true;
            }
        }

        return secondPrefixExist;
    }

    @Override
    public DetailAddress process(AddressContext addressContext) {
        DetailAddress detailAddress = new DetailAddress();
        detailAddress.setFirstAddress(addressContext.getFirstAddress());

        List<SegToken> secondUnits = addressContext.getSecondUnits();
        String minUnit = secondUnits.get(secondUnits.size() - 1).getWord();
        String secondAddress = addressContext.getSecondAddress();
        detailAddress.setSecondAddress(secondAddress.substring(0, secondAddress.indexOf(minUnit)) + minUnit);
        return detailAddress;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 30;
    }
}
