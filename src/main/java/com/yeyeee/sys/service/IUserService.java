package com.yeyeee.sys.service;

import com.yeyeee.sys.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yeyeee
 * @since 2023-03-10
 */
public interface IUserService extends IService<User> {

    Map<String, Object> login (User user);

    Map<String, Object> getUserInfo (String token);

    void logout (String token);
}
