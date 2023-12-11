package com.rspl.dto;

import com.rspl.enums.UserRole;

public class AuthenticationRequest {
	
	private String email;
	
	private String password;
	
	private static UserRole role;

	



	public static UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
