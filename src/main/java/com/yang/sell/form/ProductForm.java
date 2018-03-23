package com.yang.sell.form;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductForm {

    private String productId;

    /**名字*/
    private String productName;

    /**BigDecimal用来进行16位以上精确的计算
     * 科学计算用float double 商业计算用BigDecimal
     * 单价
     * */
    private BigDecimal productPrice;

    /**库存*/
    private Integer productStock;

    /**描述*/
    private String productDescription;

    /**小图*/
    private String productIcon;

    /** 类目编号 */
    private Integer categoryType;
}
