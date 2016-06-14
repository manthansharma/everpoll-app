package com.manthansharma.everpoll.api.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionSetList {
	private Integer count;
	private Object next;
	private Object previous;
	private List<QuestionSet> results = new ArrayList<>();
	private Map<String, Object> additionalProperties = new HashMap<>();

	/**
	 * @return The count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * @param count The count
	 */
	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 * @return The next
	 */
	public Object getNext() {
		return next;
	}

	/**
	 * @param next The next
	 */
	public void setNext(Object next) {
		this.next = next;
	}

	/**
	 * @return The previous
	 */
	public Object getPrevious() {
		return previous;
	}

	/**
	 * @param previous The previous
	 */
	public void setPrevious(Object previous) {
		this.previous = previous;
	}

	/**
	 * @return The results
	 */
	public List<QuestionSet> getResults() {
		return results;
	}

	/**
	 * @param results The results
	 */
	public void setResults(List<QuestionSet> results) {
		this.results = results;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
