/**
 * 
 */
package com.example.demoPay.op.impl;

import static com.example.demoPay.Constants.ERR_INVALID_COMMAND_SYNTAX;

import java.util.List;

import com.example.demoPay.exceptions.BusinessException;
import com.example.demoPay.op.Context;
import com.example.demoPay.op.Operation;

/**
 * Provides basic framework, like validating the passed command, for operation classes.
 * The concrete implementations should provide their own validations if required.
 * @author Rupesh.Kumar
 *
 */
public abstract class AbstractOperation implements Operation {
	/**
	 * Concrete classes should add their logic in this method implementation.
	 * 
	 * @param segments
	 * @param context
	 * @return
	 */
	public abstract String performInternal(List<String> segments, Context context) throws BusinessException;

	/**
	 * validate the command and then perform the action.
	 * 
	 * @param segments
	 * @param context
	 * @return
	 */
	@Override
	public final String perform(List<String> segments, Context context) throws BusinessException {
		if (validateCommand(segments, context)) {
			return performInternal(segments, context);
		}
		throw new BusinessException(ERR_INVALID_COMMAND_SYNTAX.getMessage());
	}
	
	/**
	 * Validate the passed command.
	 * Concrete implementations should override this if any validation is required.
	 * 
	 * @param segments
	 * @param context
	 * @return
	 */
	protected boolean validateCommand(List<String> segments, Context context) {
		// no validations
		return true;
	}

}
