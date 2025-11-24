package com.cosmos.api;


import com.cosmos.api.factory.RemoteUserFallbackFactory;
import com.cosmos.common.constant.ServiceNameConstants;
import com.cosmos.common.domain.R;
import com.cosmos.common.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {

    /**
     * 通过账号查询用户信息
     *
     * @return 结果
     */
    @PostMapping("/user/info")
    R<User> getUserInfo(@RequestParam("account") String account);


    /**
     * 注册用户
     *
     * @param account         账号
     * @param password        密码
     * @param confirmPassword 确认密码
     * @return 结果
     */
    @PostMapping("/user/register")
    R<?> register(@RequestParam("account") String account,
                  @RequestParam("password") String password,
                  @RequestParam("confirmPassword") String confirmPassword);

}
