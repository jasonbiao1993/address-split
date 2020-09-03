package com.yonghui.dict;


import com.google.common.collect.Lists;
import com.yonghui.jieba.util.Number2ChineseUtil;

import java.util.List;

public class RuleGenerator {

    private static List<String> letters = Lists.newArrayList("A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H",
            "I",
            "J",
            "K",
            "L",
            "M",
            "N",
            "O",
            "P",
            "Q",
            "R",
            "S",
            "T",
            "U",
            "V",
            "W",
            "X",
            "Y",
            "Z");


    private static List<String> jias  = Lists.newArrayList(
            "甲",
            "乙",
            "丙",
            "丁",
            "戊",
            "己",
            "庚",
            "辛",
            "壬",
            "癸"
    );

    public static void main(String[] args) {
        for (int i = 1; i < 1000; i++) {
            System.out.println(Number2ChineseUtil.toChinese(i,false) + "号附");
        }

//        for(String letter : letters) {
//            System.out.println(letter + "区");
//        }
//        for(int i = 1; i < 100; i++)
//        for(String letter : letters) {
//            System.out.println(i  + letter + "区");
//        }
//        for(int i = 1; i < 1000; i++)
//            for(String letter : letters) {
//                System.out.println(i + letter + "号");
//             }


//        for (int i = 1; i < 20; i++)
//            for (String jia : jias) {
//                System.out.println(jia  + "区");
//            }
    }
}
