/**
 * 
 */
package com.example.demoPay.op.impl;

import static com.example.demoPay.op.impl.Validators.validateUser;
import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.List;

import com.example.demoPay.exceptions.BusinessException;
import com.example.demoPay.op.Context;

/**
 * Command: TOTAL-SENT-FFRITZ-MSMITH
 * Possible responses: <amount,....> ERR - NO USER
 * 
 * @author Rupesh.Kumar
 *
 */
public class TransferHistoryOperation extends AbstractOperation {

	private static final String SEPERATOR = ",";

	private static final String TX_SENT = "SENT";

	/**
	 * Segment 1: SEND
	 * Segment 2: <amount>
	 * Segment 3: <Receiving user>
	 */
	@Override
	public String performInternal(List<String> segments, Context context) throws BusinessException {
		String transferredAmount = null;
		String typeOfTransfer = segments.get(1);

		for (int i = 2; i < segments.size(); i++) {
			String receiver = segments.get(i);
			validateUser(receiver, context);
			List<BigDecimal> allTransactions = null;
			if (TX_SENT.equals(typeOfTransfer)) {
				allTransactions = context.getTransferManager().getAllTransactions(context.getUsername(), receiver);
			} else {

			}

			BigDecimal totalAmount = ZERO;

			if (allTransactions != null && !allTransactions.isEmpty()) {
				for (BigDecimal amt : allTransactions) {
					totalAmount = totalAmount.add(amt);
				}
			}

			if (transferredAmount == null) {
				transferredAmount = totalAmount.toPlainString();
			} else {
				transferredAmount += SEPERATOR + totalAmount.toPlainString();
			}
		}
		return transferredAmount;
	}

	@Override
	protected boolean validateCommand(List<String> segments, Context context) {
		return segments != null && segments.size() >= 3; // atleast 3 segments - command+type+user
	}

	public static interface TypedHelper {

	}
}
