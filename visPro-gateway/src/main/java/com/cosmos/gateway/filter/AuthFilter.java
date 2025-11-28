package com.cosmos.gateway.filter;

import com.cosmos.common.exception.BusinessException;
import com.cosmos.common.service.RedisService;
import com.cosmos.common.utils.JwtUtils;
import com.cosmos.common.utils.ServletUtils;
import com.cosmos.common.utils.StringUtils;
import com.cosmos.gateway.config.properties.IgnoreWhiteProperties;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关鉴权
 *
 * @author ruoyi
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    // 排除过滤的 uri 地址，nacos自行添加
    @Autowired
    private IgnoreWhiteProperties ignoreWhite;

    @Autowired
    private RedisService redisService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        String url = request.getURI().getPath();

        // 添加调试日志
        log.info("=== 网关过滤器开始 ===");
        log.info("请求路径: {}", url);
        log.info("白名单列表: {}", ignoreWhite.getWhites());
        log.info("是否匹配白名单: {}", StringUtils.matches(url, ignoreWhite.getWhites()));

        // 跳过不需要验证的路径
        if (StringUtils.matches(url, ignoreWhite.getWhites())) {
            return chain.filter(exchange);
        }
        String token = getToken(request);
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException("令牌不能为空");
        }
        Claims claims = JwtUtils.parseToken(token);
        if (claims == null) {
            throw new BusinessException("令牌已过期或验证不正确！");
        }

        String userkey = JwtUtils.getUserKey(claims);
        boolean islogin = redisService.hasKey(getTokenKey(userkey));
        if (!islogin) {
            return unauthorizedResponse(exchange, "登录状态已过期");
        }

        String userid = JwtUtils.getUserId(claims);
        String account = JwtUtils.getAccount(claims);
        if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(account)) {
            throw new BusinessException("令牌验证失败");

        }

        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }


    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
        log.error("[鉴权异常处理]请求路径:{},错误信息:{}", exchange.getRequest().getPath(), msg);
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED);
    }


    /**
     * 获取缓存key
     */
    private String getTokenKey(String token) {
        return "login_tokens:" + token;
    }

    /**
     * 获取请求token
     */
    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst("Authorization");
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith("Bearer ")) {
            token = token.replaceFirst("Bearer ", StringUtils.EMPTY);
        }
        return token;
    }

    @Override
    public int getOrder() {
        return -200;
    }
}