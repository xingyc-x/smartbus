package com.pcis.smartbus.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/*
 * @author 王琪善
 * @date 2019-7-5
 * @info 开启允许跨域资源共享CORS:Cross-origin resource sharing
 */
@Configuration
public class CORSconfig extends WebMvcConfigurerAdapter {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedMethods("*").allowedOrigins("*").allowedHeaders("*");
	}
}
