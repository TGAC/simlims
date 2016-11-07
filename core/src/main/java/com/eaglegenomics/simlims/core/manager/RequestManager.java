package com.eaglegenomics.simlims.core.manager;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.eaglegenomics.simlims.core.*;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p/>
 * This manager knows how to locate and save all storable data object instances.
 *
 * @author Richard Holland
 * @since 0.0.1
 *
 * @author Rob Davey
 * @since 0.0.2
 */
public interface RequestManager {

//SAVES
  public void saveRequest(Request request) throws IOException;

  public long saveProject(Project project) throws IOException;

  public long saveSecurityProfile(SecurityProfile profile) throws IOException;

//GETS
  public Note getNoteById(long noteId) throws IOException;

  public Project getProjectById(long projectId) throws IOException;

  public Request getRequestById(long requestId) throws IOException;

//LISTS
  /**
   * Obtain a list of all the projects the user has access to. Access is
   * defined as either read or write access.
   */
  public Collection<Project> listAllAccessibleProjects(User user) throws IOException;
  public Collection<Project> listAllProjects() throws IOException;

  /**
   * Obtain a list of all the requests the user has access to within this
   * project. Access is defined as either read or write access.
   */
  public Collection<Request> listAllAccessibleRequests(User user, Project project) throws IOException;
}
