package com.yiqilian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiqilian.entity.*;
import com.yiqilian.model.VO.HomePlanVO;
import com.yiqilian.service.*;
import com.yiqilian.mapper.YqlTrainingPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author 联想
* @description 针对表【yql_training_plan(训练计划主表)】的数据库操作Service实现
* @createDate 2026-02-06 18:52:42
*/
@Service
public class YqlTrainingPlanServiceImpl extends ServiceImpl<YqlTrainingPlanMapper, YqlTrainingPlan>
    implements YqlTrainingPlanService{

    @Autowired
    private YqlPlanDetailService yqlPlanDetailService;
    @Autowired
    private YqlTrainingRecordService yqlTrainingRecordService;
    @Autowired
    private YqlCategoryService yqlCategoryService;
    @Autowired
    private YqlSubCategoryService yqlSubCategoryService;
    @Autowired
    private YqlVideoService yqlVideoService;

    @Override
    public HomePlanVO getTodayHomeData(Long userId, String date) {
         HomePlanVO vo = new HomePlanVO();

        // 1. 获取今日主计划 [YqlTrainingPlan]
        YqlTrainingPlan plan = this.getOne(new LambdaQueryWrapper<YqlTrainingPlan>()
                .eq(YqlTrainingPlan::getUserId, userId)
                .eq(YqlTrainingPlan::getPlanDate, date));
        if (plan == null) return vo;

        // 2. 获取计划关联部位 [YqlPlanDetail]
        List<YqlPlanDetail> details = yqlPlanDetailService.list(new LambdaQueryWrapper<YqlPlanDetail>()
                .eq(YqlPlanDetail::getPlanId, plan.getId()));
        if (details.isEmpty()) return vo;

        // 批量提取 ID 用于后续查询
        List<Long> categoryIds = details.stream().map(YqlPlanDetail::getCategoryId).collect(Collectors.toList());
        List<Long> detailIds = details.stream().map(YqlPlanDetail::getId).collect(Collectors.toList());

        // 3. 性能优化：批量查询名称 [YqlCategory] 和 记录 [YqlTrainingRecord]
        Map<Long, String> categoryMap = yqlCategoryService.listByIds(categoryIds).stream()
                .collect(Collectors.toMap(YqlCategory::getId, YqlCategory::getName));

        List<YqlTrainingRecord> allRecords = yqlTrainingRecordService.list(new LambdaQueryWrapper<YqlTrainingRecord>()
                .in(YqlTrainingRecord::getDetailId, detailIds)
                .orderByAsc(YqlTrainingRecord::getSetNum));

        // 4. 组装前端所需的一级标签和组数
        List<HomePlanVO.CategoryPlanVO> dailyPlanVOs = details.stream().map(d -> {
            HomePlanVO.CategoryPlanVO dvo = new HomePlanVO.CategoryPlanVO();
            dvo.setDetailId(d.getId());
            dvo.setCategoryId(d.getCategoryId());
            dvo.setMuscleGroup(categoryMap.get(d.getCategoryId()));
            dvo.setSets(allRecords.stream()
                    .filter(r -> r.getDetailId().equals(d.getId()))
                    .collect(Collectors.toList()));
            return dvo;
        }).collect(Collectors.toList());
        vo.setDailyPlans(dailyPlanVOs);

        // 5. 视频联动逻辑：默认取第一个部位
        if (!dailyPlanVOs.isEmpty()) {
            Long firstCategoryId = dailyPlanVOs.get(0).getCategoryId();

            // 1. 获取该大项下的所有细分部位 (如：上胸、中胸、下胸)
            List<YqlSubCategory> subs = yqlSubCategoryService.list(new LambdaQueryWrapper<YqlSubCategory>()
                    .eq(YqlSubCategory::getCategoryId, firstCategoryId));

            if (!subs.isEmpty()) {
                List<Long> subIds = subs.stream().map(YqlSubCategory::getId).collect(Collectors.toList());

                // 2. 批量查询这些细分部位下的所有视频 (避免循环查库)
                List<YqlVideo> allVideos = yqlVideoService.list(new LambdaQueryWrapper<YqlVideo>()
                        .in(YqlVideo::getSubCategoryId, subIds));

                // 3. 将视频按 subCategoryId 分组
                Map<Long, List<YqlVideo>> videoMap = allVideos.stream()
                        .collect(Collectors.groupingBy(YqlVideo::getSubCategoryId));

                // 4. 组装嵌套结构
                List<HomePlanVO.SubCategoryWithVideoVO> subData = subs.stream().map(s -> {
                    HomePlanVO.SubCategoryWithVideoVO subVO = new HomePlanVO.SubCategoryWithVideoVO();
                    subVO.setId(s.getId());
                    subVO.setName(s.getName());
                    // 从分组后的 Map 中取值，如果没有视频则返回空列表
                    subVO.setVideos(videoMap.getOrDefault(s.getId(), new ArrayList<>()));
                    return subVO;
                }).collect(Collectors.toList());

                HomePlanVO.VideoSectionVO videoSection = new HomePlanVO.VideoSectionVO();
                videoSection.setSubCategoryData(subData);
                vo.setVideoSection(videoSection);
            }
        }
        return vo;
    }
}




