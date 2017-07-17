package com.imwsoftware.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Class: FooFooProperties.java
 *
 * @author: Willhelm Lehman
 * @since: Jul 15, 2017
 * @version: 1.0
 *
 *           Copyright (c) 2017 IMWSoftware LLC
 *
 */
/**
 * Class: FooFooProperties.java
 * 
 * no prefix, find root level values
 *
 * @author: Springzen
 * @since: Jul 15, 2017
 * @version: 1.0
 *
 *           Copyright (c) 2017 Springzen
 * 
 * @Ref: https://www.mkyong.com/spring-boot/spring-boot-configurationproperties-example/
 *
 */
@Component
@ConfigurationProperties
public class FooFooProperties {

	private String fooFoo;

	public String getFooFoo() {
		return fooFoo;
	}

	public void setFooFoo(String fooFoo) {
		this.fooFoo = fooFoo;
	}

}
