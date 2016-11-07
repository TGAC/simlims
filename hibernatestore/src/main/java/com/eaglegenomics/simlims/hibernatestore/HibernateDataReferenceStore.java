package com.eaglegenomics.simlims.hibernatestore;

import java.io.IOException;

import org.hibernate.Hibernate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.eaglegenomics.simlims.core.AbstractDataReference;
import com.eaglegenomics.simlims.core.DataReference;
import com.eaglegenomics.simlims.core.store.DataReferenceStore;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * Uses Hibernate table to store DataReference entries. This is only able to
 * store objects that implement the associated Hibernateable interface, and are
 * themselves related to Hibernate entities (whether by annotation or by XML
 * config - as long as Hibernate can map them using the class names specified).
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public class HibernateDataReferenceStore extends HibernateDaoSupport implements
		DataReferenceStore {

	@SuppressWarnings("unchecked")
	public DataReference create(Object data) throws IllegalArgumentException,
			IOException {
		DataReference dataRef = new HibernateDataReference();
		dataRef.setReferenceClass(Hibernate.getClass(data));
		dataRef.update(data);
		Long refId = ((Hibernateable) data).getHibernateId();
		dataRef.setReferenceId(refId == null ? DataReference.UNSAVED_ID : refId);
		return dataRef;
	}

	public <C> DataReference get(Long refId, Class<C> refClass) {
		DataReference dataRef = new HibernateDataReference();
		dataRef.setReferenceId(refId);
		dataRef.setReferenceClass(refClass);
		return dataRef;
	}

	private class HibernateDataReference extends AbstractDataReference {
		@Override
		public void update(Object data) {
			if (!(data instanceof Hibernateable)) {
				throw new IllegalArgumentException(
						"Can only store Hibernateable objects.");
			}
			super.update(data);
		}

		@Transactional(readOnly = true)
		@Override
		public Object doResolveReference() throws IOException {
			return getReferenceClass().cast(
					getHibernateTemplate().load(getReferenceClass(),
							getReferenceId()));
		}

		@Transactional(readOnly = false)
		@Override
		public Long doSaveReference(Object data) throws IOException {
			getHibernateTemplate().save(data);
			return ((Hibernateable) data).getHibernateId();
		}

		@Transactional(readOnly = false)
		@Override
		public void doUpdateReference(Object data) throws IOException {
			if (((Hibernateable) data).getHibernateId() != getReferenceId()) {
				throw new IOException(
						"Hibernate ID must match getReferenceId() - DataReference("
								+ this.getClass().getName() + ")#"
								+ this.getReferenceId());
			}
			getHibernateTemplate().update(data);
		}
	}
}
