package com.yiqilian.service;

import com.yiqilian.entity.YqlTrainingPlan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yiqilian.model.VO.HomePlanVO;

/**
* @author 联想
* @description 针对表【yql_training_plan(训练计划主表)】的数据库操作Service
* @createDate 2026-02-06 18:52:42
*/
public interface YqlTrainingPlanService extends IService<YqlTrainingPlan> {
    public HomePlanVO getTodayHomeData(Long userId, String date);

    }
