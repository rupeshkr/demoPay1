/**
 * 
 */
package com.example.demoPay.op;

import java.util.List;

import com.example.demoPay.exceptions.BusinessException;

/**
 * Operation requested by the user. Will parse the command segements and then appropriately perform the action.
 * 
 * @author Rupesh.Kumar
 *
 */
public interface Operation {

	/**
	 * @param segments all the command segments from the smsCommand
	 * @return result of the operation the be sent back to the user.
	 * @throws BusinessException 
	 */
	String perform(List<String> segments, Context context) throws BusinessException;

}
