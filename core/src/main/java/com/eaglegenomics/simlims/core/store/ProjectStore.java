package com.eaglegenomics.simlims.core.store;

import com.eaglegenomics.simlims.core.Project;
import java.io.IOException;
import java.util.Collection;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * A project store knows how to find and load Project instances.
 * <p>
 * Note that the store provides no methods for directly deleting data. This
 * helps prevent accidental data loss and is important for validation and
 * provenance.
 * <p>
 * IOExceptions are thrown by all methods in case of problems with the backing
 * store.
 *
 * @author Richard Holland
 * @since 0.0.1
 */
public interface ProjectStore {

	public void save(Project project) throws IOException;

	public Project get(long projectId) throws IOException;

	public Collection<Project> listAll() throws IOException;
}
