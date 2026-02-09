package com.yiqilian.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiqilian.admin.service.AdminCategoryService;
 import com.yiqilian.entity.R;
import com.yiqilian.entity.YqlSubCategory;
import com.yiqilian.model.CategoryBatchDTO;
import com.yiqilian.model.VO.CategoryVO;
import com.yiqilian.service.YqlCategoryService;
import com.yiqilian.service.YqlSubCategoryService;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Autowired
    private AdminCategoryService adminCategoryService;
    @Autowired
    private YqlCategoryService categoryService;
    @Autowired
    private YqlSubCategoryService subCategoryService;

    @GetMapping("/tree")
    public R<List<CategoryVO>> tree() {
        return R.success(adminCategoryService.getCategoryTree());
    }

    @PostMapping("/saveBatch")
    public R saveBatch(@RequestBody CategoryBatchDTO dto) {
        adminCategoryService.saveBatch(dto);
        return R.success("批量保存成功");
    }

    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id, @RequestParam Boolean isSub) {
        if (isSub) {
            // 子项直接删（实际业务中应先检查是否有关联视频）
            subCategoryService.removeById(id);
        } else {
            // 大项需检查级联
            long count = subCategoryService.count(new LambdaQueryWrapper<YqlSubCategory>()
                    .eq(YqlSubCategory::getCategoryId, id));
            if (count > 0) {
                return R.error("该部位下仍有子项，请先清空子项再删除大项");
            }
            categoryService.removeById(id);
        }
        return R.success("删除成功");
    }
}