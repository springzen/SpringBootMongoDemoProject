package com.imwsoftware;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.imwsoftware.mongo.model.Student;
import com.imwsoftware.util.Utils;

/**
 * Class: RandomPortHailataxiiJobTests.java
 *
 * @author: Springzen
 * @since: Jul 17, 2017
 * @version: 1.0
 *
 *           Copyright (c) 2017 Springzen
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = ApplicationConfig.class)
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.imwsoftware" })
public class TestStudentRestEndpoints {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void exampleTest() {
		String body = this.restTemplate.getForObject("/students/list", String.class);

		System.out.println(body);

		assertThat(body != null, is(true));

		try {
			List<Student> students = Utils.convert(body, Student.class);
			students.forEach(System.out::println);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testTextSearch() {
		Map<String, String> urlVariables = new HashMap<>();
		urlVariables.put("text", "cloud,management,985,cat");
		String body = this.restTemplate.getForObject("/students/list?text=cloud,management,985,cat", String.class, urlVariables);

		System.out.println(body);

		assertThat(body != null, is(true));

		try {
			List<Student> students = Utils.convert(body, Student.class);
			students.forEach(System.out::println);
		} catch (IOException e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testPostStudents() {

		try {
			File file = new ClassPathResource("students.json").getFile();
			String s = FileUtils.readFileToString(file);
			List<Student> students = Utils.convert(s, Student.class);
			ResponseEntity<String> body = this.restTemplate.postForEntity("/students/insert", students, String.class);
			
			
			System.out.println(body);

			assertThat(body != null, is(true));
			
		} catch (IOException e) {
			fail(e.getMessage());
		}

	}

}