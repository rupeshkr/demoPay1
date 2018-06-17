/**
 * 
 */
package com.example.demoPay.op;

import java.util.List;
import java.util.Map;

import com.example.demoPay.exceptions.BusinessException;
import com.example.demoPay.exceptions.UnknownCommandException;

/**
 * A factory of operations with their corresponding command text.
 * @author Rupesh.Kumar
 *
 */
public class OperationFactory {

	private Map<String, Operation> operations;
	private Operation fallbackCommand;

	public OperationFactory(Map<String, Operation> operations, Operation fallbackCommand) {
		this.operations = operations;
		this.fallbackCommand = fallbackCommand;
	}

	/**
	 * Lookup Operation that could handle the smsContent
	 * 
	 * @param smsContent Contains info about the currently logged in user and other managers.
	 * @return Resolved operation, or fallbackOperation.
	 * @throws BusinessException 
	 */
	public Operation get(List<String> segments) throws BusinessException {
		if (segments == null || segments.isEmpty()) {
			throw new UnknownCommandException("Unable to resolve operation for command segment [" + segments + "]");
		}
		String command = segments.get(0);
		if (operations.containsKey(command)) {
			return operations.get(command);
		} else {
			return fallbackCommand;
		}
	}
 	}
