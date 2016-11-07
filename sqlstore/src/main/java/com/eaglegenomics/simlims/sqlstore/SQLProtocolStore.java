package com.eaglegenomics.simlims.sqlstore;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.eaglegenomics.simlims.core.Activity;
import com.eaglegenomics.simlims.core.ActivityData;
import com.eaglegenomics.simlims.core.ActivityDataPriority;
import com.eaglegenomics.simlims.core.BasicActivityData;
import com.eaglegenomics.simlims.core.DataReference;
import com.eaglegenomics.simlims.core.Request;
import com.eaglegenomics.simlims.core.User;
import com.eaglegenomics.simlims.core.manager.ProtocolManager;
import com.eaglegenomics.simlims.core.store.DataReferenceStore;
import com.eaglegenomics.simlims.core.store.ProtocolStore;
import com.eaglegenomics.simlims.core.store.RequestStore;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * Stores Protocol data in simple SQL tables.
 * <p>
 * TODO Make this much more efficient. Table designs need reviewing, as does
 * associated SQL.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public class SQLProtocolStore implements ProtocolStore {
	protected static final Logger log = LoggerFactory.getLogger(SQLProtocolStore.class);

	private DataSource dataSource;
	private DataReferenceStore dataReferenceStore;
	private ProtocolManager protocolManager;
	private RequestStore requestStore;

	public void setRequestStore(RequestStore requestStore) {
		this.requestStore = requestStore;
	}

	public void setDataReferenceStore(DataReferenceStore dataReferenceStore) {
		this.dataReferenceStore = dataReferenceStore;
	}

	public void setProtocolManager(ProtocolManager protocolManager) {
		this.protocolManager = protocolManager;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public Collection<ActivityData> getLockableInputData(User user,
			Activity activity) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("Loading lockable input data for user "
					+ user.getLoginName() + " activity "
					+ activity.getUniqueIdentifier());
		}
		Connection conn = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		try {
			conn = dataSource.getConnection();
			stmt1 = conn
					.prepareStatement("select creationDate,dataRefId,dataRefClass,priority,actInputId,actAlias from simlims_act_input where lockDate is null and processed=false and actUniqueId=?");
			stmt2 = conn
					.prepareStatement("select dataIndex,requestId,executionCount from simlims_act_input_entry where actInputId=?");
			stmt1.setString(1, activity.getUniqueIdentifier());
			rs1 = stmt1.executeQuery();
			Collection<ActivityData> inputs = new ArrayList<ActivityData>();
			while (rs1.next()) {
				if (log.isDebugEnabled()) {
					log.debug("Processing lockable input data.");
				}
				ActivityData input = new BasicActivityData();
				input.setCreationDate(new Date(rs1.getDate(1).getTime()));
				input.setDataReference((DataReference) dataReferenceStore
						.get(rs1.getLong(2), (Class) Class.forName(rs1
								.getString(3))));
				input.setPriority(ActivityDataPriority
						.valueOf(rs1.getString(4)));
				input.setUniqueId(rs1.getLong(5));
				input.setActivityAlias(rs1.getString(6));
				input.setActivity(activity);
				stmt2.setLong(1, input.getUniqueId());
				rs2 = stmt2.executeQuery();
				while (rs2.next()) {
					if (log.isDebugEnabled()) {
						log.debug("Processing lockable input data entry.");
					}
					ActivityData.Entry inputEntry = input.createEntry();
					inputEntry.setRequest(requestStore.getRequest(rs2
							.getLong(2)));
					inputEntry.setExecutionCount(rs2.getInt(3));
					inputEntry.setProtocol(protocolManager
							.getProtocol(inputEntry.getRequest()
									.getProtocolUniqueIdentifier()));
					if (inputEntry.userCanWrite(user)) {
						if (log.isDebugEnabled()) {
							log.debug("Entry accepted.");
						}
						input.getIndexedEntries().put(rs2.getString(1),
								inputEntry);
					}
				}
				if (log.isDebugEnabled()) {
					log.debug("Lockable input data has "
							+ input.getIndexedEntries().size() + " entries.");
				}
				if (!input.getIndexedEntries().isEmpty()) {
					if (log.isDebugEnabled()) {
						log.debug("Input accepted.");
					}
					inputs.add(input);
				}
			}
			if (log.isDebugEnabled()) {
				log.debug("Total lockable data size: " + inputs.size());
			}
			return inputs;
		} catch (Exception ex) {
			if (log.isDebugEnabled()) {
				log.debug("Problem loading lockable data.", ex);
			}
			if (ex instanceof IOException) {
				throw (IOException) ex;
			} else {
				throw new IOException(ex);
			}
		} finally {
			try {
				if (rs1 != null) {
					rs1.close();
				}
				if (stmt1 != null) {
					stmt1.close();
				}
				if (rs2 != null) {
					rs2.close();
				}
				if (stmt2 != null) {
					stmt2.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex2) {
				if (log.isDebugEnabled()) {
					log.debug("Unimportant SQL error", ex2);
				}
			}
		}
	}

	@Transactional(readOnly = false)
	public boolean lockInputData(User user, ActivityData input)
			throws SecurityException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("Locking data " + input.getUniqueId() + " for "
					+ user.getLoginName());
		}
		if (!input.userCanWrite(user)) {
			throw new SecurityException();
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn
					.prepareStatement("update simlims_act_input set lockDate=?, lockUserLoginName=? where lockDate is null and processed=false and actInputId=?");
			stmt.setDate(1, new java.sql.Date(new Date().getTime()));
			stmt.setString(2, user.getLoginName());
			stmt.setLong(3, input.getUniqueId());
			boolean locked = stmt.executeUpdate() == 1;
			if (log.isDebugEnabled()) {
				log.debug("Locked data " + input.getUniqueId() + " for "
						+ user.getLoginName() + "?: " + locked);
			}
			return locked;
		} catch (SQLException ex) {
			if (log.isDebugEnabled()) {
				log.debug("Failed to lock data.", ex);
			}
			throw new IOException(ex);
		} finally {
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
					log.debug("Unimportant SQL exception", ex2);
				}
			}
		}
	}

	@Transactional(readOnly = false)
	public void unlockInputData(User user, ActivityData input)
			throws IOException, SecurityException {
		if (log.isDebugEnabled()) {
			log.debug("Unlocking data " + input.getUniqueId() + " for "
					+ user.getLoginName());
		}
		if (!validateInputDataLock(user, input)) {
			return;
		}
		if (!input.userCanWrite(user)) {
			throw new SecurityException();
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn
					.prepareStatement("update simlims_act_input set lockDate=null, lockUserLoginName=null where actInputId=? and lockUserLoginName=? and lockDate is not null");
			stmt.setLong(1, input.getUniqueId());
			stmt.setString(2, user.getLoginName());
			boolean unlocked = stmt.executeUpdate() == 1;
			if (log.isDebugEnabled()) {
				log.debug("Unlocked data " + input.getUniqueId() + " for "
						+ user.getLoginName() + "?: " + unlocked);
			}
		} catch (Exception ex) {
			if (log.isDebugEnabled()) {
				log.debug("Failed to unlock data.", ex);
			}
		} finally {
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
					log.debug("Unimportant SQL exception", ex2);
				}

			}
		}
	}

	@Transactional(readOnly = true)
	public boolean validateInputDataLock(User user, ActivityData input)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("Validating locked data " + input.getUniqueId() + " for "
					+ user.getLoginName());
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn
					.prepareStatement("select lockUserLoginName from simlims_act_input where actInputId=?");
			stmt.setLong(1, input.getUniqueId());
			rs = stmt.executeQuery();
			boolean locked = rs.next()
					&& user.getLoginName().equals(rs.getString(1));
			if (log.isDebugEnabled()) {
				log.debug("Data is locked " + input.getUniqueId() + " for "
						+ user.getLoginName() + "?: " + locked);
			}
			return locked;
		} catch (SQLException ex) {
			if (log.isDebugEnabled()) {
				log.debug("Problem checking locked data.", ex);
			}
			throw new IOException(ex);
		} finally {
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
					log.debug("Unimportant SQL exception", ex2);
				}
			}
		}
	}

	@Transactional(readOnly = false)
	public void queueNewInputData(ActivityData inputData) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("Queuing input data " + inputData.getUniqueId());
		}
		Connection conn = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		try {
			conn = dataSource.getConnection();
			// Auto-generate actInputId - sequence or auto-increment
			stmt1 = conn
					.prepareStatement(
							"insert into simlims_act_input (creationDate,dataRefId,dataRefClass,priority,actAlias,processed,actUniqueId) values (?,?,?,?,?,false,?)",
							Statement.RETURN_GENERATED_KEYS);
			stmt2 = conn
					.prepareStatement("insert into simlims_act_input_entry (actInputId,dataIndex,requestId,executionCount) values (?,?,?,?)");
			DataReference dataRef = inputData.getDataReference();
			stmt1.setDate(1, new java.sql.Date(inputData.getCreationDate()
					.getTime()));
			stmt1.setLong(2, dataRef.getReferenceId());
			stmt1.setString(3, dataRef.getReferenceClass().getCanonicalName());
			stmt1.setString(4, inputData.getPriority().toString());
			stmt1.setString(5, inputData.getActivityAlias());
			stmt1.setString(6, inputData.getActivity().getUniqueIdentifier());
			stmt1.executeUpdate();
			ResultSet keys = stmt1.getGeneratedKeys();
			keys.next();
			inputData.setUniqueId(keys.getLong(1));
			for (Map.Entry<String, ActivityData.Entry> entry : inputData
					.getIndexedEntries().entrySet()) {
				stmt2.setLong(1, inputData.getUniqueId());
				stmt2.setString(2, entry.getKey());
				stmt2.setLong(3, entry.getValue().getRequest().getRequestId());
				stmt2.setInt(4, entry.getValue().getExecutionCount());
				stmt2.executeUpdate();
			}
			if (log.isDebugEnabled()) {
				log.debug("Queued input");
			}
		} catch (SQLException ex) {
			if (log.isDebugEnabled()) {
				log.debug("Failed to queue input.", ex);
			}
			throw new IOException(ex);
		} finally {
			try {
				if (stmt1 != null) {
					stmt1.close();
				}
				if (stmt2 != null) {
					stmt2.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex2) {
				if (log.isDebugEnabled()) {
					log.debug("Unimportant SQL exception", ex2);
				}
			}
		}
	}

	@Transactional(readOnly = false)
	public void saveFailedData(User user, ActivityData input, Throwable error)
			throws IOException, SecurityException {
		if (log.isDebugEnabled()) {
			log.debug("Saving failed data " + input.getUniqueId() + " for "
					+ user.getLoginName());
		}
		if (!input.userCanWrite(user)) {
			throw new SecurityException();
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			error.printStackTrace(pw);
			pw.flush();
			sw.flush();
			conn = dataSource.getConnection();
			stmt = conn
					.prepareStatement("insert into simlims_act_failed (actInputId,errorMsg) values (?,?)");
			stmt.setLong(1, input.getUniqueId());
			stmt.setString(2, sw.toString());
			stmt.executeUpdate();
			stmt.close();
			stmt = conn
					.prepareStatement("update simlims_act_input set processed=true where actInputId=?");
			stmt.setLong(1, input.getUniqueId());
			stmt.executeUpdate();
			if (log.isDebugEnabled()) {
				log.debug("Failed data saved.");
			}
		} catch (SQLException ex) {
			if (log.isDebugEnabled()) {
				log.debug("Failed data failed to save.", ex);
			}
			throw new IOException(ex);
		} finally {
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
					log.debug("Unimportant SQL exception", ex2);
				}
			}
		}
	}

	@Transactional(readOnly = false)
	public void saveOutputData(
			User user,
			Collection<ActivityData> inputDataEntities,
			Collection<ActivityData> outputDataEntities,
			Map<Map.Entry<String, ActivityData>, Collection<Map.Entry<String, ActivityData>>> outputData)
			throws IOException, SecurityException {
		if (log.isDebugEnabled()) {
			log.debug("Saving output for user " + user.getLoginName());
		}
		for (ActivityData input : inputDataEntities) {
			if (!input.userCanWrite(user)) {
				throw new SecurityException();
			}
		}
		Connection conn = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		try {
			conn = dataSource.getConnection();
			// Store a new queued input, or a result.
			stmt1 = conn
					.prepareStatement(
							"insert into simlims_act_result (creationDate,dataRefId,dataRefClass,priority,actAlias) values (?,?,?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
			stmt2 = conn
					.prepareStatement("insert into simlims_act_result_entry (actResultId,dataIndex,requestId,executionCount) values (?,?,?,?)");
			for (ActivityData outputDataEntity : outputDataEntities) {
				if (outputDataEntity.getActivityAlias().equals(
						ProtocolStore.RESULT_ACTIVITY_ALIAS)) {
					if (log.isDebugEnabled()) {
						log.debug("Output is a result");
					}
					// Auto-generate actInputId - sequence or auto-increment
					DataReference dataRef = outputDataEntity.getDataReference();
					stmt1.setDate(1, new java.sql.Date(outputDataEntity
							.getCreationDate().getTime()));
					stmt1.setLong(2, dataRef.getReferenceId());
					stmt1.setString(3, dataRef.getReferenceClass()
							.getCanonicalName());
					stmt1.setString(4, outputDataEntity.getPriority()
							.toString());
					stmt1.setString(5, outputDataEntity.getActivityAlias());
					stmt1.executeUpdate();
					ResultSet keys = stmt1.getGeneratedKeys();
					keys.next();
					outputDataEntity.setUniqueId(keys.getLong(1));
					for (Map.Entry<String, ActivityData.Entry> entry : outputDataEntity
							.getIndexedEntries().entrySet()) {
						stmt2.setLong(1, outputDataEntity.getUniqueId());
						stmt2.setString(2, entry.getKey());
						stmt2.setLong(3, entry.getValue().getRequest()
								.getRequestId());
						stmt2.setInt(4, entry.getValue().getExecutionCount());
						stmt2.executeUpdate();
					}
				} else {
					if (log.isDebugEnabled()) {
						log
								.debug("Output is new data - delegating to queue new input data");
					}
					queueNewInputData(outputDataEntity);
				}
			}
			// For each outputDataMap entry, create a row in output map
			// table for each collection member.
			stmt1.close();
			stmt1 = conn
					.prepareStatement("insert into simlims_act_output_map(actInputId,actOutputId,actResultId,inputIdx,outputIdx) values (?,?,?,?,?)");
			if (log.isDebugEnabled()) {
				log.debug("Saving output mappings");
			}
			for (Map.Entry<Map.Entry<String, ActivityData>, Collection<Map.Entry<String, ActivityData>>> entry : outputData
					.entrySet()) {
				stmt1.setLong(1, entry.getKey().getValue().getUniqueId());
				stmt1.setString(4, entry.getKey().getKey());
				for (Map.Entry<String, ActivityData> outEntry : entry
						.getValue()) {
					stmt1.setString(5, outEntry.getKey());
					long outputId = outEntry.getValue().getUniqueId();
					boolean outputIsResult = outEntry.getValue()
							.getActivityAlias().equals(
									ProtocolStore.RESULT_ACTIVITY_ALIAS);
					stmt1.setLong(outputIsResult ? 2 : 3,
							ActivityData.UNSAVED_ID);
					stmt1.setLong(outputIsResult ? 3 : 2, outputId);
					stmt1.executeUpdate();
				}
			}
			// Set input processed=true
			stmt1.close();
			stmt1 = conn
					.prepareStatement("update simlims_act_input set processed=true where actInputId=?");
			if (log.isDebugEnabled()) {
				log.debug("Setting processed=true on input");
			}
			for (ActivityData inputDataEntity : inputDataEntities) {
				stmt1.setLong(1, inputDataEntity.getUniqueId());
				stmt1.executeUpdate();
			}
		} catch (SQLException ex) {
			if (log.isDebugEnabled()) {
				log.debug("Problem saving output data.", ex);
			}
			throw new IOException(ex);
		} finally {
			try {
				if (stmt1 != null) {
					stmt1.close();
				}
				if (stmt2 != null) {
					stmt2.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex2) {
				if (log.isDebugEnabled()) {
					log.debug("Unimportant SQL exception", ex2);
				}
			}
		}
	}

	@Transactional(readOnly = false)
	public void saveSkippedData(User user, ActivityData input)
			throws IOException, SecurityException {
		if (log.isDebugEnabled()) {
			log.debug("Saving skipped data " + input.getUniqueId() + " for "
					+ user.getLoginName());
		}
		if (!input.userCanWrite(user)) {
			throw new SecurityException();
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn
					.prepareStatement("insert into simlims_act_skipped (actInputId) values (?)");
			stmt.setLong(1, input.getUniqueId());
			stmt.executeUpdate();
			stmt.close();
			if (log.isDebugEnabled()) {
				log.debug("Updating input to processed=true");
			}
			stmt = conn
					.prepareStatement("update simlims_act_input set processed=true where actInputId=?");
			stmt.setLong(1, input.getUniqueId());
			stmt.executeUpdate();
		} catch (SQLException ex) {
			if (log.isDebugEnabled()) {
				log.debug("Problem saving skipped data", ex);
			}
			throw new IOException(ex);
		} finally {
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
					log.debug("Unimportant SQL exception", ex2);
				}
			}
		}
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public Collection<ActivityData> getRequestResults(User user,
			Request request, int executionCount) throws SecurityException,
			IOException {
		if (log.isDebugEnabled()) {
			log.debug("Getting request results for user " + user.getLoginName()
					+ " for request " + request.getRequestId() + " execution "
					+ executionCount);
		}
		if (!request.userCanRead(user)) {
			throw new SecurityException();
		}
		Connection conn = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		try {
			conn = dataSource.getConnection();
			stmt1 = conn
					.prepareStatement("select creationDate,dataRefId,dataRefClass,priority,actResultId,actAlias from simlims_act_result");
			stmt2 = conn
					.prepareStatement("select dataIndex,requestId,executionCount from simlims_act_result_entry where actResultId=? and requestId=? and executionCount=?");
			rs1 = stmt1.executeQuery();
			Collection<ActivityData> results = new ArrayList<ActivityData>();
			while (rs1.next()) {
				if (log.isDebugEnabled()) {
					log.debug("Found result");
				}
				ActivityData result = new BasicActivityData();
				result.setCreationDate(new Date(rs1.getDate(1).getTime()));
				result.setDataReference(dataReferenceStore.get(rs1.getLong(2),
						(Class<Object>) Class.forName(rs1.getString(3))));
				result.setPriority(ActivityDataPriority.valueOf(rs1
						.getString(4)));
				result.setUniqueId(rs1.getLong(5));
				result.setActivityAlias(rs1.getString(6));
				stmt2.setLong(1, result.getUniqueId());
				stmt2.setLong(2, request.getRequestId());
				stmt2.setInt(3, executionCount);
				rs2 = stmt2.executeQuery();
				while (rs2.next()) {
					if (log.isDebugEnabled()) {
						log.debug("Found result entry");
					}
					ActivityData.Entry resultEntry = result.createEntry();
					resultEntry.setRequest(requestStore.getRequest(rs2
							.getLong(2)));
					resultEntry.setExecutionCount(rs2.getInt(3));
					if (resultEntry.userCanRead(user)) {
						if (log.isDebugEnabled()) {
							log.debug("Accepted result entry");
						}
						result.getIndexedEntries().put(rs2.getString(1),
								resultEntry);
					}
				}
				if (!result.getIndexedEntries().isEmpty()) {
					if (log.isDebugEnabled()) {
						log.debug("Accepted result");
					}
					results.add(result);
				}
			}
			if (log.isDebugEnabled()) {
				log.debug("Results returned: " + results.size());
			}
			return results;
		} catch (Exception ex) {
			if (log.isDebugEnabled()) {
				log.debug("Problem getting results.", ex);
			}
			if (ex instanceof IOException) {
				throw (IOException) ex;
			} else {
				throw new IOException(ex);
			}
		} finally {
			try {
				if (rs1 != null) {
					rs1.close();
				}
				if (stmt1 != null) {
					stmt1.close();
				}
				if (rs2 != null) {
					rs2.close();
				}
				if (stmt2 != null) {
					stmt2.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex2) {
				if (log.isDebugEnabled()) {
					log.debug("Unimportant SQL exception", ex2);
				}
			}
		}
	}

	@Transactional(readOnly = true)
	public boolean isCurrentExecutionComplete(User user, Request request)
			throws SecurityException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("Checking current execution status for user "
					+ user.getLoginName() + " on request "
					+ request.getRequestId());
		}
		if (!request.userCanRead(user)) {
			throw new SecurityException();
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn
					.prepareStatement("select count(1) from simlims_act_input where processed=false and requestId=?");
			stmt.setLong(1, request.getRequestId());
			rs = stmt.executeQuery();
			rs.next();
			boolean done = rs.getInt(1) == 0;
			if (log.isDebugEnabled()) {
				log.debug("Status done?: " + done);
			}
			return done;
		} catch (SQLException ex) {
			if (log.isDebugEnabled()) {
				log.debug("Failed to do check.", ex);
			}
			throw new IOException(ex);
		} finally {
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
					log.debug("Unimportant SQL exception", ex2);
				}
			}
		}
	}
}
