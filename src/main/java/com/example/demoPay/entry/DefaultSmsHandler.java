/**
 * 
 */
package com.example.demoPay.entry;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demoPay.Constants;
import com.example.demoPay.backend.TransferManager;
import com.example.demoPay.backend.UserManager;
import com.example.demoPay.exceptions.BusinessException;
import com.example.demoPay.exceptions.InsufficientFundsException;
import com.example.demoPay.exceptions.InvalidUserException;
import com.example.demoPay.exceptions.SystemException;
import com.example.demoPay.exceptions.UnknownCommandException;
import com.example.demoPay.op.CommandSplitter;
import com.example.demoPay.op.Context;
import com.example.demoPay.op.Operation;
import com.example.demoPay.op.OperationFactory;

/**
 * This is the handler which identifies the command and delegates to appropriate Operation class for further processing.
 * 
 * @author Rupesh.Kumar
 *
 */
public class DefaultSmsHandler implements SMSHandler {
	private static final Logger logger = LoggerFactory.getLogger(DefaultSmsHandler.class);

	private CommandSplitter splitter;
	private UserManager userManager;
	private TransferManager transferManager;
	private OperationFactory opFactory;

	@Autowired
	public DefaultSmsHandler(CommandSplitter splitter, UserManager userManager, TransferManager transferManager, OperationFactory opFactory) {
		this.splitter = splitter;
		this.userManager = userManager;
		this.transferManager = transferManager;
		this.opFactory = opFactory;
	}

	/**
	 * Main entry point in the handler. Identifies the command, looks up the operation, performs operation, returns the received results.
	 * 
	 * @param smsContent SMS text
	 * @param senderDeviceId Device from which the message was sent. Used to identify the user.
	 */
	@Override
	public String handleSmsRequest(String smsContent, String senderDeviceId) {
		try {
			long startTime = System.currentTimeMillis();
			Context context = getContext(senderDeviceId); //first validate the sender and create context for futher processing
			List<String> segments = splitter.split(smsContent); // identify the command segments
			Operation op = opFactory.get(segments); // lookup operation
			String result = op.perform(segments, context); // perform command operation
			logMetrics(startTime); // log time taken
			return result;
		} catch (Exception e) {
			return handleException(e); 
		}
	}

	/**
	 * @param startTime
	 */
	private void logMetrics(long startTime) {
		long timeTaken = System.currentTimeMillis() - startTime;
		if (timeTaken < 1000) {
			logger.debug(String.format("SMS Command executed without any exceptions in %s ms.", timeTaken));
		} else {
			logger.debug(String.format("SMS Command executed without any exceptions in %s seconds.", timeTaken/1000));
		}
	}

	/**
	 * Handles all the relevant Exceptions and returns appropriate Message to be sent to the user.
	 * @param e
	 */
	private String handleException(Exception e) {
		String handleMessage = null;
		
		
		if (e instanceof InvalidUserException) {
			handleMessage = Constants.ERR_UNKNOW_USER.getMessage();
		} else if (e instanceof InsufficientFundsException) {
			handleMessage = Constants.ERR_INSUFFICIENT_FUNDS.getMessage();
		} else if (e instanceof UnknownCommandException) {
			handleMessage = Constants.ERR_UNKNOWN_COMMAND.getMessage();
		}

		if (handleMessage != null) {
			logger.debug("Handled exception.", e);
			return handleMessage;
		}

		if (e instanceof BusinessException || e instanceof SystemException) {
			logger.warn("Unhandled exception.", e);
			return e.getMessage();
		}
		
		logger.error("Unkown exception.", e);
		return e.getMessage();
	}

	/**
	 * Takes a senderDeviceId and returns context containing username and various helper/manager objects. 
	 * @param senderDeviceId
	 * @return
	 * @throws SystemException 
	 */
	private Context getContext(String senderDeviceId) throws Exception {
		if (senderDeviceId == null || senderDeviceId.trim().isEmpty()) {
			throw new SystemException(Constants.ERR_MSG_UNKNOW_USER_DEVICE_ID.getMessage());
		}
		String username = userManager.getUserNameForDeviceId(senderDeviceId);
		if (userManager.existsUser(username)) {
			return new Context(userManager, transferManager, username);
		} else {
			throw new InvalidUserException("Unknown logged in user. " + Constants.ERR_UNKNOW_USER.getMessage());
		}
	}
	
	

}
