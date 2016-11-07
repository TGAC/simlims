package com.eaglegenomics.simlims.hibernatestore;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * Only Hibernateable objects can be stored in DataReferences in a
 * HibernateDataReferenceStore.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public interface Hibernateable {
	/**
	 * A method for determining the Hibernate object ID. This should return the
	 * primary key for existing objects, or null for new objects. The object
	 * itself should have Hibernate mappings defined and loaded in the same
	 * Hibernate config as for the main SimLIMS setup.
	 */
	public Long getHibernateId();
}
