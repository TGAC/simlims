package com.eaglegenomics.simlims.spring;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.eaglegenomics.simlims.core.User;
import com.eaglegenomics.simlims.core.manager.SecurityManager;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * A method of providing authentication details to Spring Security by loading
 * SimLIMS User objects.
 * <p>
 * TODO Don't use plain text passwords like this. Bad!
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public class HibernateUserDetailsService implements UserDetailsService {
	protected static final Logger log = LoggerFactory.getLogger(HibernateUserDetailsService.class);

	/**
	 * A standard role implying admin access.
	 */
	public static final GrantedAuthority ROLE_ADMIN = new GrantedAuthorityImpl(
			"ROLE_ADMIN");
	/**
	 * A standard role implying a user is an employee of the service centre.
	 * Users can be both employees and customers.
	 */
	public static final GrantedAuthority ROLE_INTERNAL = new GrantedAuthorityImpl(
			"ROLE_INTERNAL");
	/**
	 * A standard role implying a user is a customer of the service centre.
	 * Users can be both employees and customers.
	 */
	public static final GrantedAuthority ROLE_EXTERNAL = new GrantedAuthorityImpl(
			"ROLE_EXTERNAL");

	@Autowired
	private SecurityManager securityManager;

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public UserDetails loadUserByUsername(String loginName)
			throws UsernameNotFoundException, DataAccessException {
		User userNonFinal = null;
		try {
			userNonFinal = securityManager.getUserByLoginName(loginName);
		} catch (IOException e) {
			if (log.isDebugEnabled()) {
				log.debug("Failed to load user " + loginName, e);
			}
		}
		if (userNonFinal == null) {
			if (log.isInfoEnabled()) {
				log.info("User " + loginName + " not found");
			}
			return null;
		} else {
			if (log.isInfoEnabled()) {
				log.info("User " + loginName + " found");
			}
		}
		final User user = userNonFinal;
		final Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		// Set ROLE_INTERNAL or ROLE_EXTERNAL
		if (user.isInternal()) {
			authorities.add(ROLE_INTERNAL);
		}
		if (user.isExternal()) {
			authorities.add(ROLE_EXTERNAL);
		}
		// If admin, set ROLE_ADMIN
		if (user.isAdmin()) {
			authorities.add(ROLE_ADMIN);
		}
		// Set other roles
		if (user.getRoles() != null) {
			for (String role : user.getRoles()) {
				authorities.add(new GrantedAuthorityImpl(role));
			}
		}
		if (log.isInfoEnabled()) {
			log.info("User " + loginName + " has roles " + authorities);
		}
		return new UserDetails() {
			private static final long serialVersionUID = 1L;

			public Collection<GrantedAuthority> getAuthorities() {
				return authorities;
			}

			public String getPassword() {
				return user.getPassword();
			}

			public String getUsername() {
				return user.getLoginName();
			}

			public boolean isAccountNonExpired() {
				return user.isActive();
			}

			public boolean isAccountNonLocked() {
				return user.isActive();
			}

			public boolean isCredentialsNonExpired() {
				return user.isActive();
			}

			public boolean isEnabled() {
				return user.isActive();
			}
		};
	}

}
