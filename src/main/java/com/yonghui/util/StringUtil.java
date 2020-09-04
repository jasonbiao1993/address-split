package com.yonghui.util;

import org.springframework.util.StringUtils;

/**
 * @author jasonbiao
 * @date 2020-09-04 10:59
 * description: <p>
 *
 * </p>
 */
public class StringUtil {


    /**
     * 去除空格，如果空格前后是相同数字类型或者字母类型，则保留空格
     *
     * @param string
     * @return
     */
    public static String trimBlank(String string) {
        if (StringUtils.isEmpty(string)) {
            return string;
        } else {
            try {
                String[] splits = string.split("\\s+|\u0013");

                StringBuilder builder = new StringBuilder(splits[0]);
                for (int i = 1; i < splits.length; i++) {
                    char prev = splits[i - 1].charAt(splits[i - 1].length() - 1);
                    char last = splits[i].charAt(0);

                    if (isLetterAndDigit(prev) && isLetterAndDigit(last)) {
                        builder.append(" ").append(splits[i]);
                    } else {
                        builder.append(splits[i]);
                    }
                }
                return builder.toString();
            } catch (Exception e) {
                return string;
            }
        }
    }

    public static boolean isLetterAndDigit(char ch) {
        return isEnglishLetter(ch) || isDigit(ch);
    }

    public static boolean isEnglishLetter(char ch) {
        return (ch >= 0x0041 && ch <= 0x005A) || (ch >= 0x0061 && ch <= 0x007A);
    }


    public static boolean isDigit(char ch) {
        return ch >= 0x0030 && ch <= 0x0039;
    }
}
