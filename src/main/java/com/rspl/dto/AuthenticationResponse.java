package com.rspl.dto;

import java.util.List;

public class AuthenticationResponse {
	
	private String jwtToken;
//
	private String type = "Bearer";
	private Long id;
	private String username;
	private String email;
	private List<String> roles;
	
	public AuthenticationResponse(String accessToken, Long id, String username, String email, List<String> roles) {
		this.jwtToken = accessToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}
	
	public String getAccessToken() {
		return jwtToken;
	}

	public void setAccessToken(String accessToken) {
		this.jwtToken = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}


	
	
//	
	public String getJwtToken() {
		return jwtToken;
	}



	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	
	public AuthenticationResponse(String jwt) {
		
		this.jwtToken = jwt;
		 
	}




	
	
}
