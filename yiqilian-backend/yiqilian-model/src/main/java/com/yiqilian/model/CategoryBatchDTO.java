package com.yiqilian.model;

import lombok.Data;

import java.util.List;

@Data
public class CategoryBatchDTO {
    private String mainName; // 大项名称：如“胸部”
    private List<String> subNames; // 子项名称列表：如 ["上胸", "中胸", "下胸"]
}
