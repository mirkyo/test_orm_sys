package com.qingge.springboot.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_file")
public class Files{

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
    private String startTime;
    private String testTime;
    private Integer nnt;
    private String subject;
    private String lecturer;
    private String invigilator;
    private String mrn;
    private String examEnv;
    private String student;
    private String bukao_student;

}
