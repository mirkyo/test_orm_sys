package com.qingge.springboot.controller.dto;

import lombok.Data;

/**
 * 接收前端登录请求的参数
 */

@Data
public class UserDTO {
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String klass;
}
