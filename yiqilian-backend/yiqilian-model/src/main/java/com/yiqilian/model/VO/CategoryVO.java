package com.yiqilian.model.VO;

import lombok.Data;
import java.util.List;

/**
 * 树形结构视图对象
 */
@Data
public class CategoryVO {
    private Long id;
    private String name;
    private Long categoryId; // 父项ID，大项此值为null
    private List<CategoryVO> subs; // 子项列表
}