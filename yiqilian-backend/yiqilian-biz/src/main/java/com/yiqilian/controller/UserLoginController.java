package com.yiqilian.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.yiqilian.entity.R;
import com.yiqilian.entity.UserInfo;
import com.yiqilian.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用户登录
 */
@RestController
@RequestMapping("/public")
public class UserLoginController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * cc
     *
     * @return 返回服务状态信息
     */
    @PostMapping("/login")
    public R userLogin(@RequestParam String phone) {
        // 参数校验
        if (phone == null || phone.isEmpty()) {
            return R.error("手机号不能为空");
        }

        try {
            // 构造查询条件
            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone", phone);

            // 查询用户信息
            UserInfo userInfo = userInfoService.getOne(queryWrapper);

            if (userInfo != null) {
                return R.success(userInfo); // 登录成功，返回用户信息
            } else {
                return R.error("用户不存在"); // 用户不存在
            }
        } catch (Exception e) {
            return R.error("系统异常：" + e.getMessage()); // 系统异常
        }
    }
    @PostMapping("/auth/code")
    public R sendVerificationCode(@RequestParam String phone) {
        // 1. 校验手机号格式
        if (phone == null || !phone.matches("^1[3-9]\\d{9}$")) {
            return R.error("手机号格式不正确");
        }

        // 2. 生成 6 位随机数字验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));

        // 3. 存入 Redis 缓存（假设 RedisTemplate 已注入）
        redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

        // 4. 模拟发送短信
        System.out.println("验证码已发送至手机号：" + phone + "，验证码为：" + code);

        return R.success("验证码发送成功");
    }

    @PostMapping("/auth/login")
    public R login(@RequestParam String phone, @RequestParam String code) {
        // 1. 参数校验
        if (phone == null || phone.isEmpty()) {
            return R.error("手机号不能为空");
        }
        if (code == null || code.isEmpty()) {
            return R.error("验证码不能为空");
        }

        // 2. 验证码比对
        // String cachedCode = (String) redisTemplate.opsForValue().get(phone);
        // if (!code.equals(cachedCode)) {
        //     return R.error("验证码错误或已过期");
        // }

        try {
            // 3. 查询用户是否存在
            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone", phone);
            UserInfo userInfo = userInfoService.getOne(queryWrapper);

            // 4. 用户不存在则自动注册
            if (userInfo == null) {
                userInfo = new UserInfo();
                userInfo.setPhone(phone);
                userInfo.setCreateTime(new Date());
                userInfoService.save(userInfo); // 插入新用户
            }

            // 5. 生成 Token（这里简化为 UUID，实际可使用 JWT）
            String token = UUID.randomUUID().toString();

            // 6. 返回用户信息和 Token
            Map<String, Object> result = new HashMap<>();
            result.put("userInfo", userInfo);
            result.put("token", token);
            return R.success(result);
        } catch (Exception e) {
            return R.error("系统异常：" + e.getMessage());
        }
    }


}
