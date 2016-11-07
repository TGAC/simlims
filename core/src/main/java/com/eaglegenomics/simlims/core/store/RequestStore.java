package com.eaglegenomics.simlims.core.store;

import java.io.IOException;

import com.eaglegenomics.simlims.core.Note;
import com.eaglegenomics.simlims.core.Request;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * A request store knows how to save Requests and load them. It can also make
 * notes and attach them to the Request. It should never be used directly - use
 * the RequestManager for all queries.
 * <p>
 * Note that the store provides no methods for directly deleting data. This
 * helps prevent accidental data loss and assists validation.
 * <p>
 * All methods throw IOException in case the backing store has trouble.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public interface RequestStore {

	public Request getRequest(long requestId) throws IOException;

	public void saveNote(Note note) throws IOException;

	public void saveRequest(Request request) throws IOException;

	public Note getNote(long noteId) throws IOException;
}
