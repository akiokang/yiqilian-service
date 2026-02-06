package com.yiqilian.controller;

import com.yiqilian.entity.R;
import com.yiqilian.model.VO.HomePlanVO;
import com.yiqilian.service.YqlTrainingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
/**
 * 训练计划
 */
@RestController
@RequestMapping("/api")
public class TrainingPlanController {

    @Qualifier("yqlTrainingPlanServiceImpl")
    @Autowired
    private YqlTrainingPlanService yqlTrainingPlanService;

    /**
     * 获取今日计划
     * @param userId
     * @return
     */
    @GetMapping("/get/planToday")
    public R initHome(@RequestParam Long userId) {
        // 获取当前服务器日期，对应 yql_training_plan 的 plan_date
        String today = LocalDate.now().toString();
        HomePlanVO data = yqlTrainingPlanService.getTodayHomeData(userId, today);
        return R.success(data);
    }
}
