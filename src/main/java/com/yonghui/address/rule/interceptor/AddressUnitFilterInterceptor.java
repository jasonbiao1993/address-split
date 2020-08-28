package com.yonghui.address.rule.interceptor;


import com.google.common.collect.Lists;
import com.yonghui.address.AddressContext;
import com.yonghui.address.enums.AddressUnit;
import com.yonghui.jieba.SegToken;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author jasonbiao
 * @date 2020-08-14 17:01
 * description: <p>
 *
 * </p>
 */
public class AddressUnitFilterInterceptor implements AfterExtractInterceptor {

    /**
     * 排除错误分词的词列表，错误词都是以指定单位为前缀，例如单位"号"作为前缀
     * 去除举例
     * 重庆三博江陵医院  住院部内科2楼3号床， 3号床会解析成3号，需要去除
     */
    private List<String> excludeParticiples = Lists.newArrayList("号门", "号岗", "号街", "号床", "号摊");

    @Override
    public void afterExtract(AddressContext addressContext) {

        String firstAddress = addressContext.getFirstAddress();
        String secondAddress = addressContext.getSecondAddress();

        List<SegToken> firstFilterUnits = addressContext.getFirstFilterUnits();
        List<SegToken> secondFilterUnits = addressContext.getSecondFilterUnits();



        List<String> allUnit = AddressUnit.getAllUnit();
        // 去除只有单位，没有前缀的量词的segToken
        firstFilterUnits.removeIf(getSegTokenPredicate(firstAddress, allUnit));
        secondFilterUnits.removeIf((getSegTokenPredicate(secondAddress, allUnit)));


    }

    private Predicate<SegToken> getSegTokenPredicate(String address, List<String> allUnit) {
        return segToken -> {
            // 排除错误的分词
            return allUnit.stream().anyMatch(unit -> {
                boolean equals = segToken.getWord().equals(unit);
                if (equals) {
                    return true;
                }

                if (segToken.getWord().endsWith(unit)) {
                    return excludeParticiples.stream().filter(excludeParticiple -> excludeParticiple.startsWith(unit))
                            .anyMatch(excludeParticiple -> address.indexOf(excludeParticiple) == segToken.getStartOffset() + segToken.getWord().length() - unit.length());
                }
                return false;
            });
        };
    }
}
