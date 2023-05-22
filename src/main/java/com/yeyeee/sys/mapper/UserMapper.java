package com.yeyeee.sys.mapper;

import com.yeyeee.sys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yeyeee
 * @since 2023-03-10
 */
public interface UserMapper extends BaseMapper<User> {
    public List<String> getRoleNamesByUserId(Integer userId);
}
