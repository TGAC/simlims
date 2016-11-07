package com.eaglegenomics.simlims.spring;

import java.util.Collection;
import java.util.Properties;

import com.eaglegenomics.simlims.core.Activity;
import com.eaglegenomics.simlims.core.ActivityData;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * This helper assists the Spring interfaces in knowing how to request input for
 * an activity, and how to convert the input form into input data for the
 * activity.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public interface ActivityControllerHelper {

	public Activity getActivity();

	public Object createDataModel();

	public String getDataModelView();

	/**
	 * This method should construct a set of Properties based on the data model
	 * and locked input data provided, which can be fully understood by the
	 * execute() method on the activity for which this helper is for. The
	 * execute() method of the activity itself should handle the setting up of
	 * activity output, exceptions, skipped data, and creation and persistence
	 * of data reference objects.
	 */
	public Properties convertDataModelToActivityProperties(
			Collection<ActivityData> inputData, Object dataModel);

	/**
	 * This defines how a specific piece of lockable input data should appear in
	 * the user interface in the list of lockable input data options.
	 */
	public String convertActivityDataToDisplayName(ActivityData activityData);
}
