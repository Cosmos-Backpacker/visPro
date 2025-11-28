package com.cosmos.common.service;


import com.cosmos.common.constant.CacheConstants;
import com.cosmos.common.constant.SecurityConstants;
import com.cosmos.common.constant.TokenConstants;
import com.cosmos.common.model.LoginUser;
import com.cosmos.common.utils.IdUtils;
import com.cosmos.common.utils.JwtUtils;
import com.cosmos.common.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class TokenService {


    @Autowired
    private RedisService redisService;


    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long TOKEN_EXPIRE_TIME = CacheConstants.EXPIRATION;

    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY;

    private final static Long TOKEN_REFRESH_THRESHOLD_MINUTES = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;

    public Map<String, Object> createToken(LoginUser loginUser) {
        String userKey = IdUtils.fastUUID();
        Long userId = loginUser.getId();
        String userName = loginUser.getUsername();
        loginUser.setToken(userKey);
        loginUser.setId(userId);
        loginUser.setUsername(userName);
        loginUser.setGender(loginUser.getGender());
        loginUser.setAvatarUrl(loginUser.getAvatarUrl());
        loginUser.setEmail(loginUser.getEmail());
        loginUser.setPhone(loginUser.getPhone());

        refreshToken(loginUser);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, userKey);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
        claimsMap.put(SecurityConstants.DETAILS_ACCOUNT, loginUser.getUserAccount());

        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<String, Object>();
        rspMap.put("access_token", JwtUtils.createToken(claimsMap));
        rspMap.put("expires_in", TOKEN_EXPIRE_TIME);
        rspMap.put("id", userId);
        return rspMap;
    }


    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + TOKEN_EXPIRE_TIME * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisService.setCacheObject(userKey, loginUser, TOKEN_EXPIRE_TIME, TimeUnit.DAYS);
    }


    private String getTokenKey(String token) {
        return ACCESS_TOKEN + token;
    }

    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userkey = JwtUtils.getUserKey(token);

            log.info("删除用户缓存key: {}", userkey);
            String tokenKey = getTokenKey(userkey);
            log.info("删除用户缓存key: {}", tokenKey);
            redisService.deleteObject(tokenKey);
        }
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌

        String token = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return getLoginUser(token);
    }




    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(String token) {
        LoginUser user = null;
        try {
            if (StringUtils.isNotEmpty(token)) {
                String userkey = JwtUtils.getUserKey(token);//解析token并获取用户key
                user = redisService.getCacheObject(getTokenKey(userkey));
                return user;
            }
        } catch (Exception e) {
            log.error("获取用户信息异常'{}'", e.getMessage());
        }
        return user;
    }


    /**
     * 验证令牌有效期，相差不足120分钟，自动刷新缓存
     *
     * @param loginUser
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= TOKEN_REFRESH_THRESHOLD_MINUTES) {
            refreshToken(loginUser);
        }
    }


}
