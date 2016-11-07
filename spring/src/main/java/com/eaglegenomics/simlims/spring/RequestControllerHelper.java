package com.eaglegenomics.simlims.spring;

import java.util.Collection;

import com.eaglegenomics.simlims.core.Protocol;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * This helper assists the Spring interfaces in knowing how to create a new
 * request, and how to convert the input form into input data for the starting
 * activity in the protocol the request is for.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public interface RequestControllerHelper {

	public Protocol getProtocol();

	public Object createDataModel();

	public String getDataModelView();

	public Collection<?> convertDataModelToInputData(Object dataModel);

	/**
	 * The redirect URL to the results controller, with leading slash but no
	 * redirect: prefix. The controller will receive a parameter set including
	 * requestId and executionCount. It should use appropriate managers/stores
	 * to resolve these. Security checks should be repeated as the controller
	 * may be called directly and not via the ViewRequestController.
	 * 
	 * @return
	 */
	public String getResultsControllerUrl();

}
