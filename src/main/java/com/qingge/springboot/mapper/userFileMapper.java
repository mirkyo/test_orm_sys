package com.qingge.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingge.springboot.entity.Files;
import com.qingge.springboot.entity.User;
import com.qingge.springboot.entity.userFile;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface userFileMapper extends BaseMapper<userFile> {

    @Select("select * from sys_userfile")
    List<userFile> aaa();
}
