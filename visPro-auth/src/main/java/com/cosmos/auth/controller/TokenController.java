package com.cosmos.auth.controller;


import com.cosmos.auth.form.LoginBody;
import com.cosmos.auth.form.RegisterBody;
import com.cosmos.auth.service.SysLoginService;
import com.cosmos.common.service.TokenService;
import com.cosmos.common.constant.SecurityConstants;
import com.cosmos.common.domain.R;
import com.cosmos.common.model.LoginUser;
import com.cosmos.common.utils.SecurityUtils;
import com.cosmos.common.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TokenController {

    @Autowired
    private TokenService tokenService;


    @Autowired
    private SysLoginService sysLoginService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public R<?> login(@RequestBody LoginBody loginBody) {
        log.info("登录请求参数：{}", loginBody);
        LoginUser loginUser = sysLoginService.login(loginBody.getAccount(), loginBody.getPassword());
        return R.success("登录成功", tokenService.createToken(loginUser));
    }


    /**
     * 注册
     */
    @PostMapping("/register")
    public R<?> register(@RequestBody RegisterBody registerBody) {
        sysLoginService.register(registerBody.getAccount(), registerBody.getPassword(), registerBody.getConfirmPassword());
        return R.success("注册成功");
    }

    /**
     * 退出登录
     */
    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            // 删除用户缓存记录
            tokenService.delLoginUser(token);
        }
        return R.success("退出成功");
    }


    @PostMapping("refresh")
    public R<?> refresh(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUser);
            return R.success("刷新成功");
        }
        return R.success("刷新失败");
    }


}
