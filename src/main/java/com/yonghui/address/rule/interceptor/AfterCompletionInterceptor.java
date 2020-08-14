package com.yonghui.address.rule.interceptor;

import com.yonghui.address.AddressContext;
import com.yonghui.address.dto.DetailAddress;
import com.yonghui.address.rule.Rule;

/**
 * @author jasonbiao
 * @date 2020-08-14 17:05
 * description: <p>
 *
 * </p>
 */
public interface AfterCompletionInterceptor extends RuleInterceptor  {
    /**
     * 经过{@link Rule} 处理后，对结果的进一步处理
     * @param context 地址上下文
     * @param detailAddress 详细地址
     */
    void afterCompletion(AddressContext context, DetailAddress detailAddress);
}
