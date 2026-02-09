package com.yiqilian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiqilian.entity.YqlCategory;
import com.yiqilian.entity.YqlSubCategory;
import com.yiqilian.service.YqlCategoryService;
import com.yiqilian.mapper.YqlCategoryMapper;
import com.yiqilian.service.YqlSubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 联想
* @description 针对表【yql_category(训练大项配置表)】的数据库操作Service实现
* @createDate 2026-02-06 18:52:42
*/
@Service
public class YqlCategoryServiceImpl extends ServiceImpl<YqlCategoryMapper, YqlCategory>
    implements YqlCategoryService{

@Autowired
    private YqlSubCategoryService yqlSubCategoryService;
    // 在 YqlCategoryServiceImpl 中
    public boolean safeDeleteCategory(Long categoryId) {
        // 校验是否存在子项
        long count = yqlSubCategoryService.count(
                new LambdaQueryWrapper<YqlSubCategory>().eq(YqlSubCategory::getCategoryId, categoryId));
        if (count > 0) {
            throw new RuntimeException("该部位下已有细分动作，请先删除细分动作");
        }
        return this.removeById(categoryId);
    }


}




