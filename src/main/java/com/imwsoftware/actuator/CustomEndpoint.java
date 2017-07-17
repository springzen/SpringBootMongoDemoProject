package com.imwsoftware.actuator;

import org.springframework.boot.actuate.endpoint.AbstractEndpoint;

/**
 * Class: CustomEndpoint.java
 *
 * @author: Springzen
 * @since: Jul 16, 2017
 * @version: 1.0
 *
 *           Copyright (c) 2017 Springzen
 *
 */
public class CustomEndpoint extends AbstractEndpoint<String> {

	public CustomEndpoint(String id) {
		super(id);
	}

	public CustomEndpoint(String id, boolean sensitive) {
		super(id, sensitive);
	}

	public CustomEndpoint(String id, boolean sensitive, boolean enabled) {
		super(id, sensitive, enabled);
	}

	@Override
	public String invoke() {
		return "CustomEndpoint invoked!";
	}

}
