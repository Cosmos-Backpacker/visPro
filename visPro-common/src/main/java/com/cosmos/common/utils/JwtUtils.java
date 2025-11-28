package com.cosmos.common.utils;


import cn.hutool.core.convert.Convert;
import com.cosmos.common.constant.SecurityConstants;
import com.cosmos.common.constant.TokenConstants;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Jwt工具类
 *
 * @author ruoyi
 */
@Slf4j
public class JwtUtils {
    public static String secret = TokenConstants.SECRET;

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    public static String createToken(Map<String, Object> claims) {
        String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, secret).compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public static Claims parseToken(String token) {

        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 根据令牌获取用户标识
     *
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserKey(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     * 根据令牌获取用户标识
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getUserKey(Claims claims) {
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     * 根据令牌获取用户ID
     *
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserId(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }

    /**
     * 根据身份信息获取用户ID
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getUserId(Claims claims) {
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }

    /**
     * 根据令牌获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public static String getUserName(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.DETAILS_USERNAME);
    }

    /**
     * 根据身份信息获取用户名
     *
     * @param claims 身份信息
     * @return 用户名
     */
    public static String getUserName(Claims claims) {
        return getValue(claims, SecurityConstants.DETAILS_USERNAME);
    }

    /**
     * 根据身份信息获取用户名
     *
     * @param claims 身份信息
     * @return 用户名
     */
    public static String getAccount(Claims claims) {
        return getValue(claims, SecurityConstants.DETAILS_ACCOUNT);
    }


    /**
     * 根据身份信息获取键值
     *
     * @param claims 身份信息
     * @param key    键
     * @return 值
     */
    public static String getValue(Claims claims, String key) {
        return Convert.toStr(claims.get(key), "");
    }


    private static String cleanToken(String token) {
        if (token == null) {
            throw new RuntimeException("Token不能为空");
        }

        // 移除 Bearer 前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 去除所有空白字符
        token = token.trim();

        // 检查 token 格式
        if (!isValidTokenFormat(token)) {
            throw new RuntimeException("Token格式无效");
        }

        return token;
    }

    private static boolean isValidTokenFormat(String token) {
        // JWT token 应该是三段，用点分隔
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            log.error("Token段数不正确: {}", parts.length);
            return false;
        }

        // 检查每段是否只包含合法字符
        for (String part : parts) {
            if (!part.matches("[a-zA-Z0-9-_]+")) {
                log.error("Token包含非法字符: {}", part);
                return false;
            }
        }

        return true;
    }
}
