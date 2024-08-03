package com.example.app5.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.text.SimpleDateFormat;


@Configuration
@EnableWebMvc
@ComponentScan
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.freeMarker();

	}

	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer() {
		var configures = new FreeMarkerConfigurer();
		configures.setTemplateLoaderPath("classpath:/templates/freemarker");
		configures.setDefaultEncoding("UTF-8");

		return configures;
	}

	@Bean
	public SimpleDateFormat simpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	@Bean
	public FreeMarkerViewResolver freeMarkerViewResolver() {
		var fvr = new FreeMarkerViewResolver();
		fvr.setCache(true);
		fvr.setPrefix("");
		fvr.setSuffix(".html");
		fvr.setRequestContextAttribute("rc");
		return fvr;
	}
}