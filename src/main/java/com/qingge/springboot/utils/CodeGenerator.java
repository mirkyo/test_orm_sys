package com.qingge.springboot.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;


import java.util.Collections;

/**
 * mp代码生成器
 * by ska
 * @since 2022-11-3
 */


public class CodeGenerator {

    public static void main(String[] args){
        generate();
    }
    
    private static void generate(){
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/qing?serverTimezone=GMT%2b8", "root", "root")
                .globalConfig(builder -> {
                    builder.author("青哥哥") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("C:\\Users\\探险家\\Desktop\\项目demo\\springboot\\src\\main\\java\\"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.qingge.springboot") // 设置父包名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "C:\\Users\\探险家\\Desktop\\项目demo\\springboot\\src\\main\\resources\\mapper\\")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok();
                    builder.controllerBuilder().enableHyphenStyle()
                                    .enableRestStyle();
                    builder.addInclude("sys_user") // 设置需要生成的表名
                            .addTablePrefix("t_", "sys_"); // 设置过滤表前缀
                })
                // .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
    
}
