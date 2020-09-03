package com.yonghui.address.rule.interceptor;


import com.google.common.collect.Lists;
import com.yonghui.address.AddressContext;
import com.yonghui.address.enums.AddressUnit;
import com.yonghui.jieba.CharacterUtil;
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
    private List<String> excludeParticiples = Lists.newArrayList("号门", "号岗", "号街", "号床", "号摊", "号房");


    /**
     * 以指定字符串结尾的词去除
     */
    private List<String> excludeSuffix = Lists.newArrayList("小区");


    private List<String> includeChinesePrefix = Lists.newArrayList("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸", "零", "一", "二", "三", "四", "五", "六", "七", "八", "九",  "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "十", "百", "千", "拾", "佰", "仟");

    @Override
    public void afterExtract(AddressContext addressContext) {

        String firstAddress = addressContext.getFirstAddress();
        String secondAddress = addressContext.getSecondAddress();

        List<SegToken> firstFilterUnits = addressContext.getFirstFilterUnits();
        List<SegToken> secondFilterUnits = addressContext.getSecondFilterUnits();

        List<String> allUnit = AddressUnit.getAllUnit();
        firstFilterUnits.removeIf(getSegTokenPredicate(firstAddress, allUnit));
        secondFilterUnits.removeIf((getSegTokenPredicate(secondAddress, allUnit)));


        // 去除指定字符结尾的分词
        firstFilterUnits.removeIf((segToken -> excludeSuffix.stream().anyMatch(suffix -> segToken.getWord().endsWith(suffix))));
        secondFilterUnits.removeIf((segToken -> excludeSuffix.stream().anyMatch(suffix -> segToken.getWord().endsWith(suffix))));

        // 去除二级地址：1楼1号，相关分词
        secondFilterUnits.removeIf(segToken -> segToken.getWord().endsWith("号") && segToken.getStartOffset() - 1 >= 0  && "楼".equals(secondAddress.substring(segToken.getStartOffset() - 1, segToken.getStartOffset())));

    }

    private Predicate<SegToken> getSegTokenPredicate(String address, List<String> allUnit) {
        return segToken -> {
            // 排除错误的分词
            return allUnit.stream().anyMatch(unit -> {
                // 去除只有单位，没有前缀的量词的segToken
                boolean equals = segToken.getWord().equals(unit);
                if (equals) {
                    return true;
                }

                // 如果不包含在includeChinesePrefix里面，则去除该词
                if (segToken.getWord().endsWith(unit)) {
                    String word = segToken.getWord();
                    word = word.substring( word.length() - unit.length() - 1, word.length() - unit.length());

                    try {
                        if (CharacterUtil.isChineseLetter(word.charAt(0)) && !includeChinesePrefix.contains(word)) {
                            return true;
                        }
                    } catch (Exception e) {}
                }


                // 排除错误分词的词列表，错误词都是以指定单位为前缀，例如单位"号"作为前缀
                // 去除举例
                // 重庆三博江陵医院  住院部内科2楼3号床， 3号床会解析成3号，需要去除
                if (segToken.getWord().endsWith(unit)) {
                    return excludeParticiples.stream().filter(excludeParticiple -> excludeParticiple.startsWith(unit))
                            .anyMatch(excludeParticiple -> address.indexOf(excludeParticiple) == segToken.getStartOffset() + segToken.getWord().length() - unit.length());
                }
                return false;
            });
        };
    }
}
