package com.qingge.springboot.config.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.qingge.springboot.common.Constants;
import com.qingge.springboot.entity.User;
import com.qingge.springboot.exception.ServiceException;
import com.qingge.springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class  JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String token = request.getHeader("token");
        // 如果不是映射方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return false;
        }
        // 执行认证
        if(StrUtil.isBlank(token)){
            throw new ServiceException(Constants.CODE_401, "无token, 请重新登录");
        }
        // 获取token 中的 user id
        String userId;
        try{
            userId = JWT.decode(token).getAudience().get(0);
        }catch(JWTCreationException j){
            throw new ServiceException(Constants.CODE_401, "token验证成功");
        }
        // 根据token中的userId查询数据库
        User user = userService.getById(userId);
        if(user == null){
            throw new ServiceException(Constants.CODE_401, "用户不存在，请重新输入");
        }
        // 验证token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try{
            jwtVerifier.verify(token);
        }catch(JWTCreationException j){
            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新输入");
        }
        return true;

    }
}
