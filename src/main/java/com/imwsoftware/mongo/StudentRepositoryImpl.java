package com.imwsoftware.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.BulkOperationException;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.core.query.Update;

import com.imwsoftware.mongo.model.Student;
import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;

/**
 * Class: StudentRepositoryImpl.java
 *
 * @author: Springzen
 * @since: Jul 10, 2017
 * @version: 1.0
 *
 *           Copyright (c) 2017 Springzen
 *
 */
public class StudentRepositoryImpl implements StudentRepositoryCustom {

	private static Logger logger = LoggerFactory.getLogger(StudentRepositoryImpl.class);

	@Autowired
	MongoTemplate mongoTemplate;

	public StudentRepositoryImpl() {
	}

	/**
	 * <code>
	 db.students.insert({
		"name": "Bill Johnson",
		"degree": "Cloud Computing",
		"email": "bill@charity.com",
		"subjects": [
		{
		  "name": "Microservices",
		  "prof": "Prof. Mike Crows"
		},
		{
		  "name": "Cloud Computing",
		  "prof": "Prof. Tech Ninja"
		},
		{
		  "name": "Web Development",
		  "prof": "Prof. Chunky Monkey"
		}
		],
		"phone": ["4152253357", "4155553232", "4154561234"]
	});
	 * </code>
	 */
	@Override
	public void insertStudent(Student student) {
		mongoTemplate.insert(student);
	}

	@Override
	public void insertStudents(List<Student> students) {
		mongoTemplate.insert(students, COLLECTION_STUDENTS);
	}

	@Override
	public <T> int bulkInsert(List<T> records) {
		return bulkInsert(records, COLLECTION_STUDENTS);
	}

	@Override
	public <T> int bulkInsert(List<T> records, String collectionName) {
		int size = Optional.ofNullable(records).orElse(new ArrayList<>(0)).size();

		if (CollectionUtils.isNotEmpty(records)) {
			try {
				BulkOperations bulkOps = mongoTemplate.bulkOps(BulkMode.UNORDERED, StringUtils.isNotBlank(collectionName) ? collectionName : COLLECTION_STUDENTS);
				BulkOperations insert = bulkOps.insert(records);
				BulkWriteResult execute = insert.execute();
				size = execute.getInsertedCount();
			} catch (BulkOperationException e) {
				logger.debug("Received: " + size);
				if (e.getResult() != null) {
					size = e.getResult().getInsertedCount();
					logger.debug("Inserted: " + size);
				}
			} catch (Exception e) {
				logger.warn("Exception: Failed bulk insert.", e);
			}
		} else {
			size = 0;
		}

		return size;
	}

	/**
	 * <code>
		db.students.update({
			"name": "Some name"
		}, {
	  		$set: {
	    		"email" : "new@email.net"
	  		}
		});
	 * </code>
	 * 
	 * @param name
	 * @param email
	 * @return
	 */
	public int updateEmailForStudent(String name, String email) {
		Update update = new Update();
		update.addToSet("email", email);

		Query query = new Query(Criteria.where("name").is(name));

		WriteResult result = mongoTemplate.updateFirst(query, update, COLLECTION_STUDENTS);
		return result.getN();
	}

	public int updatePointsForStudent(String name, int points) {
		Update update = new Update();
		update.addToSet("points", points);

		Query query = new Query(Criteria.where("name").is(name));

		WriteResult result = mongoTemplate.updateFirst(query, update, COLLECTION_STUDENTS);
		return result.getN();
	}

	public int incrementPointsForStudent(String name, int points) {
		Update update = new Update();
		update.inc("points", points);

		Query query = new Query(Criteria.where("name").is(name));

		WriteResult result = mongoTemplate.updateFirst(query, update, COLLECTION_STUDENTS);
		return result.getN();
	}

	public int unsetPointsForStudent(String name) {
		Update update = new Update();
		update.unset("points");

		Query query = new Query(Criteria.where("name").is(name));

		WriteResult result = mongoTemplate.updateFirst(query, update, COLLECTION_STUDENTS);

		return result.getN();
	}

	public void upsertStudent(String name, Student student) {
		mongoTemplate.save(student, COLLECTION_STUDENTS);
	}

	public int renameFieldForStudent(String name, String oldName, String newName) {
		Update update = new Update();
		update.rename(oldName, newName);

		Query query = new Query(Criteria.where("name").is(name));

		WriteResult result = mongoTemplate.updateFirst(query, update, COLLECTION_STUDENTS);

		return result.getN();
	}

	public int removeStudentByName(String name) {

		Query query = new Query(Criteria.where("name").is(name));

		WriteResult remove = mongoTemplate.remove(query, Student.class, COLLECTION_STUDENTS);

		return remove.getN();
	}

	public Student findUsingOrOperator(String name, String otherName) {
		Criteria criteria = new Criteria();
		criteria.orOperator(Criteria.where("name").is(name),
				Criteria.where("name").is(otherName));
		Query query = new Query(criteria);

		Student student = mongoTemplate.findOne(query, Student.class);

		return student;
	}

	@Override
	public List<Student> textSearch(String... words) {
		TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(words);

		Query query = TextQuery
				.queryText(criteria);

		List<Student> students = mongoTemplate.find(query, Student.class);

		return students;

	}
}
