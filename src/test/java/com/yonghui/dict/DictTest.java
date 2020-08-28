package com.yonghui.dict;


import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import com.yonghui.jieba.JiebaSegmenter;
import com.yonghui.jieba.SegToken;
import com.yonghui.jieba.WordDictionary;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

/**
 * @author jasonbiao
 * @date 2020-07-28 15:46
 * description: <p>
 *
 * </p>
 */
public class DictTest {

    private JiebaSegmenter segmenter = new JiebaSegmenter();

    String[] sentences =
            new String[] {
                    "住院部内科2楼3号床"
            };


    @Test
    public void testCutForIndex() {
        WordDictionary.getInstance().init();

        for (String sentence : sentences) {
            sentence = ZhConverterUtil.toSimple(sentence);
            sentence = StringUtil.trimAnyBlank(sentence);
            List<SegToken> tokens = segmenter.process(sentence, JiebaSegmenter.SegMode.SEARCH);
            System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, tokens.toString()));

            System.out.println();

            System.out.println(sentence.indexOf("号床"));
        }


    }

}
