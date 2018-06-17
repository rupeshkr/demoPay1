/**
 * 
 */
package com.example.demoPay.op.impl;

import static com.example.demoPay.Constants.ERR_UNKNOWN_COMMAND;

import java.util.List;

import com.example.demoPay.exceptions.BusinessException;
import com.example.demoPay.op.Context;

/**
 * @author Rupesh.Kumar
 *
 */
public class UnknownOperation extends AbstractOperation {

	@Override
	public String performInternal(List<String> segments, Context context) throws BusinessException {
		// no-op
		return ERR_UNKNOWN_COMMAND.getMessage();
	}

}
