package com.cosmos.auth.form;

import lombok.Data;

/**
 * 用户登录对象
 *
 * @author ruoyi
 */
@Data
public class LoginBody {
    /**
     * 账号
     */
    private String account;

    /**
     * 用户密码
     */
    private String password;

}
