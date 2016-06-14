package com.manthansharma.everpoll.api.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionSet {
	
	private Integer id;
	private String name;
	private String owner;
	private List<Question> question = new ArrayList<>();
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	/**
	 * @return The id
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * @param id The id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * @return The name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name The name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return The owner
	 */
	public String getOwner() {
		return owner;
	}
	
	/**
	 * @param owner The owner
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return The question
	 */
	public List<Question> getQuestion() {
		return question;
	}

	/**
	 * @param question The question
	 */
	public void setQuestion(List<Question> question) {
		this.question = question;
	}
	
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}
	
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
}