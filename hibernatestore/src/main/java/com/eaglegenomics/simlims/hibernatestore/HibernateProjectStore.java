package com.eaglegenomics.simlims.hibernatestore;

import java.io.IOException;
import java.util.Collection;

import com.eaglegenomics.simlims.core.ProjectImpl;
import com.eaglegenomics.simlims.core.store.Store;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.eaglegenomics.simlims.core.Project;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * Uses Hibernate table to store Project entities.
 * 
 * @author Richard Holland
 * @since 0.0.1
 *
 * @author Rob Davey
 * @since 0.0.2
 */
public class HibernateProjectStore extends HibernateDaoSupport implements Store<Project> {

	@Transactional(readOnly = false)
	public long save(Project project) throws IOException {
      getHibernateTemplate().saveOrUpdate(project);
      return project.getProjectId();
	}

	@Transactional(readOnly = true)
	public Project get(long projectId) throws IOException {
		return (Project) getHibernateTemplate().load(ProjectImpl.class, projectId);
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public Collection<Project> listAll() throws IOException {
		return (Collection<Project>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createQuery("from Project").list();
					}
				});
	}
}
