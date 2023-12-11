package com.rspl.services.utils;

import io.jsonwebtoken.Claims;



import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.rspl.controllers.AuthenticationController;
import com.rspl.dto.AuthenticationRequest;
import com.rspl.dto.AuthenticationResponse;
import com.rspl.services.employee.EmployeeServiceImpl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class jwtutils {
	
	public static final String secret = "asdfghdjahgdhsasdfAXdfdGGFGFHSEREjshkfdhSRJTJSKJKFGJDFGKDfjkdhgkhf34GFDS455fd";
	

    public String extractUsername(String token) {
    	return extractClaim(token, Claims::getSubject);
  }
    
	
    private String extractRole(String token) {
    	return extractClaim(token, Claims::getSubject);
	}
    
   
 

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJwt(token).getBody();
    }
   
	
//	private Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
//    }

	private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

	 public Boolean validateToken(String token, UserDetails userDetails) {
	        final String username = extractUsername(token);
	        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    }




	public String generateToken(String userName, String role) {
//    	User user = (User) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        
        return createToken(claims, userName, role);
    }
	
	

	 
    public String getRoleFromJwtToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJwt(token).getBody();
        return (String) claims.get("role");
    }
	

    
	private String createToken(Map<String, Object> claims, String userName, String role) {
	

        return Jwts
        		.builder()
        		.setClaims(claims)
        		.setSubject(userName)
        		.setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secret)
                .claim("role", role)  // Added the role to the claims
                .compact();
    }
}