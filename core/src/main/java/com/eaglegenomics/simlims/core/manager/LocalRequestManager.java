package com.eaglegenomics.simlims.core.manager;

import java.io.IOException;
import java.util.*;

import com.eaglegenomics.simlims.core.*;
import com.eaglegenomics.simlims.core.store.*;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p/>
 * Basic implementation using local stores. More complex implementations may
 * choose to use web services to communicate with a remote store, or to combine
 * multiple stores.
 *
 * @author Richard Holland
 * @author Rob Davey
 * @since 0.0.2
 */
public class LocalRequestManager extends AbstractRequestManager {
  private RequestStore requestStore;
  private Store<Project> projectStore;
  private Store<Note> noteStore;
  private Store<SecurityProfile> securityProfileStore;

  public void setRequestStore(RequestStore requestStore) {
    this.requestStore = requestStore;
  }

  public void setProjectStore(Store<Project> projectStore) {
    this.projectStore = projectStore;
  }

  public void setNoteStore(Store<Note> noteStore) {
    this.noteStore = noteStore;
  }

  public void setSecurityProfileStore(Store<SecurityProfile> securityProfileStore) {
    this.securityProfileStore = securityProfileStore;
  }

  public Collection<Project> listAllAccessibleProjects(User user) throws IOException {
    if (projectStore != null) {
      Collection<Project> accessibleProjects = new HashSet<Project>();
      for (Project project : projectStore.listAll()) {
        if (project.userCanRead(user)) {
          accessibleProjects.add(project);
        }
      }
      return accessibleProjects;
    }
    else {
      throw new IOException("No projectStore available. Check that it has been declared in the Spring config.");
    }
  }

  public Collection<Project> listAllProjects() throws IOException {
    if (projectStore != null) {
      Collection<Project> accessibleProjects = new HashSet<Project>();
      for (Project project : projectStore.listAll()) {
          accessibleProjects.add(project);
      }
      return accessibleProjects;
    }
    else {
      throw new IOException("No projectStore available. Check that it has been declared in the Spring config.");
    }
  }


  public Collection<Request> listAllAccessibleRequests(User user, Project project) throws IOException {
    Collection<Request> accessibleRequests = new HashSet<Request>();
    for (Request request : project.getRequests()) {
      if (request.userCanRead(user)) {
        accessibleRequests.add(request);
      }
    }
    return accessibleRequests;
  }

//SAVES  
  public void saveRequest(Request request) throws IOException {
    if (requestStore != null) {
      requestStore.saveRequest(request);
    }
    else {
      throw new IOException("No requestStore available. Check that it has been declared in the Spring config.");
    }
  }

  public long saveProject(Project project) throws IOException {
    if (projectStore != null) {
      return projectStore.save(project);
    }
    else {
      throw new IOException("No projectStore available. Check that it has been declared in the Spring config.");
    }
  }

  public long saveSecurityProfile(SecurityProfile profile) throws IOException {
    if (securityProfileStore != null) {
      return securityProfileStore.save(profile);
    }
    else {
      throw new IOException("No securityProfileStore available. Check that it has been declared in the Spring config.");
    }
  }

//GETS

  public Project getProjectById(long projectId) throws IOException {
    if (projectStore != null) {
      return projectStore.get(projectId);
    }
    else {
      throw new IOException("No projectStore available. Check that it has been declared in the Spring config.");
    }
  }

  public Request getRequestById(long requestId) throws IOException {
    if (requestStore != null) {
      return requestStore.getRequest(requestId);
    }
    else {
      throw new IOException("No requestStore available. Check that it has been declared in the Spring config.");
    }
  }

  public Note getNoteById(long noteId) throws IOException {
    if (noteStore != null) {
      return noteStore.get(noteId);
    }
    else {
      throw new IOException("No noteStore available. Check that it has been declared in the Spring config.");
    }
  }
}
