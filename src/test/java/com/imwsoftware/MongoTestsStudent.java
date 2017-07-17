package com.imwsoftware;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.imwsoftware.mongo.StudentRepository;
import com.imwsoftware.mongo.model.Student;
import com.imwsoftware.util.Utils;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class MongoTestsStudent {

	@Autowired
	StudentRepository repository;

	public MongoTestsStudent() {
	}

	@Test
	public void insertStudent() {

		try {
			File file = new ClassPathResource("singlestudent.json").getFile();
			String s = FileUtils.readFileToString(file);
			System.out.println(s);
			Student student = Utils.fromJson(s, Student.class);

			if (repository.findByName(student.getName()) == null) {
				repository.insertStudent(student);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void insertStudents() {

		try {
			File file = new ClassPathResource("students.json").getFile();
			String s = FileUtils.readFileToString(file);
			System.out.println(s);
			List<Student> students = Utils.convert(s, Student.class);

			repository.insertStudents(students);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void insertStudentsBulk() {

		try {
			File file = new ClassPathResource("students.json").getFile();
			String s = FileUtils.readFileToString(file);
			System.out.println(s);
			List<Student> students = Utils.convert(s, Student.class);

			int bulkInsertSize = repository.bulkInsert(students, "students");

			System.out.println(String.format("Size of insert: %d", bulkInsertSize));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void insertMoreStudentsBulk() {

		try {
			File file = new ClassPathResource("morestudents.json").getFile();
			String s = FileUtils.readFileToString(file);
			System.out.println(s);
			List<Student> students = Utils.convert(s, Student.class);

			int bulkInsertSize = repository.bulkInsert(students, "students");

			System.out.println(String.format("Size of insert: %d", bulkInsertSize));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deleteStudent() {
		Long result = repository.deleteByName("Purushothaman");
		System.out.println(String.format("Deleted %d students", result));
	}

	@Test
	public void findAllSortedDesc() {
		Iterable<Student> all = repository.findAll(new Sort(new Order(Direction.DESC, "name")));
		assertThat(((Collection<?>) all).size() > 0, is(true));
	}

	@Test
	public void findAllSortedAsc() {
		Iterable<Student> all = repository.findAll(new Sort(new Order(Direction.ASC, "name")));
		assertThat(((Collection<?>) all).size() > 0, is(true));
	}

	@Test
	public void countStudents() {
		long count = repository.count();
		assertThat(count > 0, is(true));
	}

	@Test
	public void readAllViaStream() {
		Stream<Student> stream = repository.readAllByNameNotNull();
		assertNotNull(stream);
		stream.forEach(student -> {
			System.out.println("Student name ->> " + student.getName());
		});
	}

	@Test
	public void findByTheStudentName() {
		List<Student> student = repository.findByTheStudentName("Praveen Kumar");
		assertNotNull(student);
		System.out.println(Utils.toJson(student));
	}

}
