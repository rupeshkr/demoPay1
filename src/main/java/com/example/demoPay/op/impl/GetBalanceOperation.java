/**
 * 
 */
package com.example.demoPay.op.impl;

import java.util.List;

import com.example.demoPay.exceptions.BusinessException;
import com.example.demoPay.op.Context;

/**
 * Command: BALANCE
 * Possible results:
 *     ERR - NO USER
 *     <some value>
 * @author Rupesh.Kumar
 *
 */
public class GetBalanceOperation extends AbstractOperation {

	@Override
	public String performInternal(List<String> segments, Context context) throws BusinessException {
		return context.getUserManager().getBalance(context.getUsername()).toPlainString();
	}

	@Override
	protected boolean validateCommand(List<String> segments, Context context) {
		// one and only one segment
		return segments != null && segments.size() == 1;
	}
}
