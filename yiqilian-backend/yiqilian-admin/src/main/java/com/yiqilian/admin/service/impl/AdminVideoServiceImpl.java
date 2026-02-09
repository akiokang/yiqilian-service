package com.yiqilian.admin.service.impl;

import com.yiqilian.admin.service.AdminVideoService;
import com.yiqilian.entity.YqlVideo;
import com.yiqilian.service.YqlVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * 管理后台视频服务实现类
 */
@Service
public class AdminVideoServiceImpl implements AdminVideoService {

    @Autowired
    private YqlVideoService videoService;

    // 本地存储基础路径
    private static final String BASE_STORAGE_PATH = "F:\\yiqilian-videos\\";

    // 视频存储目录
    private static final String VIDEO_DIRECTORY = "videos\\";

    // 封面图存储目录
    private static final String COVER_DIRECTORY = "covers\\";

    @Override
    public YqlVideo uploadVideo(MultipartFile videoFile, MultipartFile coverFile, Long subCategoryId, 
                               String title, String description, String author, String level) throws Exception {
        // 1. 上传视频文件
        String videoPath = uploadFileToLocal(videoFile, VIDEO_DIRECTORY);

        // 2. 上传封面图文件（如果有）
        String coverPath = null;
        if (coverFile != null && !coverFile.isEmpty()) {
            coverPath = uploadFileToLocal(coverFile, COVER_DIRECTORY);
        }

        // 3. 创建并保存视频信息
        YqlVideo video = new YqlVideo();
        video.setSubCategoryId(subCategoryId);
        video.setTitle(title);
        video.setDescription(description);
        video.setAuthor(author);
        video.setCoverUrl(coverPath);
        video.setVideoUrl(videoPath);
        video.setLevel(level);
        video.setCreateTime(new Date());
        video.setPublishTime(new Date());
        video.setIsDeleted(0);

        // 4. 保存到数据库
        videoService.save(video);

        return video;
    }

    @Override
    public String uploadFileToLocal(MultipartFile file, String directory) throws Exception {
        // 确保存储目录存在
        String storagePath = BASE_STORAGE_PATH + directory;
        File storageDir = new File(storagePath);
        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                throw new IOException("无法创建存储目录: " + storagePath);
            }
        }

        // 生成唯一文件名
        String fileName = generateUniqueFileName(file.getOriginalFilename());

        // 保存文件
        File destFile = new File(storageDir, fileName);
        file.transferTo(destFile);

        // 返回相对路径，便于后续切换到其他存储方式
        return directory + fileName;
    }

    @Override
    public String generateUniqueFileName(String originalFilename) {
        // 获取文件扩展名
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }

        // 生成唯一文件名
        return UUID.randomUUID().toString() + extension;
    }
}
