package com.yeyeee.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeyeee.common.utils.JwtUtil;
import com.yeyeee.sys.entity.User;
import com.yeyeee.sys.mapper.UserMapper;
import com.yeyeee.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yeyeee
 * @since 2023-03-10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

//    @Autowired
//    private RedisTemplate redisTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Map<String, Object> login (User user) {
        //根据用户名和密码查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername());
        User loginUser = this.baseMapper.selectOne(wrapper);
        //结果不为空,并且密码和传入密码匹配,则生成token,并且将用户信息存入redis
        if (loginUser != null && passwordEncoder.matches(user.getPassword(),loginUser.getPassword())){
            //暂时用UUID,终极方案是jwt
            //String key = "user" + UUID.randomUUID();

            //存入redis
            loginUser.setPassword(null);
            //redisTemplate.opsForValue().set(key,loginUser,30, TimeUnit.MINUTES);

            //创建jwt
            String token = jwtUtil.createToken(loginUser);
            //返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token",token);
            return data;
        }
        return null;
    }
   /* @Override
    public Map<String, Object> login (User user) {
        //根据用户名和密码查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername());
        wrapper.eq(User::getPassword,user.getPassword());
        User loginUser = this.baseMapper.selectOne(wrapper);
        //结果不为空则生成token,并且将用户信息存入redis
        if (loginUser != null){
            //暂时用UUID,终极方案是jwt
            String key = "user" + UUID.randomUUID();
            //存入redis
            loginUser.setPassword(null);
            redisTemplate.opsForValue().set(key,loginUser,30, TimeUnit.MINUTES);
            //返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token",key);
            return data;
        }
        return null;
    }*/

    @Override
    public Map<String, Object> getUserInfo (String token) {
        //根据token获取用户信息,从redis中获取
        //Object obj = redisTemplate.opsForValue().get(token);
        User loginUser = null;
        try {
            loginUser = jwtUtil.parseToken(token, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (loginUser!=null){
            //User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            HashMap<String, Object> data = new HashMap<>();
            data.put("name",loginUser.getUsername());
            data.put("avatar",loginUser.getAvatar());

            //角色
            List<String> roleList = this.getBaseMapper().getRoleNamesByUserId(loginUser.getId());

            data.put("roles",roleList);
            return data;
        }
        return null;
    }

    @Override
    public void logout (String token) {
        //redisTemplate.delete(token);
    }
}
