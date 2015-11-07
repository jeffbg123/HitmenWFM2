package com.hitmenwfm.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;

class SampleAuthenticationManager implements AuthenticationManager {
	  static final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();

	  static {
	    AUTHORITIES.add(new GrantedAuthorityImpl("ROLE_USER"));
	  }

	  public Authentication authenticate(Authentication auth) throws AuthenticationException {
	    
	      return new UsernamePasswordAuthenticationToken(auth.getName(),
	        auth.getCredentials(), AUTHORITIES);
	     
	  }
	}
