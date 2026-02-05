package com.yiqilian.service;
import com.yiqilian.model.dto.LoginDTO;
import java.util.Map;
public interface AuthService {
    Map<String, Object> login(LoginDTO loginDTO);
}
