package com.pcis.smartbus.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/*
 * @author 王琪善
 * @data 2019-7-9
 *@info 静态资源映射，将对图片的访问映射到文件系统绝对路径 
 */
@Configuration
public class StaticResourceConfig extends WebMvcConfigurerAdapter {
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/pics/**").addResourceLocations("file:C:/pic/");
		super.addResourceHandlers(registry);
	}
	
}
