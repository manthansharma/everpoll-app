package com.manthansharma.everpoll.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Question {

	private Integer id;
	@SerializedName("question_text")
	private String questionText;
	private Integer set;
	private List<Choice> choice = new ArrayList<Choice>();
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Question(String questionText, List<Choice> choice) {
		this.choice = choice;
		this.questionText = questionText;
	}

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
	 * @return The questionText
	 */
	public String getQuestionText() {
		return questionText;
	}

	/**
	 * @param questionText The question_text
	 */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	/**
	 * @return The set
	 */
	public Integer getSet() {
		return set;
	}

	/**
	 * @param set The set
	 */
	public void setSet(Integer set) {
		this.set = set;
	}

	/**
	 * @return The choice
	 */
	public List<Choice> getChoice() {
		return choice;
	}

	/**
	 * @param choice The choice
	 */
	public void setChoice(List<Choice> choice) {
		this.choice = choice;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}