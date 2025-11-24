package com.cosmos.auth.controller;


import com.cosmos.auth.form.LoginBody;
import com.cosmos.auth.form.RegisterBody;
import com.cosmos.auth.service.SysLoginService;
import com.cosmos.auth.service.TokenService;
import com.cosmos.common.domain.R;
import com.cosmos.common.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


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
        LoginUser loginUser = sysLoginService.login(loginBody.getAccount(), loginBody.getPassword());
        return R.success("登录成功", loginUser);
    }


    /**
     * 注册
     */
    @PostMapping("/register")
    public R<?> register(@RequestBody RegisterBody registerBody) {
        sysLoginService.register(registerBody.getAccount(), registerBody.getPassword(), registerBody.getConfirmPassword());
        return R.success("注册成功");
    }





}
