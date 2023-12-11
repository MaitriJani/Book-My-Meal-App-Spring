package com.rspl.controllers;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rspl.dto.AuthenticationRequest;
import com.rspl.dto.AuthenticationResponse;
import com.rspl.entities.User;
import com.rspl.repositories.UserRepository;
import com.rspl.services.utils.jwtutils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;



@RestController
public class AuthenticationController {
	
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	@Qualifier("detailsService")
	private UserDetailsService userDetailsService;
	
	@Autowired
	private jwtutils jwtutils;
	
	@Autowired
	private UserRepository userRepository;
	
	public static final String TOKEN_PREFIX = "Bearer ";
	
	public static final String HEADER_STRING = "Authorization ";
	
	
	
	@PreAuthorize("hasRole('Admin')")
    @GetMapping("/admin/dashboard")
    public ResponseEntity<?> adminResource() {
       
        return ResponseEntity.ok("Admin resource accessed!");
    }
	
	 @PreAuthorize("hasRole('Employee')")
	    @GetMapping("/employee/booking")
	    public ResponseEntity<?> employeeResource() {
	      
	        String employeeData = "This is employee-specific data.";
	        return ResponseEntity.ok(employeeData);
	    }

	
	@PostMapping("/authenticate")
	public void createAuthentication(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException, JSONException {
		try {
			
//			System.out.println("testing auth");
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
//			throw new BadCredentialsException("Incorrect Username or Password");
			
			
			String errorMessage = "Invalid email or password";
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);
			
		} catch (DisabledException disabledException) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "User is not created");
			return;
		}
		
		
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		
		Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
		final String jwt = jwtutils.generateToken(userDetails.getUsername(), optionalUser.get().getRole().toString());
		System.out.println(jwt);
		
		if (optionalUser.isPresent()) {
			response.getWriter().write(new JSONObject()
					.put("userId",optionalUser.get().getId())
							.put("role",optionalUser.get().getRole())
								.put("Token", jwt )
							.toString());
			
			
		}
		
		response.setHeader("Access-Control-Expose-Headers", "Authorization");
		response.setHeader("Access-Control-Allow-Headers", "Authorization, X-Pingother,Origin,X-Requested-With,Content-Type,Accept, X-Custom-header");
		response.setHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
		
//		return new AuthenticationResponse(jwt);
	}

}
