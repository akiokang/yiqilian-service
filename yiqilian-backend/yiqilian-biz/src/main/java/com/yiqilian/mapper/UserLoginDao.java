package com.yiqilian.mapper;

import com.yiqilian.Entity.UserDto;

public interface UserLoginDao {
    int deleteByPrimaryKey(Long id);

    int insert(UserDto record);

    int insertSelective(UserDto record);

    UserDto selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserDto record);

    int updateByPrimaryKey(UserDto record);
}