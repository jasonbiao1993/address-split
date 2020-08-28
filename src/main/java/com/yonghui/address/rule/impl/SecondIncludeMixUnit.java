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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<SegToken> secondUnits = addressContext.getSecondFilterUnits();
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
        List<SegToken> secondUnits = addressContext.getSecondFilterUnits();
        List<String> baseSeocondUnits = AddressUnit.getBaseSeocondUnit();
        List<String> minUnits = AddressUnit.getMinUnit();

        SegToken minUnit = null;

        // 获取楼栋
        List<SegToken> segTokenMinUnits = secondUnits.stream().filter(segToken -> baseSeocondUnits.stream().anyMatch(baseSeocondUnit -> segToken.getWord().endsWith(baseSeocondUnit))).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(segTokenMinUnits)) {
            minUnit = segTokenMinUnits.get(segTokenMinUnits.size() - 1);
        }

        // 楼栋不存在，获取单元
        if(Objects.isNull(minUnit)) {
            minUnit = secondUnits.stream().filter(segToken -> minUnits.stream().anyMatch(minUnitFilter -> segToken.getWord().endsWith(minUnitFilter))).findAny().orElse(null);
        }

        if(Objects.nonNull(minUnit)) {
            String secondAddress = addressContext.getSecondAddress();
            detailAddress.setSecondAddress(secondAddress.substring(0, minUnit.getStartOffset()) + minUnit.getWord());
        }
        return detailAddress;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}
