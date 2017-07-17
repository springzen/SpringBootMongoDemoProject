package com.imwsoftware.mongo;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.imwsoftware.mongo.model.Student;

/**
 * Class: StudentRepository.java
 *
 * @author: Springzen
 * @since: Jul 10, 2017
 * @version: 1.0
 *
 *           Copyright (c) 2017 Springzen
 *
 */
@Repository
public interface StudentRepository extends PagingAndSortingRepository<Student, String>, StudentRepositoryCustom {
	Student findByName(String name);

	/**
	 * @ref: http://docs.spring.io/spring-data/data-mongo/docs/1.10.4.RELEASE/reference/html/#mongodb.repositories.queries.delete
	 * @param name
	 * @return
	 */
	Long deleteByName(String name);

	/**
	 * @ref: http://docs.spring.io/spring-data/data-mongo/docs/1.10.4.RELEASE/reference/html/#repositories.limit-query-result
	 * @param name
	 * @param sort
	 * @return
	 */
	List<Student> findByName(String name, Sort sort);

	/**
	 * @ref: http://docs.spring.io/spring-data/data-mongo/docs/1.10.4.RELEASE/reference/html/#repositories.query-streaming
	 * @return
	 */
	Stream<Student> readAllByNameNotNull();

	/**
	 * @ref: https://docs.spring.io/spring-data/data-mongo/docs/current/reference/html/#mongodb.repositories.queries.json-based
	 * @param name
	 * @return
	 */
	@Query(value = "{ 'name' : ?0 }", fields = "{ 'name' : 1, 'email' : 1}")
	List<Student> findByTheStudentName(String name);

}
