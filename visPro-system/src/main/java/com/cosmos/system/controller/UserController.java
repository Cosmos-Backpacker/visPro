package com.cosmos.system.controller;

import com.cosmos.common.domain.R;
import com.cosmos.common.entity.User;
import com.cosmos.common.exception.BusinessException;
import com.cosmos.system.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;


    /**
     * 根据账号获取用户信息
     *
     * @param account 用户账号
     * @return 用户信息
     */
    @PostMapping("/info")
    public R<?> getUserInfoByAccount(@RequestParam String account) {
        // 校验账号是否为空
        if (StringUtils.isBlank(account)) {
            throw new BusinessException("账号不能为空");
        }
        // 调用服务层根据账号获取用户信息
        User user = userService.getLoginUser(account);

        // 调用服务层根据账号获取用户信息
        return R.success("获取用户信息成功", user);
    }


    @PostMapping("/register")
    public R<?> register(String account, String password, String confirmPassword) {
        log.info("register account: {}, password: {}, confirmPassword: {}", account, password, confirmPassword);
        // 校验账号是否为空
        if (StringUtils.isBlank(account)) {
            throw new BusinessException("账号不能为空");
        }

        // 校验密码是否为空
        if (StringUtils.isBlank(password)) {
            throw new BusinessException("密码不能为空");
        }
        // 校验确认密码是否为空
        if (StringUtils.isBlank(confirmPassword)) {
            throw new BusinessException("确认密码不能为空");
        }
        // 校验两次输入密码是否一致
        if (!password.equals(confirmPassword)) {
            throw new BusinessException("两次输入密码不一致");
        }
        // 调用服务层根据账号获取用户信息
        return R.success("获取用户信息成功", userService.registerUser(account, password, confirmPassword));
    }


}
