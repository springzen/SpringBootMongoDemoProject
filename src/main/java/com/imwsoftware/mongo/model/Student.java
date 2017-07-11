package com.imwsoftware.mongo.model;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "students")
public class Student {

	@Id
	private String id;

	@TextIndexed
	private String email;

	@TextIndexed
	private ArrayList<String> phone;

	@Indexed(unique = true)
	private String name;

	@TextIndexed
	private String degree;

	@TextIndexed
	private ArrayList<Subject> subjects;

	@Indexed
	private int age;
	
	private int points;

	public Student() {

	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ArrayList<String> getPhone() {
		return this.phone;
	}

	public void setPhone(ArrayList<String> phone) {
		this.phone = phone;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDegree() {
		return this.degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public ArrayList<Subject> getSubjects() {
		return this.subjects;
	}

	public void setSubjects(ArrayList<Subject> subjects) {
		this.subjects = subjects;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
}
