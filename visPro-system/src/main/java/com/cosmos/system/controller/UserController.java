package com.cosmos.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.cosmos.common.domain.R;
import com.cosmos.common.entity.User;
import com.cosmos.common.exception.BusinessException;
import com.cosmos.common.model.LoginUser;
import com.cosmos.common.service.TokenService;
import com.cosmos.common.utils.SecurityUtils;
import com.cosmos.system.service.impl.UserServiceImpl;
import com.cosmos.system.utils.MailUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private TokenService tokenService;


    @Autowired
    private MailUtil mailUtil;


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


    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    public R<?> getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            throw new BusinessException("用户不存在");
        }

        return R.success("获取用户信息成功", loginUser);
    }


    /**
     * 更新用户信息
     *
     * @return 用户信息
     */
    @PostMapping("/updateUserInfo")
    public R<?> updateUserInfo(@RequestBody User user, HttpServletRequest request) {

        if (user.getId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        // 调用服务层根据账号获取用户信息

        LoginUser loginUser = new LoginUser();

        boolean isSuccess = userService.updateUserInfo(user);
        if (!isSuccess) {
            throw new BusinessException("更新用户信息失败");
        } else {
            //获取最新的数据
            User newUser = userService.getById(user.getId());
            //更新redis数据库
            loginUser = BeanUtil.copyProperties(newUser, LoginUser.class);


            String token = SecurityUtils.getToken(request);

            loginUser.setToken(SecurityUtils.getUserKey(token));
            //获取原来的userKey


            // 更新redis数据库
            tokenService.refreshToken(loginUser);
        }


        // 调用服务层根据账号获取用户信息
        return R.success("获取用户信息成功", loginUser);
    }


    @PostMapping("/feedBack")
    public R<?> feedBack(String content) throws MessagingException {

        long userId = SecurityUtils.getLoginUser().getId();

        mailUtil.sendTextMailMessage("3317194303@qq.com", "来自用户" + userId + "反馈信息", content);

        return R.success("反馈成功");

    }


}
