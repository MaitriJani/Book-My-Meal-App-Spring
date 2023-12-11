package com.rspl.services.employee;

import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

import com.rspl.entities.User;
import com.rspl.enums.UserRole;
import com.rspl.repositories.UserRepository;

import jakarta.annotation.PostConstruct;



@Service
public class EmployeeServiceImpl{
	
	private final UserRepository userRepository;
	
	public EmployeeServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	
	
	
	
	@PostConstruct
	public void createEmployeeAccount() { 
		User employeeAccount = userRepository.findByRole(UserRole.Employee);
		if(employeeAccount == null) {
		User Employee = new User();
		Employee.setEmail("emp@test.com");
		Employee.setName("emp1");
		Employee.setRole(UserRole.Employee);
		Employee.setPassword(new BCryptPasswordEncoder().encode("pass"));
		userRepository.save(Employee);
		
		User employeeAccount2 = userRepository.findByRole(UserRole.Admin);
		User Admin = new User();
		Admin.setEmail("admin@test.com");
		Admin.setName("admin1");
		Admin.setRole(UserRole.Admin);
		Admin.setPassword(new BCryptPasswordEncoder().encode("adminpass"));
		userRepository.save(Admin);
		} else {
//			
		}
		
	
		
		
		
}
	
	
}
