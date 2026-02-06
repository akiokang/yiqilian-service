package com.yiqilian.service;
import com.yiqilian.model.dto.LoginDTO;
import java.util.Map;
public interface UserLoginService {
    Map<String, Object> login(LoginDTO loginDTO);
}
