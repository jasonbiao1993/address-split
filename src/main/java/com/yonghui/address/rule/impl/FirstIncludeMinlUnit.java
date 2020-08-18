package com.yonghui.address.rule.impl;

import com.yonghui.address.AddressContext;
import com.yonghui.address.dto.DetailAddress;
import com.yonghui.address.enums.AddressUnit;
import com.yonghui.address.rule.Rule;
import com.yonghui.jieba.SegToken;
import org.apache.commons.collections.SortedBag;
import org.apache.commons.collections.bag.TreeBag;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author jasonbiao
 * @date 2020-08-03 17:33
 * description: <p>
 *  一级地址包含一下单位，丢弃二级地址：
 *  B1: 栋、幢、号楼、座（基础单位）
 *  B2: 单元（基础单位）
 *  (B1>B2)
 * </p>
 */
@Component
public class FirstIncludeMinlUnit implements Rule {


    @Override
    public Boolean condition(AddressContext addressContext) {
        List<SegToken> firstUnits = addressContext.getFirstUnits();
        if(CollectionUtils.isEmpty(firstUnits)) {
            return false;
        }

        List<String> baseUnits = AddressUnit.getBaseUnitCollection();

        SortedBag unitCount = new TreeBag();
        for (SegToken firstToken : firstUnits) {
            Optional<String> unit = baseUnits.stream().filter(baseUnit -> firstToken.getWord().endsWith(baseUnit)).findAny();
            if(unit.isPresent() && firstToken.getWord().length() > 1) {
                unitCount.add(unit.get());
            }
        }

        if (unitCount.size() == 0) {
            return false;
        }

        return baseUnits.stream().noneMatch(unit -> unitCount.getCount(unit) > 1);
    }

    @Override
    public DetailAddress process(AddressContext addressContext) {
        DetailAddress detailAddress = new DetailAddress();
        detailAddress.setFirstAddress(addressContext.getFirstAddress());
        return detailAddress;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
