package com.imwsoftware.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.imwsoftware.mongo.StudentRepository;
import com.imwsoftware.mongo.model.Student;
import com.imwsoftware.util.Utils;

/**
 * Class: StudentController.java
 *
 * @author: Springzen
 * @since: Jul 16, 2017
 * @version: 1.0
 *
 *           Copyright (c) 2017 Springzen
 *
 */
@RestController()
@RequestMapping("/students")
public class StudentController {

	@Autowired
	private StudentRepository studentRepository;

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<String> insertStudents(@RequestBody List<Student> students) {

		ResponseEntity<String> responseEntity = new ResponseEntity<>(Utils.toJson(students), HttpStatus.ACCEPTED);

		if (CollectionUtils.isNotEmpty(students)) {
			studentRepository.bulkInsert(students);
		} else {
			responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return responseEntity;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Student>> listStudents(@RequestParam(required = false, value = "text") String text) {
		
		Iterable<Student> all;
		
		if (StringUtils.isNotBlank(text)) {
			all = studentRepository.textSearch(text.split(","));
		} else {
			all = studentRepository.findAll();
		}

		return new ResponseEntity<>(Optional.ofNullable(all).orElse(new ArrayList<>(0)), HttpStatus.OK);
	}
}
