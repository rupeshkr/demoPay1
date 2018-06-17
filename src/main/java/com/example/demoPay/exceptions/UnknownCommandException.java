/**
 * 
 */
package com.example.demoPay.exceptions;

/**
 * @author Rupesh.Kumar
 *
 */
public class UnknownCommandException extends BusinessException {

	/**
	 * @param string
	 */
	public UnknownCommandException(String string) {
		super(string);
	}

}
