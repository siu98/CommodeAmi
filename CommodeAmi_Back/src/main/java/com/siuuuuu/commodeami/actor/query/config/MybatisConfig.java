package com.siuuuuu.commodeami.actor.query.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@MapperScan(basePackages = "com.siuuuuu.commodeami.actor.query.repository", annotationClass = Mapper.class)
@Configuration("ActorMybatisConfiguration")
public class MybatisConfig {
}
