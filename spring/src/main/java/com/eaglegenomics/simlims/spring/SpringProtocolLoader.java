package com.eaglegenomics.simlims.spring;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.eaglegenomics.simlims.core.Protocol;
import com.eaglegenomics.simlims.core.manager.ProtocolManager;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * Locates and loads all protocols defined as Spring beans.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public class SpringProtocolLoader implements InitializingBean,
		ApplicationContextAware {
	protected static final Logger log = LoggerFactory.getLogger(SpringProtocolLoader.class);

	@Autowired
	private ApplicationContext context;

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context = context;
	}

	@Autowired
	private ProtocolManager protocolManager;

	public void setProtocolManager(ProtocolManager protocolManager) {
		this.protocolManager = protocolManager;
	}

	public void afterPropertiesSet() throws Exception {
		// Load protocol beans.
		if (log.isInfoEnabled()) {
			log.info("Loading protocol beans");
		}
		for (Map.Entry<String, Protocol> entry : context.getBeansOfType(
				Protocol.class).entrySet()) {
			if (log.isInfoEnabled()) {
				log.info("Loading protocol bean " + entry.getKey());
			}
			protocolManager.validateProtocol(entry.getValue());
			protocolManager.cacheProtocol(entry.getValue());
		}
		if (log.isInfoEnabled()) {
			log.info("Done loading protocol beans");
		}
	}
}
