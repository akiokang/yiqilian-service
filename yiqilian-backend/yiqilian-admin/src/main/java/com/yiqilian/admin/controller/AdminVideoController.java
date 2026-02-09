package com.yiqilian.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiqilian.admin.service.AdminVideoService;
import com.yiqilian.entity.R;
import com.yiqilian.entity.YqlVideo;
import com.yiqilian.service.YqlVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

@RestController
@RequestMapping("/admin/video")
public class AdminVideoController {

    @Autowired
    private YqlVideoService videoService;
    
    @Autowired
    private AdminVideoService adminVideoService;

    /**
     * 分页查询视频列表
     */
    @GetMapping("/page")
    public R getPage(@RequestParam int current, @RequestParam int size, Long subCategoryId) {
        Page<YqlVideo> page = new Page<>(current, size);
        LambdaQueryWrapper<YqlVideo> wrapper = new LambdaQueryWrapper<>();
        // 级联查询逻辑：如果传了细分部位 ID，则过滤
        wrapper.eq(subCategoryId != null, YqlVideo::getSubCategoryId, subCategoryId)
                .orderByDesc(YqlVideo::getCreateTime);
        return R.success(videoService.page(page, wrapper));
    }

    /**
     * 核心接口：上传/新增视频并绑定部位
     */
    @PostMapping("/save")
    public R saveVideo(@RequestBody YqlVideo video) {
        // 逻辑：前端传来的 video 对象中必须包含 subCategoryId
        video.setCreateTime(new Date());
        video.setPublishTime(new Date());
        videoService.save(video);
        // 保存后，由于 subCategoryId 已经关联，App 端的 planToday 接口会立即搜到这个视频
        return R.success("视频发布成功");
    }

    /**
     * 删除视频（逻辑级联：删除视频不影响部位，但需要同步清理文件，此处略）
     */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        videoService.removeById(id);
        return R.success("删除成功");
    }
    
    /**
     * 上传视频接口
     * 支持视频文件上传和视频信息保存
     */
    @PostMapping("/upload")
    public R uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("subCategoryId") Long subCategoryId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("author") String author,
            @RequestParam("level") String level,
            @RequestParam(value = "coverFile", required = false) MultipartFile coverFile) {
        try {
            YqlVideo video = adminVideoService.uploadVideo(file, coverFile, subCategoryId, title, description, author, level);
            return R.success(video);
        } catch (Exception e) {
            return R.error("视频上传失败：" + e.getMessage());
        }
    }
}