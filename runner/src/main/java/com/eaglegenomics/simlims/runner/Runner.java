package com.eaglegenomics.simlims.runner;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.eaglegenomics.simlims.core.Activity;
import com.eaglegenomics.simlims.core.ActivityDataFilter;
import com.eaglegenomics.simlims.core.ActivitySession;
import com.eaglegenomics.simlims.core.ActivitySessionFactory;
import com.eaglegenomics.simlims.core.AutomatedActivity;
import com.eaglegenomics.simlims.core.User;
import com.eaglegenomics.simlims.core.exception.ActivityFailedException;
import com.eaglegenomics.simlims.core.manager.ProtocolManager;
import com.eaglegenomics.simlims.core.manager.SecurityManager;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * Searches for and runs AutomatedActivities in the background at specified
 * periods.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public class Runner {
	protected static final Logger log = LoggerFactory.getLogger(Runner.class);

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws SecurityNotStoredException
	 * @throws DataNotStoredException
	 * @throws ActivityFailedException
	 * @throws NonAutomatedActivityException
	 */
	public static void main(String[] args) throws InterruptedException,
			IOException, ActivityFailedException {
		if (log.isInfoEnabled()) {
			log.info("Starting runner");
		}
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:/applicationContext.xml");
		AuthenticationManager authenticationManager = context
				.getBean(AuthenticationManager.class);
		ProtocolManager protocolManager = context
				.getBean(ProtocolManager.class);
		SecurityManager securityManager = context
				.getBean(SecurityManager.class);
		LoginDetails loginDetails = context.getBean(LoginDetails.class);
		ActivitySessionFactory factory = context
				.getBean(ActivitySessionFactory.class);
		if (log.isInfoEnabled()) {
			log.info("Logging in");
		}
		Authentication request = new UsernamePasswordAuthenticationToken(
				loginDetails.getUsername(), loginDetails.getPassword());
		Authentication result = authenticationManager.authenticate(request);
		SecurityContextHolder.getContext().setAuthentication(result);
		User user = securityManager.getUserByLoginName(result.getName());
		while (true) { // Loop forever
			if (log.isInfoEnabled()) {
				log.info("Waking up - loop starting");
			}
			for (Activity activity : protocolManager.listAllActivities()) {
				if (activity instanceof AutomatedActivity) {
					if (log.isInfoEnabled()) {
						log.info("Running: " + activity.getUniqueIdentifier());
					}
					ActivitySession session = factory.createActivitySession(
							user, activity);
					session.automateActivity(ActivityDataFilter.FILTER_ACCEPT_ALL);
				}
			}
			if (log.isInfoEnabled()) {
				log.info("Sleeping - loop ended");
			}
			Thread.sleep(60 * 1000); // Wait 1 minute
		}
	}

	/**
	 * Logs out.
	 */
	@Override
	public void finalize() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}
}
