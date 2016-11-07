package com.eaglegenomics.simlims.core;

import java.util.Arrays;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * The basic application logic behind how all Activity objects should behave.
 * This includes things such as defining input and output types.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public abstract class AbstractActivity implements Activity {

	private String description = "(no description)";
	private String name = "(no name)";
	private int version = 1;
	private Class<?> inputDataClass = String.class;
	private Class<?> outputDataClass = String.class;
	private int maxAttempts = Activity.DEFAULT_MAX_ATTEMPTS;
	private String role = null;

	/**
	 * Defaults to {@link Activity.DEFAULT_MAX_ATTEMPTS}.
	 */
	public int getMaxAttempts() {
		return maxAttempts;
	}

	/**
	 * Max attempts may or may not be of use to the Activity. It is provided
	 * here for convenience but may not be honoured. It mostly refers to
	 * AutomatedActivity objects which are run within the Runner, which does
	 * take notice of max attempts. If a value is supplied, it must be >= 1.
	 */
	public void setMaxAttempts(int maxAttempts) {
		if (maxAttempts <= 0) {
			throw new IllegalArgumentException("MaxAttempts must be >= 1");
		}
		this.maxAttempts = maxAttempts;
	}

	/**
	 * Defaults to "(no description").
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Defaults to "(no name)".
	 */
	public String getName() {
		return name;
	}

	/**
	 * Unless otherwise specified, the role defaults to the unique identifier.
	 */
	public String getRole() {
		return role == null ? getUniqueIdentifier() : role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * The version is used to distinguish between upgraded versions of the
	 * activity. It also differentiates between different configurations of the
	 * same activity. Defaults to 1.
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Defaults to String.
	 */
	@SuppressWarnings("unchecked")
	public <C> Class<C> getInputDataClass() {
		return (Class<C>) this.inputDataClass;
	}

	/**
	 * Defaults to String.
	 */
	@SuppressWarnings("unchecked")
	public <C> Class<C> getOutputDataClass() {
		return (Class<C>) this.outputDataClass;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setVersion(int version) {
		if (version <= 0) {
			throw new IllegalArgumentException("Version must be >= 1");
		}
		this.version = version;
	}

	public <C> void setInputDataClass(Class<C> inputDataClass) {
		this.inputDataClass = inputDataClass;
	}

	public <C> void setOutputDataClass(Class<C> outputDataClass) {
		this.outputDataClass = outputDataClass;
	}

	/**
	 * Defaults to the format 'Name v1' where 'Name' is from getName() and '1'
	 * is getVersion().
	 */
	public String getUniqueIdentifier() {
		return getName() + " v" + getVersion();
	}

	/**
	 * Activities can be read (i.e. seen) by everyone who has an active account.
	 */
	public boolean userCanRead(User user) {
		return user.isActive();
	}

	/**
	 * Activities can be written (i.e. executed) only by those active users who
	 * have the role granted to them, as defined by getRole().
	 */
	public boolean userCanWrite(User user) {
		return user.isActive()
				&& Arrays.asList(user.getRoles()).contains(getRole());
	}

	/**
	 * Activities are equated using their unique identifier.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof Activity))
			return false;
		Activity them = (Activity) obj;
		return this.getUniqueIdentifier().equals(them.getUniqueIdentifier());
	}

	@Override
	public int hashCode() {
		return getUniqueIdentifier().hashCode();
	}

	/**
	 * Equivalent to getUniqueIdentifier().
	 */
	@Override
	public String toString() {
		return getUniqueIdentifier();
	}
}
