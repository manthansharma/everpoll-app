package com.manthansharma.everpoll.api.model;

import java.util.HashMap;
import java.util.Map;

public class User {
	private Integer id;
	private String email;
	private String password;
	private String first_name;
	private String last_name;
	private String auth_token;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * @param email    The email
	 * @param password The password
	 */
	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	/**
	 * @param name The name
	 * @param email The email
	 * @param password The password
	 */
	public User(String name, String email, String password) {
		setName(name);
		this.email = email;
		this.password = password;
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
	 * @return The email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email The email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return The password
	 */
	public String setPassword() {
		return email;
	}

	/**
	 * @param password The password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return The firstName
	 */
	public String getFirstName() {
		return first_name;
	}

	/**
	 * @param firstName The first_name
	 */
	public void setFirstName(String firstName) {
		this.first_name = firstName;
	}

	/**
	 * @return The lastName
	 */
	public String getLastName() {
		return last_name;
	}

	/**
	 * @param lastName The last_name
	 */
	public void setLastName(String lastName) {
		this.last_name = lastName;
	}

	/**
	 * @return The Name
	 */
	public String getName() {
		return first_name + " " + last_name;
	}

	/**
	 * @param name The name
	 */
	public void setName(String name) {
		String[] name_arr = name.split(" ", 2);
		first_name = name_arr[0];
		last_name = name_arr[1];
	}

	/**
	 * @return The auth_token
	 */
	public String getAuthToken() {
		return auth_token;
	}

	/**
	 * @param auth_token The auth_token
	 */
	public void setAuthToken(String auth_token) {
		this.auth_token = auth_token;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}


}