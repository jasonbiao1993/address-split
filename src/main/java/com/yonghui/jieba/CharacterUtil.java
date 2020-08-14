package com.yonghui.jieba;

import com.yonghui.jieba.util.DigitalUtil;

import java.util.regex.Pattern;


public class CharacterUtil {
    public static Pattern reSkip = Pattern.compile("(\\d+\\.\\d+|[a-zA-Z0-9]+)");
    private static final char[] connectors = new char[] { '+', '#', '&', '.', '_', '-' };


    public static boolean isChineseLetter(char ch) {
        return ch >= 0x4E00 && ch <= 0x9FA5;
    }


    public static boolean isEnglishLetter(char ch) {
        return (ch >= 0x0041 && ch <= 0x005A) || (ch >= 0x0061 && ch <= 0x007A);
    }


    public static boolean isDigit(char ch) {
        return ch >= 0x0030 && ch <= 0x0039;
    }


    public static boolean isConnector(char ch) {
        for (char connector : connectors) {
            if (ch == connector) {
                return true;
            }
        }
        return false;
    }


    public static boolean ccFind(char ch) {
        if (isChineseLetter(ch)) {
            return true;
        }
        if (isEnglishLetter(ch)) {
            return true;
        }
        if (isDigit(ch)) {
            return true;
        }
        return isConnector(ch);
    }


    /**
     * 全角 to 半角,字母小写 to 大写, 数字大写转，小写
     * 
     * @param input
     *            输入字符
     * @return 转换后的字符
     */
    public static char regularize(char input) {
        if(DigitalUtil.isBig(input)) {
            return DigitalUtil.bigToLower(input);
        }

        if (input == 12288) {
            return 32;
        }
        else if (input > 65280 && input < 65375) {
            return (char) (input - 65248);
        }
        else if (input >= 'a' && input <= 'z') {
            return (input -= 32);
        }
        return input;
    }

}
