package com.eaglegenomics.simlims.demoprotocol;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.eaglegenomics.simlims.core.ActivityData;
import com.eaglegenomics.simlims.core.Request;
import com.eaglegenomics.simlims.core.User;
import com.eaglegenomics.simlims.core.manager.ProtocolManager;
import com.eaglegenomics.simlims.core.manager.RequestManager;
import com.eaglegenomics.simlims.core.manager.SecurityManager;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * Demonstration controller which shows how to display the results from the demo
 * protocol.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
@Controller
@RequestMapping("/protocols/demoprotocol/results")
public class DemoProtocolResultsController {
	protected static final Log log = LogFactory
			.getLog(DemoProtocolResultsController.class);

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private RequestManager requestManager;

	@Autowired
	private ProtocolManager protocolManager;

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public void setRequestManager(RequestManager requestManager) {
		this.requestManager = requestManager;
	}

	public void setProtocolManager(ProtocolManager protocolManager) {
		this.protocolManager = protocolManager;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showResults(
			@RequestParam(value = "requestId", required = true) long requestId,
			@RequestParam(value = "executionCount", required = true) int executionCount,
			ModelMap model) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("Getting results for request " + requestId
					+ " execution " + executionCount);
		}
		try {
			User user = securityManager
					.getUserByLoginName(SecurityContextHolder.getContext()
							.getAuthentication().getName());
			Request request = requestManager.getRequestById(requestId);
			if (!request.userCanRead(user)) {
				throw new SecurityException("Permission denied.");
			}

			Collection<ActivityData> activityData = protocolManager
					.getRequestResults(user, request, executionCount);
			// There should only be one result per request, which is a single
			// string.
			if (activityData.size() > 0) {
				model.put("result", activityData.iterator().next()
						.getDataReference().resolve());
			} else {
				model.put("result", "(No results yet)");
			}

			return new ModelAndView("/pages/results/demoprotocol.jsp", model);
		} catch (IOException ex) {
			if (log.isDebugEnabled()) {
				log.debug("Failed to show results", ex);
			}
			throw ex;
		}
	}
}
