package com.eaglegenomics.simlims.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * The basic definition from which all concrete Protocol implementations should
 * derive. This handles behaviour which should be common regardless of
 * implementation choices to do with backing managers etc.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public abstract class AbstractProtocol implements Protocol {
	private Map<String, Activity> activityAliasMap = new HashMap<String, Activity>();
	private Map<String, String[]> activityFlowMap = new HashMap<String, String[]>();
	private ProtocolVisibility visibility = ProtocolVisibility.EVERYBODY;
	private String name;
	private String description;
	private String startpoint;
	private Collection<String> endpoints;
	private int publicVersion = 1;
	private int privateVersion = 1;
	private String role;

	public Map<String, Activity> getActivityAliasMap() {
		return activityAliasMap;
	}

	public Map<String, String[]> getActivityFlowMap() {
		return activityFlowMap;
	}

	public ProtocolVisibility getVisibility() {
		return visibility;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public String getRole() {
		return role == null ? getUniqueIdentifier() : role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getStartpoint() {
		return startpoint;
	}

	public Collection<String> getEndpoints() {
		return endpoints;
	}

	public int getPrivateVersion() {
		return privateVersion;
	}

	public int getPublicVersion() {
		return publicVersion;
	}

	public void setActivityAliasMap(Map<String, Activity> activityAliasMap) {
		this.activityAliasMap = activityAliasMap;
	}

	public void setActivityFlowMap(Map<String, String[]> activityFlowMap) {
		this.activityFlowMap = activityFlowMap;
	}

	public void setVisibility(ProtocolVisibility visibility) {
		this.visibility = visibility;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStartpoint(String startpoint) {
		this.startpoint = startpoint;
	}

	public void setEndpoints(Collection<String> endpoints) {
		this.endpoints = endpoints;
	}

	public void setPrivateVersion(int privateVersion) {
		this.privateVersion = privateVersion;
	}

	public void setPublicVersion(int publicVersion) {
		this.publicVersion = publicVersion;
	}

	public String getUniqueIdentifier() {
		return getName() + " v" + getPublicVersion() + "."
				+ getPrivateVersion();
	}

	/**
	 * All active users can read.
	 */
	public boolean userCanRead(User user) {
		return user.isActive();
	}

	/**
	 * Users can write if they are active and their roles contain the getRole()
	 * of this protocol.
	 */
	public boolean userCanWrite(User user) {
		return user.isActive()
				&& Arrays.asList(user.getRoles()).contains(getRole());
	}

	/**
	 * Protocols are matched using their unique identifiers.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof Protocol))
			return false;
		Protocol them = (Protocol) obj;
		return this.getUniqueIdentifier().equals(them.getUniqueIdentifier());
	}

	@Override
	public int hashCode() {
		return getUniqueIdentifier().hashCode();
	}

	/**
	 * Sames as getUniqueIdentifier().
	 */
	@Override
	public String toString() {
		return getUniqueIdentifier();
	}
}
