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
 * @date 2020-08-05 16:51
 * description: <p>
 *  一级地址包含以下单位， 一级地址最小单位<二级地址最小单位, 取一级地址，否则，取一级地址+二级地址最长的A：
 *  A0：区、期, 不作为排序
 *  A1: 弄、巷
 *  A2: 号
 *  A3: 号院
 * （A1>A2>A3)
 * </p>
 */
@Component
public class FirstContainPrefixUnit implements Rule {
    @Override
    public Boolean condition(AddressContext addressContext) {
        List<SegToken> firstUnits = addressContext.getFirstFilterUnits();
        List<String> prefixUnits = AddressUnit.getPrefixUnit();

        Boolean firstPrefixExist = false;
        for (SegToken firstUnit : firstUnits) {
            Optional<String> unit = prefixUnits.stream().filter(prefixUnit -> firstUnit.getWord().endsWith(prefixUnit)).findAny();
            if(unit.isPresent() && firstUnit.getWord().length() > 1) {
                firstPrefixExist = true;
            }
        }

        return firstPrefixExist;
    }

    @Override
    public DetailAddress process(AddressContext addressContext) {
        DetailAddress detailAddress = new DetailAddress();

        List<SegToken> firstUnits = addressContext.getFirstFilterUnits();
        List<SegToken> secondUnits = addressContext.getSecondFilterUnits();

        List<AddressUnit> prefixUnitEnum = AddressUnit.getPrefixUnitEnum();

        Integer firstMinLevel = Integer.MAX_VALUE;
        for (SegToken firstToken : firstUnits) {
            Optional<AddressUnit> addressUnit = prefixUnitEnum.stream().filter(prefixUnit -> firstToken.getWord().endsWith(prefixUnit.getUnit())).findAny();
            if(addressUnit.isPresent() && firstToken.getWord().length() > 1) {
                firstMinLevel = Math.min(firstMinLevel, addressUnit.get().getLevel());
            }
        }


        Integer secondMinLevel = Integer.MAX_VALUE;
        for (SegToken secondToken : secondUnits) {
            Optional<AddressUnit> addressUnit = prefixUnitEnum.stream().filter(prefixUnit -> secondToken.getWord().endsWith(prefixUnit.getUnit())).findAny();
            if(addressUnit.isPresent() && secondToken.getWord().length() > 1) {
                secondMinLevel = Math.min(firstMinLevel, addressUnit.get().getLevel());
            }
        }


        if(firstMinLevel < secondMinLevel) {
            detailAddress.setFirstAddress(addressContext.getFirstAddress());
            return detailAddress;
        }
        detailAddress.setFirstAddress(addressContext.getFirstAddress());

        String minUnit = secondUnits.get(secondUnits.size() - 1).getWord();
        String secondAddress = addressContext.getSecondAddress();
        detailAddress.setSecondAddress(secondAddress.substring(0, secondAddress.indexOf(minUnit)) + minUnit);
        return detailAddress;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 20;
    }
}
