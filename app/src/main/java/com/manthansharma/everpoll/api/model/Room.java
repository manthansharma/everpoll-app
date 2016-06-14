package com.manthansharma.everpoll.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Room {

	private Integer id;
	private String name;
	private String description;
	private String owner;
	private Integer question_set;
	private Boolean destroyed;
	@SerializedName("public")
	private Boolean _public;
	private String days_ago;
	private Integer response;
	private QuestionSet question_set_detail;
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
	 * @return The description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @return The question_set
	 */
	public Integer getQuestionSet() {
		return question_set;
	}

	/**
	 * @param question_set The question_set
	 */
	public void setQuestionSet(Integer question_set) {
		this.question_set = question_set;
	}

	/**
	 * @return The destroyed
	 */
	public Boolean getDestroyed() {
		return destroyed;
	}

	/**
	 * @param destroyed The destroyed
	 */
	public void setDestroyed(Boolean destroyed) {
		this.destroyed = destroyed;
	}

	/**
	 * @return The _public
	 */
	public Boolean getPublic() {
		return _public;
	}

	/**
	 * @param _public The public
	 */
	public void setPublic(Boolean _public) {
		this._public = _public;
	}

	/**
	 * @return The response
	 */
	public Integer getResponse() {
		return response;
	}

	/**
	 * @param response The response
	 */
	public void setResponse(Integer response) {
		this.response = response;
	}

	/**
	 * @return The daysAgo
	 */
	public String getDaysAgo() {
		return days_ago;
	}

	/**
	 * @param days_ago The days_ago
	 */
	public void setDaysAgo(String days_ago) {
		this.days_ago = days_ago;
	}

	/**
	 * @return The questionSet
	 */
	public QuestionSet getQuestionSetDetail() {
		return question_set_detail;
	}

	/**
	 * @param question_set_detail The questionSet
	 */
	public void setQuestionSetDetail(QuestionSet question_set_detail) {
		this.question_set_detail = question_set_detail;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}