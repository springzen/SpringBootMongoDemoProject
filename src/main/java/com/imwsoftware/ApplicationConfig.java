package com.imwsoftware;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

/**
 * Class: ApplicationConfig.java
 *
 * @author: Springzen
 * @since: Jul 10, 2017
 * @version: 1.0
 *
 *           Copyright (c) 2017 Springzen
 *
 */
@Configuration
@EnableMongoRepositories
class ApplicationConfig extends AbstractMongoConfiguration {

	@Override
	protected String getDatabaseName() {
		return "imw";
	}

	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient();
	}

	@Override
	protected String getMappingBasePackage() {
		return "com.imwsoftware.mongo.model";
	}
}