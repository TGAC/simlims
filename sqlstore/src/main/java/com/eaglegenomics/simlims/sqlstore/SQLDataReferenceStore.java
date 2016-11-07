package com.eaglegenomics.simlims.sqlstore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import com.eaglegenomics.simlims.core.AbstractDataReference;
import com.eaglegenomics.simlims.core.DataReference;
import com.eaglegenomics.simlims.core.store.DataReferenceStore;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * Uses a simple SQL database table to store DataReference entries. This is only
 * able to store DataReference objects that refer to Serializable data. Anything
 * that is not Serializable cannot be stored.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public class SQLDataReferenceStore implements DataReferenceStore {
	protected static final Log log = LogFactory
			.getLog(SQLDataReferenceStore.class);

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		assert (dataSource != null);
		this.dataSource = dataSource;
	}

	@SuppressWarnings("unchecked")
	public DataReference create(Object data) throws IllegalArgumentException,
			IOException {
		DataReference dataRef = new SQLDataReference();
		dataRef.setReferenceClass((Class) data.getClass());
		dataRef.update(data);
		dataRef.setReferenceId(DataReference.UNSAVED_ID);
		return dataRef;
	}

	public <C> DataReference get(Long refId, Class<C> refClass) {
		DataReference dataRef = new SQLDataReference();
		dataRef.setReferenceId(refId);
		dataRef.setReferenceClass(refClass);
		return dataRef;
	}

	private class SQLDataReference extends AbstractDataReference {
		@Override
		public void update(Object data) {
			if (!(data instanceof Serializable)) {
				throw new IllegalArgumentException(
						"Can only store Serializable objects.");
			}
			super.update(data);
		}

		@Transactional(readOnly = true)
		@Override
		public Object doResolveReference() throws IOException {
			if (log.isDebugEnabled()) {
				log.debug("Resolving reference "
						+ getReferenceClass().getName() + "#"
						+ getReferenceId());
			}
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				conn = dataSource.getConnection();
				stmt = conn
						.prepareStatement("select ref_data from simlims_dataref where ref_id=?");
				stmt.setLong(1, getReferenceId());
				rs = stmt.executeQuery();
				rs.next();
				return deserialize(rs.getBytes(1));
			} catch (Exception ex) {
				if (log.isDebugEnabled()) {
					log.debug("Encountered exception whilst resolving", ex);
				}
				try {
					if (rs != null) {
						rs.close();
					}
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException ex2) {
					if (log.isDebugEnabled()) {
						log.debug("Unimportant SQLException", ex2);
					}
				}
				IOException ioex = new IOException(
						"Unable to resolve DataReference("
								+ this.getClass().getName() + ")#"
								+ this.getReferenceId());
				ioex.initCause(ex);
				throw ioex;
			}
		}

		@Transactional(readOnly = false)
		@Override
		public Long doSaveReference(Object data) throws IOException {
			if (log.isDebugEnabled()) {
				log.debug("Saving reference " + getReferenceClass().getName()
						+ "#" + getReferenceId());
			}
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				conn = dataSource.getConnection();
				stmt = conn
						.prepareStatement(
								"insert into simlims_dataref (ref_class, ref_data) values (?,?)",
								Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, getReferenceClass().getName());
				stmt.setBytes(2, serialize(data));
				stmt.executeUpdate();
				rs = stmt.getGeneratedKeys();
				if (!rs.next()) {
					throw new SQLException(
							"Failed to generate auto ID for new data reference.");
				}
				long newId = rs.getLong(1);
				if (log.isDebugEnabled()) {
					log.debug("Generated ID " + newId);
				}
				return newId;
			} catch (Exception ex) {
				if (log.isDebugEnabled()) {
					log.debug("Encountered exception whilst saving", ex);
				}
				try {
					if (rs != null) {
						rs.close();
					}
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException ex2) {
					if (log.isDebugEnabled()) {
						log.debug("Unimportant SQLException", ex2);
					}
				}
				IOException ioex = new IOException(
						"Unable to save DataReference("
								+ this.getClass().getName() + ")#"
								+ this.getReferenceId());
				ioex.initCause(ex);
				throw ioex;
			}
		}

		@Transactional(readOnly = false)
		@Override
		public void doUpdateReference(Object data) throws IOException {
			if (log.isDebugEnabled()) {
				log.debug("Updating reference " + getReferenceClass().getName()
						+ "#" + getReferenceId());
			}
			Connection conn = null;
			PreparedStatement stmt = null;
			try {
				conn = dataSource.getConnection();
				stmt = conn
						.prepareStatement("update simlims_dataref set ref_data=? where ref_id=?");
				stmt.setBytes(1, serialize(data));
				stmt.setLong(2, getReferenceId());
				stmt.executeUpdate();
			} catch (Exception ex) {
				if (log.isDebugEnabled()) {
					log.debug("Encountered exception whilst updating", ex);
				}
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException ex2) {
					if (log.isDebugEnabled()) {
						log.debug("Unimportant SQLException", ex2);
					}
				}
				IOException ioex = new IOException(
						"Unable to update DataReference("
								+ this.getClass().getName() + ")#"
								+ this.getReferenceId());
				ioex.initCause(ex);
				throw ioex;
			}
		}

		private byte[] serialize(Object data) throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			try {
				out.writeObject(data);
				return bos.toByteArray();
			} finally {
				out.close();
				bos.close();
			}
		}

		private Object deserialize(byte[] serialized) throws IOException,
				ClassNotFoundException {
			ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
			ObjectInputStream in = new ObjectInputStream(bis);
			try {
				return getReferenceClass().cast(in.readObject());
			} finally {
				in.close();
				bis.close();
			}
		}
	}
}
