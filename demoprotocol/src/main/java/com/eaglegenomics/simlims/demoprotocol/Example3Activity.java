package com.eaglegenomics.simlims.demoprotocol;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.eaglegenomics.simlims.core.AbstractManualActivity;
import com.eaglegenomics.simlims.core.ActivityData;
import com.eaglegenomics.simlims.core.ActivitySession;
import com.eaglegenomics.simlims.core.DataReference;
import com.eaglegenomics.simlims.core.exception.ActivityFailedException;
import com.eaglegenomics.simlims.core.store.DataReferenceStore;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * A demonstration activity.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public class Example3Activity extends AbstractManualActivity {
	protected static final Logger log = LoggerFactory.getLogger(Example3Activity.class);

	/**
	 * The name of the only property this activity requires to be set from user
	 * input.
	 */
	public static final String FINAL_APPEND_STR_KEY = "APPEND_STR";

	@Autowired(required = true)
	private DataReferenceStore dataReferenceStore;

	public void setDataReferenceStore(DataReferenceStore dataReferenceStore) {
		this.dataReferenceStore = dataReferenceStore;
	}

	public void execute(ActivitySession activitySession)
			throws ActivityFailedException {
		if (log.isInfoEnabled()) {
			log.info("Executing activity " + getUniqueIdentifier());
		}
		String appendStr = activitySession.getProperties().getProperty(
				FINAL_APPEND_STR_KEY);
		for (ActivityData activityData : activitySession.getLockedInputData()) {
			if (log.isInfoEnabled()) {
				log.info("Processing activity " + getUniqueIdentifier()
						+ " input " + activityData.getUniqueId());
			}
			try {
				String inputData = activityData.getDataReference().resolve();
				DataReference outputDataRef = dataReferenceStore
						.create(inputData + " " + appendStr);
				outputDataRef.save();
				activitySession.addOutputData(activityData,
						ActivityData.NO_INDEX, outputDataRef,
						ActivityData.NO_INDEX);
				if (log.isInfoEnabled()) {
					log.info("Processed activity " + getUniqueIdentifier()
							+ " input " + activityData.getUniqueId());
				}

			} catch (Exception ex) {
				if (log.isInfoEnabled()) {
					log.info("Failed activity " + getUniqueIdentifier()
							+ " input " + activityData.getUniqueId(), ex);
				}
				activitySession.addFailedData(activityData, ex);
			}
		}
		if (log.isInfoEnabled()) {
			log.info("Done executing activity " + getUniqueIdentifier());
		}
	}
}
