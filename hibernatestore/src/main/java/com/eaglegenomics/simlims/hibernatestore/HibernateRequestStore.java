package com.eaglegenomics.simlims.hibernatestore;

import java.io.IOException;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.eaglegenomics.simlims.core.Note;
import com.eaglegenomics.simlims.core.Request;
import com.eaglegenomics.simlims.core.store.RequestStore;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * Uses Hibernate table to store Request entities.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public class HibernateRequestStore extends HibernateDaoSupport implements
		RequestStore {

	@Transactional(readOnly = false)
	public void saveRequest(Request request) throws IOException {
		getHibernateTemplate().saveOrUpdate(request);
	}

	@Transactional(readOnly = false)
	public void saveNote(Note note) throws IOException {
		getHibernateTemplate().saveOrUpdate(note);
	}

	@Transactional(readOnly = true)
	public Request getRequest(long requestId) throws IOException {
		return (Request) getHibernateTemplate().load(Request.class, requestId);
	}

	@Transactional(readOnly = true)
	public Note getNote(long noteId) throws IOException {
		return (Note) getHibernateTemplate().load(Note.class, noteId);
	}
}
