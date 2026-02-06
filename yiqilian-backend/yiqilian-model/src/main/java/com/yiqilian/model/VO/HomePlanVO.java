package com.yiqilian.model.VO;

import com.yiqilian.entity.YqlTrainingRecord;
import com.yiqilian.entity.YqlVideo;
import lombok.Data;

import java.util.List;

@Data
public class HomePlanVO {
    private List<CategoryPlanVO> dailyPlans;
    private VideoSectionVO videoSection;

    @Data
    public static class CategoryPlanVO {
        private Long detailId;
        private Long categoryId;
        private String muscleGroup;
        private List<YqlTrainingRecord> sets;
    }

    @Data
    public static class VideoSectionVO {
        // 核心改变：每个细分部位对象里，直接包含它自己的视频列表
        private List<SubCategoryWithVideoVO> subCategoryData;
    }

    @Data
    public static class SubCategoryWithVideoVO {
        private Long id;         // 细分部位ID (如：上胸的ID)
        private String name;     // 细分部位名称 (如：'上胸')
        private List<YqlVideo> videos; // 该部位下的视频列表
    }
}