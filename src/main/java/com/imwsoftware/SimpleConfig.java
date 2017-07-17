package com.imwsoftware;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.imwsoftware.properties.SimpleProperties;

/**
 * Class: SimpleConfig.java
 *
 * @author: Springzen
 * @since: Jul 15, 2017
 * @version: 1.0
 *
 *           Copyright (c) 2017 Springzen
 *
 */
@Configuration
public class SimpleConfig {

	@Value("${my.property}")
	private String myProperty;
	
	@Autowired
	private SimpleProperties simpleProperties;

	@PostConstruct
	private void postConstruct() {
		System.out.println(myProperty);
		System.out.println(simpleProperties.getDescription());
		System.out.println(simpleProperties.isEnabled());
	}
}
