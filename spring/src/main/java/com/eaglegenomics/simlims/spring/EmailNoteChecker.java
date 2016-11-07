package com.eaglegenomics.simlims.spring;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.eaglegenomics.simlims.core.Note;
import com.eaglegenomics.simlims.core.Request;
import com.eaglegenomics.simlims.core.User;
import com.eaglegenomics.simlims.core.manager.RequestManager;
import com.eaglegenomics.simlims.core.manager.SecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright (C) 2009 The Genome Analysis Center, Norwich, UK.
 * <p>
 * Monitors an email account at a certain address, and adds any emails received
 * from registered users as Notes on the request ID mentioned in the subject of
 * the email. Any emails received from non-registered users are discarded.
 * <p>
 * TODO Untested.
 * 
 * @author Richard Holland
 * @since 0.0.1
 */
public class EmailNoteChecker implements Runnable {
	protected static final Logger log = LoggerFactory.getLogger(EmailNoteChecker.class);

	public static enum Method {
		pop3
	}

	private final Pattern requestIdPattern = Pattern.compile("^\\[(\\d+\\].*$");

	private RequestManager requestManager;
	private SecurityManager securityManager;
	private Method method;
	private String server;
	private String username;
	private String password;
	private int checkInterval = 60; // Seconds

	public EmailNoteChecker() {
		new Thread(this).run();
	}

	public void run() {
		if (log.isInfoEnabled()) {
			log.info("Starting email checker.");
		}
		while (true) {
			try {
				doCheck();
			} catch (Throwable t) {
				if (log.isInfoEnabled()) {
					log.info("Failed to check mail.", t);
				}
			}
			try {
				if (log.isDebugEnabled()) {
					log.debug("Sleeping for " + checkInterval + " seconds");
				}
				Thread.sleep(checkInterval * 1000);
			} catch (InterruptedException e) {
				// No worries. Just check again then return to sleep. Don't need
				// to log.
			}
		}
	}

	private void doCheck() throws MessagingException, IOException,
			NumberFormatException {
		// Get system properties
		Properties props = System.getProperties();
		props.put("mail." + getMethod().toString() + ".host", getServer());
		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(getUsername(), getPassword());
			}
		};
		Session session = Session.getDefaultInstance(props, auth);

		// Get the store
		if (log.isDebugEnabled()) {
			log.debug("Connecting to mail server.");
		}
		Store store = session.getStore(getMethod().toString());
		store.connect();

		// Get folder
		if (log.isDebugEnabled()) {
			log.debug("Opening INBOX read-write.");
		}
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_WRITE);

		try {
			// Get directory
			if (log.isDebugEnabled()) {
				log.debug("Listing messages.");
			}
			for (Message message : folder.getMessages()) {
				if (log.isDebugEnabled()) {
					log.debug("Processing message from " + message.getFrom()[0]
							+ " with subject " + message.getSubject());
				}
				User user = securityManager.getUserByEmail(message.getFrom()[0]
						.toString());
				if (user != null) {
					if (log.isDebugEnabled()) {
						log.debug("From matches user " + user.getLoginName());
					}
					Matcher matcher = requestIdPattern.matcher(message
							.getSubject());
					String requestText = matcher.groupCount() > 0 ? matcher
							.group(1) : null;
					if (requestText != null) {
						if (log.isDebugEnabled()) {
							log.debug("Subject matches pattern.");
						}
						Request request = requestManager.getRequestById(Long
								.valueOf(requestText));
						if (request != null && request.userCanWrite(user)) {
							if (log.isDebugEnabled()) {
								log.debug("Subject matched request "
										+ requestText);
							}
							StringBuilder text = new StringBuilder();
							text.append("(by email, subject: ");
							text.append(message.getSubject());
							text.append(')');
							text.append(System.getProperty("line.separator"));
							text.append(System.getProperty("line.separator"));
							text.append(message.getContent());
							Note note = request.createNote(user);
							note.setText(text.toString());
							note.setInternalOnly(false);
							requestManager.saveRequest(request);
						}
					}
				}
				// We own this folder - all messages get deleted regardless.
				if (log.isDebugEnabled()) {
					log.debug("Deleting message");
				}
				message.setFlag(Flags.Flag.DELETED, true);
			}
		} finally {
			// Close connection
			if (log.isDebugEnabled()) {
				log.debug("Closing connection to mail server.");
			}
			folder.close(false);
			store.close();
		}
	}

	public SecurityManager getSecurityManager() {
		return securityManager;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public RequestManager getRequestManager() {
		return requestManager;
	}

	public void setRequestManager(RequestManager requestManager) {
		this.requestManager = requestManager;
	}

	public int getCheckInterval() {
		return checkInterval;
	}

	public void setCheckInterval(int checkInterval) {
		this.checkInterval = checkInterval;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
