package com.eaglegenomics.simlims.spring;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import com.eaglegenomics.simlims.core.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import com.eaglegenomics.simlims.core.manager.ProtocolManager;
import com.eaglegenomics.simlims.core.manager.RequestManager;
import com.eaglegenomics.simlims.core.manager.SecurityManager;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * Binds the main Hibernate SimLIMS datatypes to Spring. This helps when they're
 * used in dropdowns, checkboxes, etc.
 *
 * @author Richard Holland
 * @since 0.0.1
 */
public class HibernateBindingInitializer implements WebBindingInitializer {
	protected static final Log log = LogFactory
			.getLog(HibernateBindingInitializer.class);

	@Autowired
	private ProtocolManager protocolManager;

	@Autowired
	private RequestManager requestManager;

	@Autowired
	private SecurityManager securityManager;

	public void setRequestManager(RequestManager requestManager) {
		assert (requestManager != null);
		this.requestManager = requestManager;
	}

	public void setProtocolManager(ProtocolManager protocolManager) {
		assert (protocolManager != null);
		this.protocolManager = protocolManager;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		assert (securityManager != null);
		this.securityManager = securityManager;
	}

	public void initBinder(WebDataBinder binder, WebRequest req) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				new SimpleDateFormat("dd/MM/yyyy"), false));
		binder.registerCustomEditor(Set.class, "groups",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						return resolveGroup(element);
					}
				});
		binder.registerCustomEditor(Activity.class,
				new PropertyEditorSupport() {
					@Override
					public void setAsText(String element)
							throws IllegalArgumentException {
						setValue(resolveActivity(element));
					}
				});
		binder.registerCustomEditor(ProjectImpl.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String element)
					throws IllegalArgumentException {
				setValue(resolveProject(element));
			}
		});
		binder.registerCustomEditor(Request.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String element)
					throws IllegalArgumentException {
				setValue(resolveRequest(element));
			}
		});
		binder.registerCustomEditor(Protocol.class,
				new PropertyEditorSupport() {
					@Override
					public void setAsText(String element)
							throws IllegalArgumentException {
						setValue(resolveProtocol(element));
					}
				});
		binder.registerCustomEditor(Group.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String element)
					throws IllegalArgumentException {
				setValue(resolveGroup(element));
			}
		});
		binder.registerCustomEditor(Set.class, "users",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						return resolveUser(element);
					}
				});
		binder.registerCustomEditor(UserImpl.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String element)
					throws IllegalArgumentException {
				setValue(resolveUser(element));
			}
		});
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(
				Long.class, false));
	}

	private Group resolveGroup(Object element) throws IllegalArgumentException {
		Long id = null;

		if (element instanceof String)
			id = NumberUtils.parseNumber((String) element, Long.class)
					.longValue();

		try {
			return id != null ? securityManager.getGroupById(id) : null;
		} catch (IOException e) {
			if (log.isDebugEnabled()) {
				log.debug("Failed to resolve group " + element, e);
			}
			throw new IllegalArgumentException(e);
		}
	}

	private User resolveUser(Object element) throws IllegalArgumentException {
		Long id = null;

		if (element instanceof String)
			id = NumberUtils.parseNumber((String) element, Long.class)
					.longValue();

		try {
			return id != null ? securityManager.getUserById(id) : null;
		} catch (IOException e) {
			if (log.isDebugEnabled()) {
				log.debug("Failed to resolve user " + element, e);
			}
			throw new IllegalArgumentException(e);
		}
	}

	private Request resolveRequest(Object element)
			throws IllegalArgumentException {
		Long id = null;

		if (element instanceof String)
			id = NumberUtils.parseNumber((String) element, Long.class)
					.longValue();

		try {
			return id != null ? requestManager.getRequestById(id) : null;
		} catch (IOException e) {
			if (log.isDebugEnabled()) {
				log.debug("Failed to resolve request " + element, e);
			}
			throw new IllegalArgumentException(e);
		}
	}

	private Project resolveProject(Object element)
			throws IllegalArgumentException {
		Long id = null;

		if (element instanceof String)
			id = NumberUtils.parseNumber((String) element, Long.class)
					.longValue();

		try {
			return id != null ? requestManager.getProjectById(id) : null;
		} catch (IOException e) {
			if (log.isDebugEnabled()) {
				log.debug("Failed to resolve project " + element, e);
			}
			throw new IllegalArgumentException(e);
		}
	}

	private Activity resolveActivity(Object element)
			throws IllegalArgumentException {
		String id = null;

		if (element instanceof String)
			id = element.toString();

		try {
			return id != null ? protocolManager.getActivity(id) : null;
		} catch (IOException e) {
			if (log.isDebugEnabled()) {
				log.debug("Failed to resolve activity " + element, e);
			}
			throw new IllegalArgumentException(e);
		}
	}

	private Protocol resolveProtocol(Object element)
			throws IllegalArgumentException {
		String id = null;

		if (element instanceof String)
			id = element.toString();

		try {
			return id != null ? protocolManager.getProtocol(id) : null;
		} catch (IOException e) {
			if (log.isDebugEnabled()) {
				log.debug("Failed to resolve protocol " + element, e);
			}
			throw new IllegalArgumentException(e);
		}
	}
}