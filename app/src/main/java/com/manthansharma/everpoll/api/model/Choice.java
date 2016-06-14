package com.manthansharma.everpoll.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Choice {

	private Integer id;
	@SerializedName("choice_text")
	private String choiceText;
	private Integer votes;
	private Integer question;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Choice(String choiceText) {
		this.choiceText = choiceText;
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
	 * @return The choiceText
	 */
	public String getChoiceText() {
		return choiceText;
	}

	/**
	 * @param choiceText The choice_text
	 */
	public void setChoiceText(String choiceText) {
		this.choiceText = choiceText;
	}

	/**
	 * @return The votes
	 */
	public Integer getVotes() {
		return votes;
	}

	/**
	 * @param votes The votes
	 */
	public void setVotes(Integer votes) {
		this.votes = votes;
	}

	/**
	 * @return The question
	 */
	public Integer getQuestion() {
		return question;
	}

	/**
	 * @param question The question
	 */
	public void setQuestion(Integer question) {
		this.question = question;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}