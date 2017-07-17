package com.imwsoftware.properties;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Class: Springzen
 *
 * @author: Willhelm Lehman
 * @since: Jul 15, 2017
 * @version: 1.0
 *
 *           Copyright (c) 2017 Springzen
 *
 */
@Component
@ConfigurationProperties(prefix = "simple")
@Validated
public class SimpleProperties {
	
	@NotNull
	private String name;
	private String description;
	private boolean enabled;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
