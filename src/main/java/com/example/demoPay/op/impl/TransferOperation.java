/**
 * 
 */
package com.example.demoPay.op.impl;

import static com.example.demoPay.Constants.INFO_OK;
import static com.example.demoPay.op.impl.Validators.hasEnoughBalance;
import static com.example.demoPay.op.impl.Validators.validateUser;

import java.math.BigDecimal;
import java.util.List;

import com.example.demoPay.exceptions.BusinessException;
import com.example.demoPay.op.Context;

/**
 * Command: SEND-100-FFRITZ
 * Possible responses:
 *     OK
 *     ERR - INSUFFICIENT FUNDS
 * @author Rupesh.Kumar
 *
 */
public class TransferOperation extends AbstractOperation {

	/**
	 * Segment 1: SEND
	 * Segment 2: <amount>
	 * Segment 3: <Receiving user>
	 */
	@Override
	public String performInternal(List<String> segments, Context context) throws BusinessException {
		String receiver = segments.get(2);
		validateUser(receiver, context);
		String amount = segments.get(1);
		hasEnoughBalance(amount, context);
		context.getTransferManager().sendMoney(context.getUsername(), receiver, new BigDecimal(amount));
		return INFO_OK.getMessage();
	}

	@Override
	protected boolean validateCommand(List<String> segments, Context context) {
		return segments != null && segments.size() == 3;
	}
}
