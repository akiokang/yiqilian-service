package com.yiqilian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiqilian.entity.UserInfo;
import com.yiqilian.service.UserInfoService;
import com.yiqilian.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author 联想
* @description 针对表【user_info】的数据库操作Service实现
* @createDate 2026-02-06 18:52:42
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

}




