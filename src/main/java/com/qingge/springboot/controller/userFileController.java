package com.qingge.springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.entity.userFile;
import com.qingge.springboot.mapper.userFileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 文件上传相关接口
 */
@RestController
@RequestMapping("/userfile")
public class userFileController {

    @Value("${file.location}")
    private String fileUploadPath;


    @Resource
    private userFileMapper userfileMapper;


    /**
     * 文件上传接口
     * @param file 前端传来的文件
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file, @RequestParam Map<String,String> map) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        System.out.println(originalFilename + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        // 先存储到磁盘
//        File uploadParentFile = new File(fileUploadPath);
        // 如果文件不存在， 则创建一个新的目录
//        if(!uploadParentFile.exists()){
//            uploadParentFile.mkdirs();
//        }
        String klass = originalFilename.split("-")[0]; // 获取班级信息
        String filename = originalFilename.split("-")[1]; // 获取试卷信息





        // 定义文件唯一的表示码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;

        File uploadFile = new File(fileUploadPath + fileUUID);

        // 判断文件目录是否存在，若不存在则创建一个新的文件目录
        File ParentFile = uploadFile.getParentFile();
        if(!ParentFile.exists()){
            ParentFile.mkdirs();
        }


        String url;
        // 上传文件到磁盘
        file.transferTo(uploadFile);
        // 获取文件的MD5
        String md5 = SecureUtil.md5(uploadFile);
        // 从数据库查询是否存在相同的记录
        userFile dbFiles = getFileMd5(md5);
        if(dbFiles != null){
            url = dbFiles.getUrl();
            // 由于文件已存在，所以删除刚才上传的文件
            uploadFile.delete();
        }else{
            // 数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://localhost:9090/userfile/" + fileUUID;
        }



        // 存储数据库
        Date date = new Date();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(date.getMonth()+1);
        System.out.println(date.getDate());
        System.out.println(date.getHours());
        System.out.println(date.getMinutes());
        String month = String.valueOf(date.getMonth()+1);
        String date1 = String.valueOf(date.getDate());
        String hour = String.valueOf(date.getHours());
        String minutes = String.valueOf(date.getMinutes());
        String endTime = month + "-" + date1 + "-" + hour + "-" + minutes;
        System.out.println(endTime);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(map.get("hour"));
        System.out.println(map.get("hour1"));
        System.out.println(map.get("minutes"));
        System.out.println(map.get("minutes1"));
        System.out.println(map.get("testTime"));
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
        // 计算时间并set值
        String useTime = String.valueOf((Integer.parseInt(hour) - Integer.parseInt(map.get("hour")))*60 + (Integer.parseInt(minutes) - Integer.parseInt(map.get("minutes"))));
        System.out.println(useTime);

        userFile saveFile = new userFile();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        saveFile.setKlass(klass);
        saveFile.setEndTime(endTime);
        saveFile.setUseTime(useTime);
        saveFile.setFilename(filename);
        if(Integer.parseInt(useTime) <= Integer.parseInt(map.get("testTime"))){
            saveFile.setStatus("未超时");
        }else{
            saveFile.setStatus("超时");
        }
        userfileMapper.insert(saveFile);
        return url;
    }


    /**
     * 文件下载接口 http://localhost:9090/userfile/{fileUUID}
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException{
        // 根据文件唯一表示码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    /**
     * 通过文件的md5查询文件
     * @param md5
     * @return
     */
    private userFile getFileMd5(String md5){
        // 查询文件的md5是否存在
        QueryWrapper<userFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<userFile> filesList = userfileMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }


    // 更新
    @PostMapping("/update")
    public Result update(@RequestBody userFile files){

        return Result.success(userfileMapper.updateById(files));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        userFile files = userfileMapper.selectById(id);
        files.setIsDelete(true);
        userfileMapper.updateById(files);
        return Result.success();
    }

    @PostMapping("del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        // select * from sys_file while id in (id, id, id........)
        QueryWrapper<userFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<userFile> files = userfileMapper.selectList(queryWrapper);
        for(userFile file: files){
            file.setIsDelete(true);
            userfileMapper.updateById(file);
        }
        return Result.success();
    }


    /**
     * 分页查询接口
     * @param pageNum
     * @param pageSize
     * @param klass
     * @param filename
     * @return
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String klass,
                           @RequestParam(defaultValue = "") String filename){


        QueryWrapper<userFile> queryWrapper = new QueryWrapper<>();
        // 查询未删除的记录
        queryWrapper.eq("is_delete", false);
        queryWrapper.orderByDesc("id");
        if(!"".equals(klass)){
            queryWrapper.like("klass", klass);
        }
        if(!"".equals(filename)){
            queryWrapper.like("filename", filename);
        }
        return Result.success(userfileMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/test")
    public Result iiu(){
        return Result.success(userfileMapper.aaa());
    }


}
