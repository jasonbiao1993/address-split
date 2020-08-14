package com.yonghui.address.rule;

import com.yonghui.address.AddressContext;
import com.yonghui.address.dto.DetailAddress;
import org.springframework.core.Ordered;

/**
 * @author jasonbiao
 * @date 2020-08-03 15:13
 * description: <p>
 *
 * </p>
 */
public interface Rule extends Ordered {

    Boolean condition(AddressContext addressContext);

    DetailAddress process(AddressContext addressContext);

}
