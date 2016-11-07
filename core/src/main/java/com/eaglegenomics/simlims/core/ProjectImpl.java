package com.eaglegenomics.simlims.core;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * A simple implementation of the Project interface with getters/setters all
 * coded and ready to use.
 *
 * @author Richard Holland
 * @since 0.0.1
 */
@Entity
public class ProjectImpl implements Project {

	private static final long serialVersionUID = 1L;
	protected static final Log log = LogFactory.getLog(ProjectImpl.class);
	/**
	 * Use this ID to indicate that a project has not yet been saved, and
	 * therefore does not yet have a unique ID.
	 */
	public static final Long UNSAVED_ID = null;

	private Date creationDate = new Date();
	private String description = "";
	private String name = "";
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long projectId = ProjectImpl.UNSAVED_ID;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
	private Collection<Request> requests = new HashSet<Request>();
	@OneToOne(cascade = CascadeType.ALL)
	private SecurityProfile securityProfile = null;

	public ProjectImpl(User user) {
		setSecurityProfile(new SecurityProfile(user));
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	/**
	 * Internal use only.
	 */
	public Long getProjectId() {
		return projectId;
	}

	public Collection<Request> getRequests() {
		return requests;
	}

	public void setCreationDate(Date date) {
		this.creationDate = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public void setRequests(Collection<Request> requests) {
		this.requests = requests;
	}

	public SecurityProfile getSecurityProfile() {
		return securityProfile;
	}

	public void setSecurityProfile(SecurityProfile profile) {
		this.securityProfile = profile;
	}

	/**
	 * Lets the security profile decide.
	 */
	public boolean userCanRead(User user) {
		return securityProfile.userCanRead(user);
	}

	/**
	 * Lets the security profile decide.
	 */
	public boolean userCanWrite(User user) {
		return securityProfile.userCanWrite(user);
	}

	/**
	 * Only those users who can write to the project can create requests on it.
	 */
	public Request createRequest(User owner) throws SecurityException {
		if (!userCanWrite(owner)) {
			throw new SecurityException();
		}
		Request request = new Request(this, owner);
		getRequests().add(request);
		return request;
	}

	/**
	 * Equivalency is based on getProjectId() if set, otherwise on name,
	 * description and creation date.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof ProjectImpl))
			return false;
		Project them = (Project) obj;
		// If not saved, then compare resolved actual objects. Otherwise
		// just compare IDs.
		if (getProjectId() == ProjectImpl.UNSAVED_ID
				|| them.getProjectId() == ProjectImpl.UNSAVED_ID) {
			return this.getName().equals(them.getName())
					&& this.getDescription().equals(them.getDescription())
					&& this.getCreationDate().equals(them.getCreationDate());
		} else {
			return this.getProjectId() == them.getProjectId();
		}
	}

	@Override
	public int hashCode() {
		if (this.getProjectId() != ProjectImpl.UNSAVED_ID) {
			return this.getProjectId().intValue();
		} else {
			int hashcode = this.getName().hashCode();
			hashcode = 37 * hashcode + this.getDescription().hashCode();
			hashcode = 37 * hashcode + this.getCreationDate().hashCode();
			return hashcode;
		}
	}

	/**
	 * Format is "Date : Name : Description".
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getCreationDate());
		sb.append(" : ");
		sb.append(getName());
		sb.append(" : ");
		sb.append(getDescription());
		return sb.toString();
	}
}