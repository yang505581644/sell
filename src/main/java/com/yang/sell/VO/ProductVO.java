package com.yang.sell.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品(包含类目)
 * */
@Data
public class ProductVO implements Serializable {

    private static final long serialVersionUID = 3068837304742385821L;

    @JsonProperty("name")
    private String categoryName;

    @JsonProperty("tyoe")
    private Integer categoryType;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;

}
