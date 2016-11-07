package com.eaglegenomics.simlims.spring;

import com.eaglegenomics.simlims.core.Activity;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * An activity controller helper with default behaviour defined.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public abstract class AbstractActivityControllerHelper implements
		ActivityControllerHelper {

	private Activity activity;

	private String dataModelView;

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setDataModelView(String dataModelView) {
		this.dataModelView = dataModelView;
	}

	public String getDataModelView() {
		return dataModelView;
	}

}
