package com.cosmos.auth.service;


import cn.hutool.core.bean.BeanUtil;
import com.cosmos.api.RemoteUserService;
import com.cosmos.common.constant.UserConstants;
import com.cosmos.common.domain.R;
import com.cosmos.common.entity.User;
import com.cosmos.common.exception.BusinessException;
import com.cosmos.common.model.LoginUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import static com.cosmos.common.constant.UserConstants.SALT;

@Component
public class SysLoginService {

    @Autowired
    private RemoteUserService remoteUserService;


    public LoginUser login(String account, String password) {
        if (StringUtils.isAnyBlank(account, password)) {
            throw new BusinessException("用户/密码必须填写");
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {

            throw new BusinessException("用户密码不在指定范围");
        }
        // 用户名不在指定范围内 错误
        if (account.length() < UserConstants.USERNAME_MIN_LENGTH
                || account.length() > UserConstants.USERNAME_MAX_LENGTH) {
            throw new BusinessException("账号不在指定范围");
        }
        //远程调用获取用户信息
        R<User> userR = remoteUserService.getUserInfo(account);
        if (userR.getCode() != 200) {
            throw new BusinessException(userR.getMsg());
        }
        User user = userR.getData();
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        //对密码用Md5加密
        //密码错误
        if (!encryptPassword.equals(user.getUserPassword())) {
            throw new BusinessException("密码错误");
        }

        return BeanUtil.copyProperties(user, LoginUser.class);
    }

    public void register(String account, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new BusinessException("两次输入密码不一致");
        }

        //远程调用处理
        R<?> registerR = remoteUserService.register(account, password, confirmPassword);
        if (registerR.getCode() != 200) {
            throw new BusinessException(registerR.getMsg());
        }

        boolean resultData = (boolean) registerR.getData();
        if (!resultData) {
            throw new BusinessException("注册失败");
        }

        //注册成功
        System.out.println("注册成功");

    }

}
