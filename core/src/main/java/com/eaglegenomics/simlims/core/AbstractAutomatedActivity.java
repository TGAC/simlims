package com.eaglegenomics.simlims.core;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * The basic application logic behind how all automated Activity objects should
 * behave.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public abstract class AbstractAutomatedActivity extends AbstractActivity
		implements AutomatedActivity {

	private int minDataCountPerSession = AutomatedActivity.DEFAULT_MIN_DATA_COUNT;
	private int maxDataCountPerSession = AutomatedActivity.DEFAULT_MAX_DATA_COUNT;
	private int maxDataAge = AutomatedActivity.DEFAULT_MAX_DATA_AGE;

	public int getMinDataCountPerSession() {
		return minDataCountPerSession;
	}

	public void setMinDataCountPerSession(int minDataCountPerSession) {
		if (minDataCountPerSession <= 0) {
			throw new IllegalArgumentException("MinDataCount must be >= 1");
		}
		this.minDataCountPerSession = minDataCountPerSession;
	}

	public int getMaxDataCountPerSession() {
		return maxDataCountPerSession;
	}

	public void setMaxDataCountPerSession(int maxDataCountPerSession) {
		if (maxDataCountPerSession <= 0) {
			throw new IllegalArgumentException("MaxDataCount must be >= 1");
		}
		this.maxDataCountPerSession = maxDataCountPerSession;
	}

	public int getMaxDataAge() {
		return maxDataAge;
	}

	public void setMaxDataAge(int maxDataAge) {
		if (maxDataAge <= 0) {
			throw new IllegalArgumentException("MaxDataAge must be >= 1");
		}
		this.maxDataAge = maxDataAge;
	}
}
