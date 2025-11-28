package com.cosmos.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cosmos.common.entity.User;
import com.cosmos.common.model.LoginUser;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author cosmosBackpacker
 * @since 2025-11-18
 */
@Service
public interface IUserService extends IService<User> {

    User getLoginUser(String account);

    boolean registerUser(String account, String password, String confirmPassword);

    boolean updateUserInfo(User user);

}
