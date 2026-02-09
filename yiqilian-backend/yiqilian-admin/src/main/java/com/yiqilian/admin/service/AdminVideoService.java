package com.yiqilian.admin.service;

import com.yiqilian.entity.YqlVideo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 管理后台视频服务接口
 */
public interface AdminVideoService {

    /**
     * 上传视频并保存信息
     * @param videoFile 视频文件
     * @param coverFile 封面图文件
     * @param subCategoryId 细分部位ID
     * @param title 视频标题
     * @param description 视频描述
     * @param author 作者/教练
     * @param level 难度等级
     * @return 保存后的视频信息
     * @throws Exception 上传失败时抛出异常
     */
    YqlVideo uploadVideo(MultipartFile videoFile, MultipartFile coverFile, Long subCategoryId, 
                        String title, String description, String author, String level) throws Exception;

    /**
     * 上传文件到本地存储
     * @param file 要上传的文件
     * @param directory 存储目录
     * @return 文件存储路径
     * @throws Exception 上传失败时抛出异常
     */
    String uploadFileToLocal(MultipartFile file, String directory) throws Exception;

    /**
     * 生成唯一的文件名
     * @param originalFilename 原始文件名
     * @return 唯一文件名
     */
    String generateUniqueFileName(String originalFilename);
}
