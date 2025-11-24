package com.cosmos.api.factory;

import com.cosmos.api.RemoteUserService;
import com.cosmos.common.domain.R;
import com.cosmos.common.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务降级处理
 *
 * @author cosmosBackpacker
 */
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteUserFallbackFactory.class);

    @Override
    public RemoteUserService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteUserService() {

            @Override
            public R<User> getUserInfo(String account) {
                return R.error("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<?> register(String account, String password, String confirmPassword) {
                return R.error("注册失败");
            }
        };
    }
}
