package com.yonghui.address.rule.impl;

import com.yonghui.address.AddressContext;
import com.yonghui.address.dto.DetailAddress;
import com.yonghui.address.enums.AddressUnit;
import com.yonghui.address.rule.Rule;
import com.yonghui.jieba.SegToken;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author jasonbiao
 * @date 2020-08-05 11:03
 * description: <p>
 *  二级地址包含以下单位, 如果包含B1, 则返回 一级地址 + 二级地址的第一个B1, 否者返回 一级地 + 二级地址的第一个B2：
 *  B1: 栋、幢、号楼、座（基础单位）
 *  B2: 单元（基础单位）
 *  (B1>B2)
 * </p>
 */
@Component
public class SecondIncludeMixUnit implements Rule {

    @Override
    public Boolean condition(AddressContext addressContext) {
        List<SegToken> secondUnits = addressContext.getSecondUnits();
        if(CollectionUtils.isEmpty(secondUnits)) {
            return false;
        }

        List<String> minUnits = AddressUnit.getBaseUnitCollection();

        for (SegToken secondToken : secondUnits) {
            Optional<String> unit = minUnits.stream().filter(minUnit -> secondToken.getWord().endsWith(minUnit)).findAny();
            if(unit.isPresent() && secondToken.getWord().length() > 1) {
                return true;
            }
        }

        return false;
    }

    @Override
    public DetailAddress process(AddressContext addressContext) {
        DetailAddress detailAddress = new DetailAddress();
        detailAddress.setFirstAddress(addressContext.getFirstAddress());
        List<SegToken> secondUnits = addressContext.getSecondUnits();
        List<String> baseSeocondUnits = AddressUnit.getBaseSeocondUnit();
        List<String> minUnits = AddressUnit.getMinUnit();

        String minUnit = null;
        for (SegToken secondToken : secondUnits) {
            // 获取最小的栋
            Optional<String> unit = baseSeocondUnits.stream().filter(baseSecondUnit -> secondToken.getWord().endsWith(baseSecondUnit)).findAny();
            if(unit.isPresent() && secondToken.getWord().length() > 1) {
                minUnit = unit.get();
                break;
            }

            // 栋不存在，获取最小的单元
            unit = minUnits.stream().filter(u -> secondToken.getWord().endsWith(u)).findAny();
            if(unit.isPresent() && secondToken.getWord().length() > 1) {
                minUnit = unit.get();
                break;
            }
        }

        String secondAddress = addressContext.getSecondAddress();
        detailAddress.setSecondAddress(secondAddress.substring(0, secondAddress.indexOf(minUnit)) + minUnit);
        return detailAddress;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}
