package com.yonghui.address;

import com.yonghui.address.dto.DetailAddress;
import com.yonghui.jieba.SegToken;

import java.util.List;

/**
 * @author jasonbiao
 * @date 2020-08-03 10:30
 * description: <p>
 * 地址拆分规则
 * </p>
 */
public interface AddressSplit {
    /**
     * 地址拆分, 拆分出 区、期 弄、巷 -> 号 -> 号院 ->  栋、幢、号楼、座 -> 单元，以及一些不需要的杂词
     * @param sentence 句子
     * @return 返回拆分后的词列表
     */
    List<SegToken> split(String sentence);


    /**
     * 过滤不需要的词，返回区、期 弄、巷 -> 号 -> 号院 ->  栋、幢、号楼、座 -> 单元等信息
     * @param segTokens 拆分后的词列表
     * @return 弄、巷 -> 号 -> 号院 ->  栋、幢、号楼、座 -> 单元等信息
     */
    List<String> extract(List<SegToken> segTokens);


    /**
     * @param addressContext 地址解析上下文
     * @return 规则化后的地址信息
     */
    DetailAddress process(AddressContext addressContext);
}
