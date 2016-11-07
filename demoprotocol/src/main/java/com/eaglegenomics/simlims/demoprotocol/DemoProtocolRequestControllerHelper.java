package com.eaglegenomics.simlims.demoprotocol;

import java.util.Collection;
import java.util.Collections;

import com.eaglegenomics.simlims.spring.AbstractRequestControllerHelper;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * Demonstration request creation controller for the demo protocol.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public class DemoProtocolRequestControllerHelper extends
		AbstractRequestControllerHelper {

	public Collection<?> convertDataModelToInputData(Object dataModel) {
		DemoProtocolDataModel model = (DemoProtocolDataModel) dataModel;
		// Input is a single string.
		return Collections.singleton(model.getYourName());
	}

	public Object createDataModel() {
		return new DemoProtocolDataModel();
	}

	class DemoProtocolDataModel {
		private String yourName = "Demo name";

		public String getYourName() {
			return yourName;
		}

		public void setYourName(String name) {
			this.yourName = name;
		}

	}
}
