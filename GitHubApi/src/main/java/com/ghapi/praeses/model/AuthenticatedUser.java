package com.ghapi.praeses.model;

public class AuthenticatedUser {
	private String username;
	private String token;
	
	public AuthenticatedUser(String username, String token) {
		this.username = username;
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
