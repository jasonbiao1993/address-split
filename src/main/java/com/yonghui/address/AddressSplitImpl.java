package com.yonghui.address;

import com.alibaba.fastjson.JSON;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import com.google.common.collect.Lists;
import com.yonghui.address.enums.AddressUnit;
import com.yonghui.address.rule.Rule;
import com.yonghui.address.dto.DetailAddress;
import com.yonghui.address.rule.interceptor.*;
import com.yonghui.jieba.JiebaSegmenter;
import com.yonghui.jieba.SegToken;
import com.yonghui.jieba.WordDictionary;
import com.yonghui.util.StringEscapeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author jasonbiao
 * @date 2020-08-03 10:31
 * description: <p>
 *
 * </p>
 */
@Component("addressSplit")
@Slf4j
public class AddressSplitImpl implements AddressSplit , BeanPostProcessor {

    private static List<Rule> rules = new ArrayList<>();

    private static JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();

    private static final Object LOCK = new Object();

    private static Boolean init = Boolean.FALSE;

    /**
     * 拦截器集合
     * 处理优先级按照保存优先级
     */
    private static List<RuleInterceptor> interceptors = Lists.newArrayList();

    public AddressSplitImpl() {
        synchronized (LOCK) {
            if(!init) {
                WordDictionary.getInstance().init();
                init = true;
            }
        }

        addInterceptor(new AddressUniqInterceptor());
        addInterceptor(new AddressUnitFilterInterceptor());
    }

    /**
     * 添加拦截器，用于后续扩展
     * @param ruleInterceptor 规则拦截器
     */
    public void addInterceptor(RuleInterceptor ruleInterceptor) {
        if(Objects.isNull(ruleInterceptor)) {
            throw new NullPointerException("不能添加null的规则拦截器");
        }
        interceptors.add(ruleInterceptor);
    }


    /**
     * 拆解地址
     * @param sentence 句子
     * @return 返回拆分列表
     */
    @Override
    public List<SegToken> split(String sentence) {
        // 繁体->简体
        sentence = ZhConverterUtil.toSimple(sentence);

        // 去除空串
        sentence = StringUtil.trimAnyBlank(sentence);

        // 去除html 转译
        sentence = StringEscapeUtils.unescapeHtml4(sentence);
        return jiebaSegmenter.process(sentence, JiebaSegmenter.SegMode.SEARCH);
    }

    /**
     * 过滤出我们需要的地址
     * @param segTokens 拆分后的词列表
     * @return
     */
    @Override
    public List<SegToken> extract(List<SegToken> segTokens) {
        List<String> filterUnit = AddressUnit.getAllUnit();
        return segTokens.stream().filter(segToken -> filterUnit.stream().anyMatch(segToken.word::endsWith)).collect(Collectors.toList());
    }

    @Override
    public DetailAddress process(AddressContext addressContext) {
        try {
            List<SegToken> firstSeg = split(addressContext.getFirstAddress());
            List<SegToken> secondSeg = split(addressContext.getSecondAddress());

            addressContext.setFirstSegToken(firstSeg);
            addressContext.setSecondSegToken(secondSeg);

            addressContext.setFirstAddress(firstSeg.stream().map(SegToken::getWord).collect(Collectors.joining()));
            addressContext.setSecondAddress(secondSeg.stream().map(SegToken::getWord).collect(Collectors.joining()));
            addressContext.setFirstFilterUnits(extract(firstSeg));
            addressContext.setSecondFilterUnits(extract(secondSeg));

            // 经过提取单位后的后置处理
            interceptors.stream().filter(interceptor -> interceptor instanceof AfterExtractInterceptor)
                    .forEach(interceptor -> ((AfterExtractInterceptor )interceptor).afterExtract(addressContext));

            Rule rule = rules.stream().filter(ruleFileter -> ruleFileter.condition(addressContext)).findFirst().orElse(null);
            if(Objects.isNull(rule)) {
                throw new Exception("没有找到地址拆封规则");
            }
            DetailAddress detailAddress = rule.process(addressContext);

            // 经过规则处理后的后置处理
            interceptors.stream().filter(interceptor -> interceptor instanceof AfterCompletionInterceptor)
                    .forEach(interceptor -> ((AfterCompletionInterceptor )interceptor).afterCompletion(addressContext, detailAddress));
            return detailAddress;
        } catch (Exception e) {
            log.error("address split error, addressContext{}", JSON.toJSONString(addressContext), e);
            return null;
        }
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 地址处理规则
        if(bean instanceof Rule) {
            rules.add((Rule) bean);

            // 地址处理规则排序
            AnnotationAwareOrderComparator.sort(rules);
        }
        return bean;
    }
}
