package com.imwsoftware.mongo;

import java.util.List;

import com.imwsoftware.mongo.model.Student;

public interface StudentRepositoryCustom {

	public static final String COLLECTION_STUDENTS = "students";

	void insertStudent(Student student);

	void insertStudents(List<Student> students);

	<T> int bulkInsert(List<T> records, String collectionName);
}
