package com.imwsoftware.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Class: Utils.java
 *
 * @author: Springzen
 * @since: Jul 10, 2017
 * @version: 1.0
 *
 *           Copyright (c) 2017 Springzen
 *
 */
public class Utils {

	private static Logger logger = LoggerFactory.getLogger(Utils.class);

	public Utils() {
	}

	public static String toJson(Object value) {
		ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(Include.NON_EMPTY).configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		StringBuilder sb = null;
		try {
			sb = new StringBuilder();
			sb.append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value));
		} catch (JsonProcessingException e) {
			logger.debug("failed to serialize object to json string", e);
		} catch (Exception e) {
			logger.debug("failed to serialize object to json string", e);
		} finally {
			if (sb == null) {
				sb = new StringBuilder();
				sb.append(new ObjectMapper().createObjectNode().toString());
			}
		}

		return sb.toString();

	}

	public static <T> T fromJson(String jsonString, Class<T> classOfT) {
		ObjectMapper mapper = new ObjectMapper();
		T value = null;
		try {
			value = mapper.readValue(jsonString, classOfT);
		} catch (Exception e) {
			value = null;
		}

		return value;
	}

	public static <T> List<T> convert(String jsonString, Class<T> classOfT) throws JsonParseException, JsonMappingException,
			IOException {

		ObjectMapper objectMapper = new ObjectMapper();

		List<T> navigation = objectMapper.readValue(
				jsonString,
				objectMapper.getTypeFactory().constructCollectionType(
						List.class, classOfT));

		List<T> opt = Optional.ofNullable(navigation).orElse(new ArrayList<>(0));
		logger.info(String.format("Size of things %d", opt.size()));
		try {
			assertThat(opt.size() > 0, is(true));
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}

		return opt;
	}

}
