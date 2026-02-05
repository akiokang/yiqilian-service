package com.yiqilian.service.impl;

import com.yiqilian.common.exception.BusinessException;
import com.yiqilian.model.dto.LoginDTO;
import com.yiqilian.service.AuthService;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        if ("123456".equals(loginDTO.getCode())) {
            Map<String, Object> result = new HashMap<>();
            result.put("token", "mock-jwt-token-666888");
            result.put("nickname", "健身达人_" + loginDTO.getPhone().substring(7));
            result.put("userId", 1001);
            return result;
        } else {
            throw new BusinessException(400, "验证码不正确，请填写 123456");
        }
    }
}
