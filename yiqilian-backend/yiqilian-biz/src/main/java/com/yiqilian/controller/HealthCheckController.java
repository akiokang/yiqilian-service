package com.yiqilian.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器，用于测试服务是否正常启动
 */
@RestController
@RequestMapping("/health")
public class HealthCheckController {

    /**
     * 健康检查接口
     *
     * @return 返回服务状态信息
     */
    @GetMapping("/check")
    public Map<String, Object> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Service is running normally.");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}
