package com.snowtraces.itwhy.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snowtraces.itwhy.entity.User;
import com.snowtraces.itwhy.repository.UserMapper;
import com.snowtraces.itwhy.service.UserService;
import com.snowtraces.itwhy.util.SetData;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User addBySrc(String srcUserId, String srcUserName, String srcPlat) {
        // 1. 判断用户是否存在
        User exist = new LambdaQueryChainWrapper<>(baseMapper)
                .eq(User::getSrcId, srcUserId)
                .eq(User::getSrcPlat, srcPlat)
                .one();
        if (exist != null) {
            return exist;
        }

        // 2. 新增用户
        User user = new User();
        user.setSrcId(srcUserId);
        user.setUserName(srcUserName);
        user.setSrcPlat(srcPlat);
        super.save(user);
        return user;
    }

    @Override
    public <T> T forName(T t) {
        if (t == null) {
            return null;
        }

        Class<?> clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields).forEach(f -> {
            SetData setData = f.getAnnotation(SetData.class);
            if (setData != null) {
                try {
                    String value = setData.value();
                    Field from = clazz.getDeclaredField(value);
                    from.setAccessible(true);
                    Object fromValue = from.get(t);
                    if (fromValue != null) {
                        Long id = (Long) fromValue;
                        User user = new LambdaQueryChainWrapper<>(baseMapper)
                                .select(User::getUserName)
                                .eq(User::getUserId, id)
                                .one();
                        if (user != null) {
                            f.setAccessible(true);
                            f.set(t, user.getUserName());
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return t;
    }
}
