package com.yonghui.address.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jasonbiao
 * @date 2020-08-05 15:46
 * description: <p>
 *
 * </p>
 */
@Getter
public enum AddressUnit {
    /**
     * 地址相关单位
     */
    QI("期", 100),
    QU("区", 100),
    NONG("弄", 5),
    XIANG("巷", 5),
    JIEQU("街区", 5),
    HAOJIEQU("号街区", 5),
    HAOFU("号附", 5),
    HAO("号", 4),
    HAOYUAN("号院", 3),
    DONG("栋", 2),
    ZHUANG("幢", 2),
    HAOLOU("号楼", 2),
    ZHUO("座", 2),
    DANYUAN("单元", 1),
    ;

    AddressUnit(String unit, Integer level) {
        this.unit = unit;
        this.level = level;
    }

    private String unit;

    private Integer level;


    public static List<String> getBaseUnitCollection() {
        return Arrays.stream(AddressUnit.values()).filter(unit -> unit.level == 1 || unit.level == 2).map(AddressUnit::getUnit).collect(Collectors.toList());
    }

    public static List<String> getBaseSeocondUnit() {
        return Arrays.stream(AddressUnit.values()).filter(unit -> unit.level == 2).map(AddressUnit::getUnit).collect(Collectors.toList());
    }

    public static List<String> getAllUnit() {
        return Arrays.stream(AddressUnit.values()).map(AddressUnit::getUnit).collect(Collectors.toList());
    }

    public static List<String> getMinUnit() {
        return Arrays.stream(AddressUnit.values()).filter(unit -> unit.level == 1).map(AddressUnit::getUnit).collect(Collectors.toList());
    }


    public static List<String> getPrefixUnit() {
        return Arrays.stream(AddressUnit.values()).filter(unit -> unit.level == 3 || unit.level == 4 || unit.level == 5 || unit.level == 100).map(AddressUnit::getUnit).collect(Collectors.toList());
    }

    public static List<AddressUnit> getPrefixUnitEnum() {
        return Arrays.stream(AddressUnit.values()).filter(unit -> unit.level == 3 || unit.level == 4 || unit.level == 5 || unit.level == 100).collect(Collectors.toList());
    }
}
