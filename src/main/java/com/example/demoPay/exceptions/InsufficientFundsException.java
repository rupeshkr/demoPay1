/**
 * 
 */
package com.example.demoPay.exceptions;

/**
 * @author Rupesh.Kumar
 *
 */
public class InsufficientFundsException extends BusinessException {

	/**
	 * @param string
	 */
	public InsufficientFundsException(String string) {
		super(string);
	}

}
