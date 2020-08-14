package com.yonghui.jieba.util;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author jasonbiao
 * @date 2020-07-29 14:12
 * description: <p>
 *
 * </p>
 */
public class DigitalUtil {
    private static List<String> bigLowers = Lists.newArrayList(
            "壹一","贰二","叁三","肆四","伍五","陆六","柒七","捌八","玖九","零零","拾十","佰百","仟千","万万","亿亿"
    );

    public static char bigToLower(char big) {
        return bigLowers.stream().filter(bigLower -> bigLower.startsWith(String.valueOf(big))).map(bigLower -> bigLower.substring(1).toCharArray()[0]).findFirst().orElse(big);
    }


    public static Boolean isBig(char digital) {
        return bigLowers.stream().anyMatch(bigLower -> bigLower.startsWith(String.valueOf(digital)));
    }
}
