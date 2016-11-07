package com.eaglegenomics.simlims.core;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * An abstract data reference that knows when to save and when to update.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public abstract class AbstractDataReference implements DataReference {
	protected static final Log log = LogFactory
			.getLog(AbstractDataReference.class);

	private Class<?> refDataClass;
	private long refId = DataReference.UNSAVED_ID;
	private transient Object temporaryReference = null;

	@SuppressWarnings("unchecked")
	public <C> Class<C> getReferenceClass() {
		return (Class<C>) refDataClass;
	}

	public <C> void setReferenceClass(Class<C> refDataClass) {
		this.refDataClass = refDataClass;
	}

	public long getReferenceId() {
		return refId;
	}

	public void setReferenceId(long refId) {
		this.refId = refId;
	}

	public void update(Object object) {
		assert (object != null);
		if (!object.getClass().equals(getReferenceClass())) {
			throw new IllegalArgumentException(
					"Can not change object to one of a different class.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Replacing contents of DataReference "
					+ getReferenceClass().getName() + "#" + getReferenceId());
		}
		temporaryReference = object;
	}

	public void save() throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("Checking whether to save/update data reference "
					+ getReferenceClass().getName() + "#" + getReferenceId());
		}
		if (temporaryReference != null) {
			if (getReferenceId() == DataReference.UNSAVED_ID) {
				if (log.isDebugEnabled()) {
					log.debug("Saving data reference "
							+ getReferenceClass().getName() + "#"
							+ getReferenceId());
				}
				setReferenceId(doSaveReference(temporaryReference));
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Updating data reference "
							+ getReferenceClass().getName() + "#"
							+ getReferenceId());
				}
				doUpdateReference(temporaryReference);
			}
			temporaryReference = null;
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Save/update not required for data reference "
						+ getReferenceClass().getName() + "#"
						+ getReferenceId());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Object resolve() throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("Resolving data reference "
					+ getReferenceClass().getName() + "#" + getReferenceId());
		}
		if (temporaryReference != null) {
			if (log.isDebugEnabled()) {
				log.debug("Resolved data reference internally for "
						+ getReferenceClass().getName() + "#"
						+ getReferenceId());
			}
			return temporaryReference;
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Using backing store to resolve data reference "
						+ getReferenceClass().getName() + "#"
						+ getReferenceId());
			}
			return doResolveReference();
		}
	}

	/**
	 * Implementing classes should override this method to create a new data
	 * object in the backing store and assign a unique ID to it. The unique ID
	 * should be returned as the result of the method.
	 * <p>
	 * This method is called by the abstract implementation of {@link #save()},
	 * which uses the return value to call {@link #setReferenceId()}.
	 * <p>
	 * This method is NOT called to update existing pieces of data.
	 * {@link #doUpdateReference()} is used for that.
	 */
	public abstract Long doSaveReference(Object data) throws IOException;

	/**
	 * Implementing classes should use this to update the data object for the
	 * reference ID in this data reference. This method stores the updated data
	 * to the backing store.
	 */
	public abstract void doUpdateReference(Object data) throws IOException;

	/**
	 * Implementing classes should use this to resolve the data reference and
	 * load the actual data object from the backing store.
	 */
	public abstract Object doResolveReference() throws IOException;

	/**
	 * References match if their IDs match, otherwise if their resolved objects
	 * return true when compared with equals().
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof AbstractDataReference))
			return false;
		AbstractDataReference them = (AbstractDataReference) obj;
		// If not saved, then compare resolved actual objects. Otherwise
		// just compare IDs and classes.
		if (getReferenceId() == DataReference.UNSAVED_ID
				|| them.getReferenceId() == DataReference.UNSAVED_ID) {
			try {
				return this.resolve().equals(them.resolve());
			} catch (IOException ex) {
				if (log.isErrorEnabled()) {
					log
							.error(
									"Failed to resolve data reference in equals() method.",
									ex);
				}
				// Pretend they don't match by default, safer that way.
				return false;
			}
		} else {
			return this.getReferenceClass().equals(them.getReferenceClass())
					&& this.getReferenceId() == them.getReferenceId();
		}
	}

	@Override
	public int hashCode() {
		long hashcode = getReferenceClass().hashCode();
		hashcode = 37 * hashcode + getReferenceId();
		return (int) hashcode;
	}

	/**
	 * Format is "Class#ID", or "Class#(unsaved)" for unsaved references.
	 */
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(getReferenceClass().getName());
		buff.append('#');
		buff.append(getReferenceId() == DataReference.UNSAVED_ID ? "(unsaved)"
				: getReferenceId());
		return buff.toString();
	}
}
