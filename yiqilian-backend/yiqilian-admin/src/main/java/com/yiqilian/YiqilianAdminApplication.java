package com.yiqilian;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 管理后台启动类
 * scanBasePackages: 确保扫描到 com.yiqilian 包下所有模块的组件（如 Service/Component）
 */
@SpringBootApplication(scanBasePackages = "com.yiqilian")
@MapperScan("com.yiqilian.mapper") // 扫描全局 Mapper 接口路径
public class YiqilianAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(YiqilianAdminApplication.class, args);
    }
}