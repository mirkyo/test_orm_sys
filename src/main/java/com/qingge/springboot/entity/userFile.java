package com.qingge.springboot.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_userfile")
public class userFile {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String type;
    private Long size;
    private String url;
    private String klass;
    private Boolean isDelete;
    private Boolean enable;
    private String md5;
    private String endTime;
    private String useTime;
    private String status;

}