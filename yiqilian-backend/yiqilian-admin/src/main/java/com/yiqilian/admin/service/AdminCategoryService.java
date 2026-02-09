package com.yiqilian.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiqilian.entity.YqlCategory;
import com.yiqilian.entity.YqlSubCategory;
import com.yiqilian.model.CategoryBatchDTO;
import com.yiqilian.model.VO.CategoryVO;
import com.yiqilian.service.YqlCategoryService;
import com.yiqilian.service.YqlSubCategoryService;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminCategoryService {

    @Autowired
    private YqlCategoryService categoryService;
    @Autowired
    private YqlSubCategoryService subCategoryService;

    /**
     * 获取全量部位树形结构
     */
    public List<CategoryVO> getCategoryTree() {
        List<YqlCategory> parents = categoryService.list();
        List<YqlSubCategory> subs = subCategoryService.list();

        // 按父ID分组
        Map<Long, List<YqlSubCategory>> subMap = subs.stream()
                .collect(Collectors.groupingBy(YqlSubCategory::getCategoryId));

        return parents.stream().map(p -> {
            CategoryVO vo = new CategoryVO();
            vo.setId(p.getId());
            vo.setName(p.getName());

            List<YqlSubCategory> currentSubs = subMap.getOrDefault(p.getId(), new ArrayList<>());
            vo.setSubs(currentSubs.stream().map(s -> {
                CategoryVO subVo = new CategoryVO();
                subVo.setId(s.getId());
                subVo.setName(s.getName());
                subVo.setCategoryId(s.getCategoryId());
                return subVo;
            }).collect(Collectors.toList()));
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 批量保存大项及所属子项
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(CategoryBatchDTO dto) {
        // 1. 保存大项
        YqlCategory category = new YqlCategory();
        category.setName(dto.getMainName());
        categoryService.save(category);

        // 2. 获取回填ID并保存子项
        Long parentId = category.getId();
        if (dto.getSubNames() != null && !dto.getSubNames().isEmpty()) {
            List<YqlSubCategory> subList = dto.getSubNames().stream()
                    .filter(name -> name != null && !name.trim().isEmpty())
                    .map(name -> {
                        YqlSubCategory sub = new YqlSubCategory();
                        sub.setName(name);
                        sub.setCategoryId(parentId);
                        return sub;
                    }).collect(Collectors.toList());
            subCategoryService.saveBatch(subList);
        }
    }
}