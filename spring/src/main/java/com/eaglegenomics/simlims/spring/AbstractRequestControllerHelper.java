package com.eaglegenomics.simlims.spring;

import com.eaglegenomics.simlims.core.Protocol;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * A request controller helper with default behaviour defined.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public abstract class AbstractRequestControllerHelper implements
		RequestControllerHelper {

	private Protocol protocol;

	private String dataModelView;

	private String resultsControllerUrl;

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public void setDataModelView(String dataModelView) {
		this.dataModelView = dataModelView;
	}

	public String getDataModelView() {
		return dataModelView;
	}

	public void setResultsControllerUrl(String resultsControllerUrl) {
		this.resultsControllerUrl = resultsControllerUrl;
	}

	public String getResultsControllerUrl() {
		return resultsControllerUrl;
	}

}
