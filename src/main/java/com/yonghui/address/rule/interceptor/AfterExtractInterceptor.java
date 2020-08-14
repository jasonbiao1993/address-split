package com.yonghui.address.rule.interceptor;

import com.yonghui.address.AddressContext;

/**
 * @author jasonbiao
 * @date 2020-08-14 17:06
 * description: <p>
 *
 * </p>
 */
public interface AfterExtractInterceptor extends RuleInterceptor {
    /**
     * 在extractc之后进行的处理
     * @param addressContext 地址上下文
     */
    void afterExtract(AddressContext addressContext);
}
