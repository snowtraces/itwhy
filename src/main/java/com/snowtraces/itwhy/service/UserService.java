package com.snowtraces.itwhy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snowtraces.itwhy.entity.User;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author snowtraces
 * @since 2021-09-26
 */
public interface UserService extends IService<User> {

    /**
     * 通过源数据添加用户
     *
     * @param srcUserId
     * @param srcUserName
     * @param srcPlat
     * @return
     */
    User addBySrc(String srcUserId, String srcUserName, String srcPlat);

    /**
     * 填充用户名
     *
     * @param obj
     */
    void forName(Object obj);
}
