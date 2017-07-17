package com.imwsoftware.mongo;

import java.util.List;

import com.imwsoftware.mongo.model.Student;

/**
 * Class: StudentRepositoryCustom.java
 *
 * @author: Springzen
 * @since: Jul 10, 2017
 * @version: 1.0
 *
 *           Copyright (c) 2017 Springzen
 *
 */
public interface StudentRepositoryCustom {

	public static final String COLLECTION_STUDENTS = "students";

	void insertStudent(Student student);

	void insertStudents(List<Student> students);

	<T> int bulkInsert(List<T> records, String collectionName);

	<T> int bulkInsert(List<T> records);

	List<Student> textSearch(String... strings);
}
