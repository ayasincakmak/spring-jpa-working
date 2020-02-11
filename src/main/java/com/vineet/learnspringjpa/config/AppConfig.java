package com.vineet.learnspringjpa.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(
		basePackages = {"com.vineet.learnspringjpa.controller","com.vineet.learnspringjpa.service"}
		)
@PropertySource(value = { "classpath:/config/${environment:qa}/db.properties" })
@Import(value = { JpaConfig.class })
public class AppConfig {

}
