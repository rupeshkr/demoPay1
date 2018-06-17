/**
 * 
 */
package com.example.demoPay.op.impl;

import static com.example.demoPay.Constants.ERR_INSUFFICIENT_FUNDS;
import static com.example.demoPay.Constants.ERR_UNKNOWN_COMMAND;
import static com.example.demoPay.Constants.ERR_UNKNOW_USER;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demoPay.backend.UserManager;
import com.example.demoPay.exceptions.BusinessException;
import com.example.demoPay.op.Context;

/**
 * Various utility methods which could be used to verify constraints.
 * @author Rupesh.Kumar
 *
 */
public class Validators {

	private static final Logger logger = LoggerFactory.getLogger(Validators.class);
	
	/**
	 * Query UserManager to validate of the passed user is a valid user or not.
	 * 
	 * @param username
	 * @param context
	 * @throws BusinessException
	 */
	public static void validateUser(String username, Context context) throws BusinessException {
		UserManager uMgr = context.getUserManager();
		if (!uMgr.existsUser(username)) {
			String message = ERR_UNKNOW_USER.getMessage();
			logger.error("validateUser: User: " + username + ":" + message);
			throw new BusinessException(message);
		}
	}

	/**
	 * Query UserManager to validate if the user has enough balance to make the transfer.
	 * @param amount
	 * @param context
	 * @throws BusinessException
	 */
	public static void hasEnoughBalance(String amount, Context context) throws BusinessException {
		BigDecimal amt = validateAmount(amount, context);
		if (context.getUserManager().getBalance(context.getUsername()).compareTo(amt) < 0) {
			String message = ERR_INSUFFICIENT_FUNDS.getMessage();
			logger.error("hasEnoughBalance: " + amount + ":" + message);
			throw new BusinessException(message);
		}
	}

	/**
	 * Check if valid amount is being passed.
	 * @param amount
	 * @param context
	 * @throws BusinessException 
	 */
	private static BigDecimal validateAmount(String amount, Context context) throws BusinessException {
		try {
			return new BigDecimal(amount);
		} catch (Exception e) {
			String message = ERR_UNKNOWN_COMMAND.getMessage();
			logger.error("validateAmount: " + amount + ":" + message);
			throw new BusinessException(message);
		}
	}
}
