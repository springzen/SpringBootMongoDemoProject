package com.imwsoftware;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.imwsoftware.actuator.CustomEndpoint;

/**
 * Class: CustomEndpointsConfig.java
 *
 * @author: Springzen
 * @since: Jul 16, 2017
 * @version: 1.0
 *
 *           Copyright (c) 2017 Springzen
 *
 */
@Configuration
public class CustomEndpointsConfig {

	@Bean
	public CustomEndpoint customEndpoint() {
		return new CustomEndpoint("custom", false);
	}
}
