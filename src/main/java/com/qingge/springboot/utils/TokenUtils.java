package com.qingge.springboot.utils;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.qingge.springboot.entity.User;
import com.qingge.springboot.service.IUserService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
@Component
public class TokenUtils {


    private static IUserService staticUserService;

    @Resource
    private IUserService userService;

    @PostMapping
    public void setUserService(){
        staticUserService = userService;
    }
    /**
     * 生成Token
     * @return
     */
    public static String genToken(String userId, String sign){
        return JWT.create().withAudience(userId)
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) // 两小时后token过期
                .sign(Algorithm.HMAC256(sign));
//        JWT.create().withAudience(userId)
//            .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) // 两小时后token过期
//            .sign(Algorithm.HMAC256(sign)); // 以password作为token的密钥
    }

    public static User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserService.getById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
