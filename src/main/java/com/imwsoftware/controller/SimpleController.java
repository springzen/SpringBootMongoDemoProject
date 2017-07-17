package com.imwsoftware.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class: SimpleController.java
 *
 * @author: Springzen
 * @since: Jul 15, 2017
 * @version: 1.0
 *
 *           Copyright (c) 2017 Springzen
 *
 */
@RestController
public class SimpleController {
	
	@Value("${my.property}")
	private String myProperty;

	@RequestMapping("/")
	public String index() {
		return "Started application with property: " + myProperty;
	}

}
